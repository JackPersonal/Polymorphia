package polymorphia.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polymorphia.characters.Character;

public class SleepCommand extends Command {
    private static final Logger logger = LoggerFactory.getLogger(SleepCommand.class);

    public SleepCommand(Character character) {

        super(CommandType.SLEEP, character, null);
    }

    @Override
    public boolean execute() {
        logger.info("Sleeping...");
        return true;
    }

    @Override
    public String uiLabel() {
        return "SLEEP:";
    }
}
