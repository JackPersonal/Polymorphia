package polymorphia.strategy;

import org.slf4j.Logger;
import polymorphia.CommandExecutor;
import polymorphia.artifacts.ArtifactType;
import polymorphia.artifacts.IArtifact;
import polymorphia.characters.Character;
import polymorphia.commands.CommandFactory;
import polymorphia.commands.ICommand;
import polymorphia.maze.Room;

import java.io.Serializable;
import java.util.*;

public abstract class BaseStrategy implements IStrategy, Serializable {
    protected static final Random random = new Random();
    static protected CommandFactory commandFactory = new CommandFactory();
//    private static Logger logger = org.slf4j.LoggerFactory.getLogger(CommandExecutor.class);

    public ICommand selectAction(Character myself, Room room) {
        Optional<Character> foe = room.getRandomCharacterNotMe(myself);
        if (foe.isPresent()) {
            return commandFactory.newFightCommand(myself, foe.get(), room);
        }
        Optional<IArtifact> food = room.getArtifact(ArtifactType.Food);
        if (food.isPresent()) {
            return commandFactory.newEatCommand(myself, room, food.get());
        }
        if (room.hasNeighbors()) {
            return commandFactory.newMoveCommand(myself, room, room.getRandomNeighbor());
        }
        return commandFactory.newSleepCommand(myself);
    }

    static List<IArtifact> getArtifacts(Room room, ArtifactType type) {
        return room.getArtifacts().stream()
                .filter(artifact -> artifact.isType(type))
                .toList();
    }

    static public List<ICommand> getPossibleActions(Character myself, Room room) {
        List<ICommand> possibleChoices = new ArrayList<>(Collections.singletonList(commandFactory.newSleepCommand(myself)));

        for (Character creature : room.getLivingCreatures()) {
            possibleChoices.add(commandFactory.newFightCommand(myself, creature, room));
        }
        for (IArtifact food : getArtifacts(room, ArtifactType.Food)) {
            possibleChoices.add(commandFactory.newEatCommand(myself, room, food));
        }
        for (IArtifact armor : getArtifacts(room, ArtifactType.Armor)) {
            possibleChoices.add(commandFactory.newWearCommand(myself, room, armor));
        }
        for (IArtifact weapon : getArtifacts(room, ArtifactType.Weapon)) {
            possibleChoices.add(commandFactory.newWieldCommand(myself, room, weapon));
        }
        for (IArtifact map : getArtifacts(room, ArtifactType.TreasureMap)) {
            possibleChoices.add(commandFactory.newTakeCommand(myself, room, map));
        }
        for (IArtifact cloak : getArtifacts(room, ArtifactType.Cloak)) {
            possibleChoices.add(commandFactory.newDonCommand(myself, room, cloak));
        }
        for (IArtifact potion : getArtifacts(room, ArtifactType.Potion)) {
            possibleChoices.add(commandFactory.newDrinkCommand(myself, room, potion));
        }
        for (IArtifact treasure : getArtifacts(room, ArtifactType.Treasure)) {
            possibleChoices.add(commandFactory.newTakeCommand(myself, room, treasure));
        }

        for (Room nextRoom : room.getNeighbors()) {
            possibleChoices.add(commandFactory.newMoveCommand(myself, room, nextRoom));
        }
//        logger.info("getPossibleActions ran: {}", possibleChoices);
        return possibleChoices;
    }
}
