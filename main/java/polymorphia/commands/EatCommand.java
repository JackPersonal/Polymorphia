package polymorphia.commands;

import org.slf4j.Logger;
import polymorphia.artifacts.IArtifact;
import polymorphia.characters.Character;
import polymorphia.maze.Room;


public class EatCommand extends ArtifactCommand {
    static Logger logger = org.slf4j.LoggerFactory.getLogger(EatCommand.class);

    public EatCommand(Character myself, Room room, IArtifact food) {
        super(CommandType.EAT, myself, room, food);
    }

    @Override
    public boolean execute() {
        if (!room.contains(myself)) {
            logger.info("{} tried to execute a command in room {}, but was not in that room", myself.getName(), room.getName());
        }
        if (!room.contains(artifact)) {
            logger.info("{} tried to eat food {} that was not in that room", myself.getName(), artifact.getName());
        }
        room.remove(artifact);
        return myself.eat(artifact);
    }
}
