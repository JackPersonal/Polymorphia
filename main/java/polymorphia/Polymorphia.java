package polymorphia;

import org.slf4j.Logger;
import polymorphia.characters.Character;
import polymorphia.commands.ICommand;
import polymorphia.maze.Maze;
import polymorphia.maze.Room;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static java.lang.Math.max;

public class Polymorphia implements Serializable {
    static Logger logger = org.slf4j.LoggerFactory.getLogger(Polymorphia.class);
    static Random random = new Random();

    private final String name;
    protected final Maze maze;
    protected Integer turnCount = 0;
    private Boolean treasureFound = false;
    private transient CommandExecutor commandExecutor;
    private transient boolean commandExecutorStarted;
    Thread commandThread;

    public Polymorphia(Maze maze) {
        this("Polymorphia_" + random.nextInt(1000), maze);
    }

    public Polymorphia(String name, Maze maze) {
        this.name = name;
        this.maze = maze;
        createCommandExecutor();
    }

    public void waitUntilIdle() {
        if (commandExecutor == null) {
            createCommandExecutor();
        }
        ensureCommandExecutorStarted();
        while (!commandExecutor.isIdle()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public String toString() {
        return "Polymorphia MAZE: turn " + turnCount + "\n" + maze.toString();
    }

    public String getName() {
        return name;
    }

    public Boolean isOver() {
        return !hasLivingCreatures() || !hasLivingAdventurers() || treasureFound;
    }

    protected void setTreasureFound() {
        treasureFound = true;
    }

    public Boolean hasLivingCreatures() {
        return maze.hasLivingCreatures();
    }

    public Boolean hasLivingAdventurers() {
        return maze.hasLivingAdventurers();
    }

    public void playTurn() {
        ensureCommandExecutorStarted();
        if (turnCount == 0) {
            logger.info("\nStarting play...\n");
        }
        turnCount += 1;

        for (Room room : maze) {    // using the iterator pattern
            for (Character character : room.getLivingCharacters()) {
                // We need to check again here, in case a previous command killed this character
                // We skip API players, since their commands are handled separately by the service API
                if (character.isAlive()) {
                    if (character.isAPIPlayer()) {
                        continue;
                    }
                    ICommand command = character.selectAction(room);
                    commandExecutor.put(command);
                    logger.info("Submitted command for {}: {}", character, command);
                    character.commandSubmitted();
                }
            }
        }
    }

    private List<Character> getLivingCharacters() {
        return maze.getLivingCharacters();
    }

    public void play() {
        ensureCommandExecutorStarted();
        while (!isOver()) {
            logger.info(this.toString());
            playTurn();
        }
        stopCommandExecutor();
        String eventDescription = createFinalResults();
        logger.info(eventDescription);
        EventBus.getInstance().postEvent(eventDescription);
    }

    private void createCommandExecutor() {
        this.commandExecutor = new CommandExecutor(max(2, maze.numLivingCharacters()));
    }

    private void startCommandExecutorThread() {
        commandThread = new Thread(commandExecutor, "command-executor");
        commandThread.setDaemon(true);
        commandThread.start();
        commandExecutorStarted = true;
        logger.info("Command Executor started.");
    }

    private void stopCommandExecutor() {
        if (commandExecutor != null) {
            commandExecutor.stop();
        }
        if (commandThread != null) {
            commandThread.interrupt();
            try {
                commandThread.join(1000);   // Wait one second for the thread to finish, otherwise interrupt it
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        commandExecutorStarted = false;
        commandThread = null;
        commandExecutor = null;
    }

    private void ensureCommandExecutorStarted() {
        logger.info("Starting up Command Executor from ensureCommandExecutorStarted...");
        if (commandExecutor == null) {
            createCommandExecutor();
        }
        if (commandExecutorStarted) {
            return;
        }
        startCommandExecutorThread();
    }

    public void stop() {
        stopCommandExecutor();
    }

    public void submitCommand(ICommand command) {
        commandExecutor.put(command);
    }

    // Stop/start and disconnect/connect CommandExecutor when serializing by overriding these methods:
    private void writeObject(ObjectOutputStream out) throws java.io.IOException {
        stopCommandExecutor();
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        createCommandExecutor();
        // These are set to false so that the thread is started when game play starts
        commandExecutorStarted = false;
        commandThread = null;
    }

    public String createFinalResults() {
        String eventDescription = "The game ended after " + turnCount + " turns.\n";
        if (treasureFound) {
            eventDescription += "The treasure was found!\n";
            eventDescription += getLivingCreatures().size() + " creatures were alive at the end\n";
        }
        if (hasLivingAdventurers()) {
            eventDescription += "The adventurers won! Left standing are: " + getAdventurerNames() + "\n";
        } else if (hasLivingCreatures()) {
            eventDescription += "The creatures won. Boo! Left standing are: " + getCreatureNames() + "\n";
        } else {
            eventDescription += "No team won! Everyone died!\n";
        }
        return eventDescription;
    }

    private String getAdventurerNames() {
        return String.join(", ", getLivingCharacters().stream().map(Object::toString).toList());
    }

    private String getCreatureNames() {
        return String.join(", ", getLivingCreatures().stream().map(Object::toString).toList());
    }

    private List<Character> getLivingCreatures() {
        return maze.getLivingCreatures();
    }

    public Character getWinner() {
        if (!isOver() || !hasLivingCharacters()) {
            // No one has won yet, or no one won -- all died
            return null;
        }
        return getLivingCharacters().getFirst();
    }

    private boolean hasLivingCharacters() {
        return maze.hasLivingCharacters();
    }

    public int getTurnCount() {
        return turnCount;
    }

    public boolean isTreasureFound() {
        return treasureFound;
    }

    public Optional<Character> getCharacter(String userName) {
        return maze.getCharacter(userName);
    }

    public Optional<Room> getRoomWithCharacter(Character character) {
        return maze.getRoomWithCharacter(character.getBaseName());
    }
}
