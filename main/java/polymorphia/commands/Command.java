package polymorphia.commands;

import polymorphia.characters.Character;
import polymorphia.maze.Room;
import polymorphia.strategy.StrategyFactory;

public abstract class Command implements ICommand {
    static StrategyFactory strategyFactory = new StrategyFactory();

    protected CommandType type;
    protected final Character myself;
    protected final Room room;

    public Command(CommandType type, Character myself, Room room) {
        this.type = type;
        this.myself = myself;
        this.room = room;
    }

    @Override
    public Character getCharacter() {
        return myself;
    }

    @Override
    public CommandType getType() {
        return type;
    }

    @Override
    public String optionString() {
        return "";
    }

}
