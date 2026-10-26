package polymorphia.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polymorphia.artifacts.IArtifact;
import polymorphia.characters.Character;
import polymorphia.commands.ICommand;
import polymorphia.maze.Room;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class SeekerStrategy extends BaseStrategy {
    static Logger logger = LoggerFactory.getLogger(SeekerStrategy.class);

    private Set<Room> visitedRooms = new HashSet<>();

    @Override
    public ICommand selectAction(Character myself, Room room) {
        visitedRooms.add(room);

        Optional<IArtifact> treasure = room.getTreasure();
        logger.info("{} looking for treasure in room {}", myself.getName(), room.getName());
        if (treasure.isPresent()) {
            logger.info("{} found treasure {} in room {}", myself.getName(), treasure.get(), room.getName());
            Optional<polymorphia.characters.Character> possibleGuard = room.getGuardOrCreature();
            if (possibleGuard.isEmpty()) {
                return commandFactory.newTakeCommand(myself, room, treasure.get());
            }
            logger.info("{} found guard {} in treasure room {}", myself.getName(), possibleGuard.get(), room.getName());
            return commandFactory.newFightCommand(myself, possibleGuard.get(), room);
        }
        Room newRoom = selectNewRoom(room);
        return commandFactory.newMoveCommand(myself, room, newRoom);
    }

    private Room selectNewRoom(Room room) {
        for (Room candidate : room.getNeighbors()) {
            if (!visitedRooms.contains(candidate)) {
                return candidate;
            }
        }
        return room.getRandomNeighbor();
    }
}
