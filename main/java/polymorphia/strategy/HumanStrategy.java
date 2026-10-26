package polymorphia.strategy;

import org.slf4j.Logger;
import polymorphia.characters.Character;
import polymorphia.commands.CommandType;
import polymorphia.commands.ICommand;
import polymorphia.maze.Room;

import java.util.*;
import java.util.stream.Collectors;

public class HumanStrategy extends BaseStrategy implements IStrategy {
    static Logger logger = org.slf4j.LoggerFactory.getLogger(HumanStrategy.class);

    public ICommand selectAction(Character character, Room room) {
        List<ICommand> possibleActions = getPossibleActions(character, room);
        CommandType topLevelChoice = getTopLevelChoice(possibleActions, character, room);
        List<ICommand> filteredActions = possibleActions.stream().filter(command -> command.getType() == topLevelChoice).collect(Collectors.toList());
        ICommand selectedAction = filteredActions.getFirst();
        if (filteredActions.size() > 1) {
            selectedAction = getSecondLevelChoice(filteredActions);
        }
        logger.info("Selected action: {}", selectedAction);
        return selectedAction;
    }

    private ICommand getSecondLevelChoice(List<ICommand> possibleActions) {
        int currentChoiceNumber = 1;
        for (ICommand command : possibleActions) {
            System.out.println(currentChoiceNumber + ": " + command.optionString());
            currentChoiceNumber++;
        }

        int optionNumber = getOptionNumber(possibleActions.size()) - 1;
        return possibleActions.get(optionNumber);
    }

    private static int getOptionNumber(int maxOptions) {
        while (true) {  // This exits the method if a valid option is selected
            System.out.print("Enter your option: ");
            Scanner scanner = new Scanner(System.in);

            int choiceNumber = Integer.parseInt(scanner.nextLine());
            if (choiceNumber >= 1 && choiceNumber <= maxOptions) {
                return choiceNumber;
            }
            System.out.println("Invalid option. Please try again.");
        }
    }

    CommandType getTopLevelChoice(List<ICommand> options, Character character, Room room) {
        Set<CommandType> availableCommandTypes = options.stream().map(ICommand::getType).collect(Collectors.toSet());

        Map<Integer, CommandType> optionMap = new HashMap<>();
        System.out.print(character.getName() + ", are in room " + room + "\n\n");
        int currentChoiceNumber = 1;
        for (CommandType option : availableCommandTypes) {
            System.out.println(currentChoiceNumber + ": " + option);
            optionMap.put(currentChoiceNumber, option);
            currentChoiceNumber++;
        }
        return optionMap.get(getOptionNumber(availableCommandTypes.size()));
    }
}