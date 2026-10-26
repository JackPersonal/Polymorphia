package polymorphia.strategy;

import org.slf4j.Logger;
import polymorphia.characters.Character;
import polymorphia.commands.ICommand;
import polymorphia.maze.Room;

import java.util.List;


public class RandomStrategy extends BaseStrategy {
    static Logger logger = org.slf4j.LoggerFactory.getLogger(RandomStrategy.class);

    @Override
    public ICommand selectAction(Character myself, Room room) {
        List<ICommand> possibleActions = getPossibleActions(myself, room);
        ICommand choice = possibleActions.get(random.nextInt(possibleActions.size()));
        logger.info("Random choice: {} out of {} options", choice, possibleActions.size());
        return choice;
    }
}
