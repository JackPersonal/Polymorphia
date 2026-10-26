package polymorphia.commands;

import org.slf4j.Logger;
import polymorphia.artifacts.IArtifact;
import polymorphia.characters.Character;
import polymorphia.maze.Room;


public class WearCommand extends ArtifactCommand {
    static Logger logger = org.slf4j.LoggerFactory.getLogger(WearCommand.class);

    public WearCommand(Character character, Room room, IArtifact armor) {
        super(CommandType.WEAR, character, room, armor);
    }

    @Override
    public boolean execute() {
        IArtifact armor = getArtifactFromRoom();
        Character armoredMe = characterFactory.newArmoredCharacter(this.myself, armor);
        room.replace(myself, armoredMe);
        logger.info("{} put on: {}", myself.getName(), armor);
        return true;
    }
}
