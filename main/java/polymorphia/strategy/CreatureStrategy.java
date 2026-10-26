package polymorphia.strategy;

import polymorphia.characters.Character;
import polymorphia.commands.ICommand;
import polymorphia.maze.Room;

import java.util.Optional;

public class CreatureStrategy extends BaseStrategy {
    // Creatures fight adventurers or other creatures

    @Override
    public ICommand selectAction(Character myself, Room currentRoom) {
        Optional<Character> character = currentRoom.getRandomCharacterNotMe(myself);
        if (character.isPresent()) {
            return commandFactory.newFightCommand(myself, character.get(), currentRoom);
        }
        if (currentRoom.hasNeighbors()) {
            return commandFactory.newMoveCommand(myself, currentRoom, currentRoom.getRandomNeighbor());
        }
        return super.selectAction(myself, currentRoom);
    }
}
