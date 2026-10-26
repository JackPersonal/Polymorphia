package polymorphia.strategy;

import polymorphia.characters.Character;
import polymorphia.commands.ICommand;
import polymorphia.maze.Room;

public class SleepStrategy extends BaseStrategy {

    @Override
    public ICommand selectAction(Character myself, Room room) {
        return commandFactory.newSleepCommand(myself);
    }
}
