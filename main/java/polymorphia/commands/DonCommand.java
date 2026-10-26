package polymorphia.commands;

import org.slf4j.Logger;
import polymorphia.artifacts.IArtifact;
import polymorphia.characters.Character;
import polymorphia.maze.Room;

public class DonCommand extends ArtifactCommand {
    static Logger logger = org.slf4j.LoggerFactory.getLogger(DonCommand.class);

    public DonCommand(Character character, Room room, IArtifact cloak) {
        super(CommandType.DON, character, room, cloak);
    }

    @Override
    public boolean execute() {
        IArtifact cloak = getArtifactFromRoom();
        Character cloakedMe = characterFactory.newCloakedCharacter(myself, cloak);
        room.replace(myself, cloakedMe);
        logger.info("{} donned: {}", myself.getName(), cloak);
        return true;
    }
}
