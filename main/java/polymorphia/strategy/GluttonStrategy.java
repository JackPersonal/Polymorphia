package polymorphia.strategy;

import polymorphia.artifacts.ArtifactType;
import polymorphia.artifacts.IArtifact;
import polymorphia.characters.Character;
import polymorphia.commands.ICommand;
import polymorphia.maze.Room;

import java.util.Optional;

public class GluttonStrategy extends BaseStrategy {

    @Override
    public ICommand selectAction(Character character, Room room) {

        Optional<IArtifact> potion = room.getArtifact(ArtifactType.Potion);
        if (potion.isPresent()) {
            return commandFactory.newDrinkCommand(character, room, potion.get());
        }
        Optional<IArtifact> food = room.getFood();
        if (food.isPresent()) {
            return commandFactory.newEatCommand(character, room, food.get());
        }
        return commandFactory.newMoveCommand(character, room, room.getRandomNeighbor());
    }

    @Override
    public boolean isGlutton() {
        return true;
    }
}
