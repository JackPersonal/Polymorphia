package polymorphia.strategy;

import polymorphia.artifacts.ArtifactType;
import polymorphia.artifacts.IArtifact;
import polymorphia.characters.Character;
import polymorphia.commands.ICommand;
import polymorphia.maze.Room;

import java.util.Optional;

public class CowardStrategy extends BaseStrategy {

    @Override
    public ICommand selectAction(Character myself, Room room) {
        Optional<IArtifact> cloak = room.getArtifact(ArtifactType.Cloak);
        if (cloak.isPresent()) {
            return commandFactory.newDonCommand(myself, room, cloak.get());
        }
        if (room.hasLivingCreatures() && room.hasNeighbors()) {
            return commandFactory.newMoveCommand(myself, room, room.getRandomNeighbor());
        }
        Optional<IArtifact> map = room.getMap();
        if (map.isPresent()) {
            return commandFactory.newTakeCommand(myself, room, map.get());
        }
        Optional<IArtifact> food = room.getFood();
        if (food.isPresent()) {
            return commandFactory.newEatCommand(myself, room, food.get());
        }
        if (room.hasNeighbors()) {
            return commandFactory.newMoveCommand(myself, room, room.getRandomNeighbor());
        }
        return commandFactory.newSleepCommand(myself);
    }

    @Override
    public boolean isCoward() {
        return true;
    }
}
