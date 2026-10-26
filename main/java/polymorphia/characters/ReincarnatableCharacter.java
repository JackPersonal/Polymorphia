package polymorphia.characters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polymorphia.EventBus;
import polymorphia.EventType;
import polymorphia.artifacts.IArtifact;
import polymorphia.commands.CommandFactory;

public class ReincarnatableCharacter extends DecoratedCharacter {
    private static final Logger logger = LoggerFactory.getLogger(ReincarnatableCharacter.class);
    private static final CharacterFactory characterFactory = new CharacterFactory();
    private static final CommandFactory commandFactory = new CommandFactory();

    public ReincarnatableCharacter(Character character, IArtifact potion) {
        super(character, potion);
    }

    @Override
    public String getName() {
        return "Reincarnatable " + myself.getName();
    }

    @Override
    protected void loseHealth(double healthToLose) {
        super.loseHealth(healthToLose);
        if (!isAlive()) {
            // If I died, I'll reincarnate (without the ability to do it again)
            Character rebornSelf = characterFactory.createKnight("Reborn " + myself.getBaseName(), artifact.getHealthValue());
            String msg = String.format("%s just reborn with health %.2f!", myself.getBaseName(), artifact.getHealthValue());
            logger.info(msg);

            // Polymorphia should receive this event and add the new character to a random room
            // The old character, now dead, stays in the game but is no longer active
            EventBus.getInstance().postEvent(EventType.ADD_CHARACTER, rebornSelf);
        }
    }

    public double getRebornHealth() {
        return artifact.getHealthValue();
    }
}
