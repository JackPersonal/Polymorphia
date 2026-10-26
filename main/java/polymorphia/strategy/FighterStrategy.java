package polymorphia.strategy;

import polymorphia.artifacts.ArtifactType;
import polymorphia.artifacts.IArtifact;
import polymorphia.characters.Character;
import polymorphia.commands.ICommand;
import polymorphia.maze.Room;

import java.util.Optional;

public class FighterStrategy extends BaseStrategy {

    @Override
    public ICommand selectAction(Character myself, Room room) {
        Optional<IArtifact> armor = room.getArtifact(ArtifactType.Armor);
        if (armor.isPresent()) {
            return commandFactory.newWearCommand(myself, room, armor.get());
        }
        Optional<IArtifact> weapon = room.getArtifact(ArtifactType.Weapon);
        if (weapon.isPresent()) {
            return commandFactory.newWieldCommand(myself, room, weapon.get());
        }
        Optional<Character> creature = room.getRandomCreature();
        if (creature.isPresent()) {
            return commandFactory.newFightCommand(myself, creature.get(), room);
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
    public boolean isFighter() {
        return true;
    }
}
