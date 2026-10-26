package polymorphia.commands;

import org.slf4j.Logger;
import polymorphia.artifacts.IArtifact;
import polymorphia.characters.Character;
import polymorphia.maze.Room;

public class WieldCommand extends ArtifactCommand {
    static Logger logger = org.slf4j.LoggerFactory.getLogger(WieldCommand.class);

    public WieldCommand(Character character, Room room, IArtifact weapon) {
        super(CommandType.WIELD, character, room, weapon);
    }

    @Override
    public boolean execute() throws IllegalStateException {
        IArtifact weapon = getArtifactFromRoom();
        Character weaponizedMe = characterFactory.newWeaponizedCharacter(myself, weapon);
        room.replace(myself, weaponizedMe);
        logger.info("{} took: {}", myself.getName(), weapon);
        return true;
    }
}
