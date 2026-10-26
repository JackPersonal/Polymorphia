package polymorphia;

import org.slf4j.Logger;
import polymorphia.characters.Character;
import polymorphia.commands.ICommand;

import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class CommandExecutor implements Runnable {
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(CommandExecutor.class);

    BlockingQueue<ICommand> commandQueue;
    Set<Character> charactersWithPendingCommands = new ConcurrentHashMap<>().newKeySet();

    // This variable is used to signal the command-processing thread to stop
    private volatile boolean running = true;

    public CommandExecutor(int queueSize) {
        this.commandQueue = new ArrayBlockingQueue<>(queueSize);
    }

    public void put(ICommand command) {
        try {
            commandQueue.put(command);
            charactersWithPendingCommands.add(command.getCharacter());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.info("Command Executor thread interrupted putting command on queue, shutting down...");
        }
    }

    public void stop() {
        logger.info("Stopping CommandExecutor and clearing queue and pending Characters...");
        running = false;
        commandQueue.clear();
        charactersWithPendingCommands.clear();
    }

    @Override
    public void run() {
        logger.info("Starting up Command Executor on thread " + Thread.currentThread().getName());
        while (running && !Thread.currentThread().isInterrupted()) {
            try {
                logger.info("Waiting for command...");
                ICommand command = commandQueue.take();
                logger.info("Command retrieved: " + command.uiLabel());
                Character character = command.getCharacter();
                charactersWithPendingCommands.remove(character);
                if (character.isAlive()) {
                    logger.info("Executing command: {}", command);
                    command.execute();
                    character.turnTaken();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.info("Command Executor thread interrupted, shutting down...");
                break;
            }
        }
    }

    public boolean isIdle() {
        return commandQueue.isEmpty() && charactersWithPendingCommands.isEmpty();
    }
}
