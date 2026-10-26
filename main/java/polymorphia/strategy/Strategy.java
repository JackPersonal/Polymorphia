package polymorphia.strategy;

import polymorphia.characters.Character;
import polymorphia.commands.CommandFactory;
import polymorphia.commands.ICommand;
import polymorphia.maze.Room;

public abstract class Strategy implements IStrategy {
    static protected CommandFactory commandFactory = new CommandFactory();

    @Override
    public ICommand selectAction(Character myself, Room currentRoom) {
        return commandFactory.newSleepCommand(myself);
    }
}
