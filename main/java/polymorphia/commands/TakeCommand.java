package polymorphia.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polymorphia.EventBus;
import polymorphia.artifacts.ArtifactType;
import polymorphia.artifacts.IArtifact;
import polymorphia.characters.Character;
import polymorphia.maze.Room;


public class TakeCommand extends Command {
    static Logger logger = LoggerFactory.getLogger(TakeCommand.class);

    IArtifact artifact;

    public TakeCommand(Character myself, Room room, IArtifact artifact) {
        super(CommandType.TAKE, myself, room);
        this.artifact = artifact;
    }

    @Override
    public boolean execute() {
        if (!room.contains(myself)) {
            logger.info("{} tried to execute a command in room {}, but was not in that room", myself.getName(), room.getName());
        }
        if (!room.contains(artifact)) {
            logger.info("{} tried to take artifact {} that was not in that room", myself.getName(), artifact.getName());
        }

        room.remove(artifact);
        String msg = String.format("%s took %s from room %s", myself.getName(), artifact, room.getName());
        logger.info(msg);
        EventBus.getInstance().postEvent(msg);

        if (artifact.isType(ArtifactType.Treasure)) {
            EventBus.getInstance().postEvent(myself.getName() + " found a treasure!");
        }

        if (myself.isCoward() && artifact.isType(ArtifactType.TreasureMap)) {
            myself.setStrategy(strategyFactory.newSeekerStrategy());
        }
        return true;
    }
}
