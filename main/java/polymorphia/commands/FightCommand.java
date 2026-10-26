package polymorphia.commands;

import org.slf4j.Logger;
import polymorphia.characters.Character;
import polymorphia.maze.Room;

public class FightCommand extends Command {
    static Logger logger = org.slf4j.LoggerFactory.getLogger(FightCommand.class);

    private Character foe;

    public FightCommand(Character me, Character foe, Room room) {
        super(CommandType.FIGHT, me, room);
        this.foe = foe;
    }

    @Override
    public boolean execute() {
        if (!room.contains(myself)) {
            logger.info("{} tried to execute a command in room {}, but was not in that room", myself.getName(), room.getName());
        }
        if (!room.contains(foe)) {
            logger.info("{} tried to fight foe {} that was not in that room", myself.getName(), foe.getName());
        }
        return myself.fight(foe);
    }

    @Override
    public String optionString() {
        return foe.getName();
    }
}
