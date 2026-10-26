package polymorphia.commands;

import org.slf4j.Logger;
import polymorphia.characters.Character;
import polymorphia.maze.Room;

public class MoveCommand extends Command {
    static Logger logger = org.slf4j.LoggerFactory.getLogger(MoveCommand.class);

    private final Room newRoom;

    public MoveCommand(Character myself, Room room, Room newRoom) {
        super(CommandType.MOVE, myself, room);
        this.newRoom = newRoom;
    }

    @Override
    public boolean execute() {
        if (!room.contains(myself)) {
            logger.info("{} tried to execute a command in room {}, but was not in that room", myself.getName(), room.getName());
        }
        if (!room.hasNeighbor(newRoom)) {
            logger.info("{} tried to move to room {}, but that room was not a neighbor of current room {}", myself.getName(), newRoom.getName(), room.getName());
        }
        newRoom.add(myself);
        room.remove(myself);

        myself.movedTo(newRoom);
        return true;
    }

    @Override
    public String optionString() {
        return newRoom.getName();
    }

}
