package polymorphia.commands;

import org.slf4j.Logger;
import polymorphia.artifacts.IArtifact;
import polymorphia.characters.Character;
import polymorphia.characters.CharacterFactory;
import polymorphia.maze.Room;

public abstract class ArtifactCommand extends Command {
    static Logger logger = org.slf4j.LoggerFactory.getLogger(ArtifactCommand.class);
    static final CharacterFactory characterFactory = new CharacterFactory();

    IArtifact artifact;

    ArtifactCommand(CommandType type, Character character, Room room, IArtifact artifact) {
        super(type, character, room);
        this.artifact = artifact;
    }

    IArtifact getArtifactFromRoom() {
        if (!room.contains(artifact)) {
            logger.warn("Artifact not in room");
        }
        room.remove(artifact);
        return artifact;
    }

    @Override
    public String optionString() {
        return artifact.getName();
    }

}
