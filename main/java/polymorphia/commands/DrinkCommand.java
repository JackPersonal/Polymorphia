package polymorphia.commands;

import org.slf4j.Logger;
import polymorphia.artifacts.IArtifact;
import polymorphia.characters.Character;
import polymorphia.maze.Room;

public class DrinkCommand extends ArtifactCommand {
    static Logger logger = org.slf4j.LoggerFactory.getLogger(DrinkCommand.class);

    public DrinkCommand(Character character, Room room, IArtifact potion) {
        super(CommandType.DRINK, character, room, potion);
    }

    @Override
    public boolean execute() {
        IArtifact potion = getArtifactFromRoom();
        Character reincarnatableMe = characterFactory.newReincarnatableMeCharacter(myself, potion);
        room.replace(myself, reincarnatableMe);
        logger.info("{} drank: {}", myself.getName(), potion);
        return true;
    }
}
