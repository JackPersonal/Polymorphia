package polymorphia;

import org.apache.commons.cli.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class GameConfiguratorTest {

    @Disabled
    void testParsingLongArgumentsAndRunningGame() throws ParseException {
        String[] args = new String[]{
                "--numberOfRooms", "11",
                "--humanPlayerNames", "Professor Holmes",
                "--numberOfKnights", "1",
                "--numberOfCreatures", "1",
                "--numberOfFoodItems", "2",
                "--numberOfArmoredSuits", "2"
        };
        GameConfigurator.main(args);
    }

    @Disabled
    void testParsingShortArguments() throws ParseException {
        String[] args = new String[]{
                "-r", "3",
                "-a", "3",
                "-c", "3",
                "-f", "3"
        };
        GameConfigurator.main(args);
    }

    @Disabled
    void testHelp() throws ParseException {
        String[] args = new String[]{
                "--help"
        };
        GameConfigurator.main(args);
    }

    @Disabled
    void testOptionParsing() throws ParseException {
        String[] args = new String[]{
                "-r", "3",
                "-a", "3",
                "-c", "3",
                "-f", "3",
                "-help"
        };

        CommandLineParser parser = new DefaultParser();
        CommandLine cmdLine = parser.parse(GameConfigurator.getOptions(), args);
        for (Option option : cmdLine.getOptions()) {
            System.out.println(option.getLongOpt() + " = " + cmdLine.getOptionValue(option.getLongOpt()));
        }
    }
}