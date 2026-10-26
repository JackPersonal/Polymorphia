package polymorphia;

import ooad.gameobserver.intf.IGameObserver;
import ooad.mazeobserver.MazeObserver;
import org.apache.commons.cli.*;
import polymorphia.artifacts.ArtifactFactory;
import polymorphia.artifacts.IArtifact;
import polymorphia.characters.Character;
import polymorphia.characters.CharacterFactory;
import polymorphia.maze.Maze;
import polymorphia.maze.RoomFactory;

public class GameConfigurator {
    static int SECONDS_TO_PAUSE_BETWEEN_TURNS = 1;
    static int MINIMUM_CONNECTIONS = 3;

    static CharacterFactory characterFactory = new CharacterFactory();
    static ArtifactFactory artifactFactory = new ArtifactFactory();
    static RoomFactory roomFactory = new RoomFactory();

    Maze.Builder mazeBuilder;

    // TODO: add option for graphical display of maze
    // TODO: add fight-to-death command
    /*
    +----------------------+---------------------------+-----------------------------+----------------------------------------------+
    | Short Option         | Long Option               | Argument                    | Description                                  |
    +----------------------+---------------------------+-----------------------------+----------------------------------------------+
    | -a                   | --numberOfArmoredSuits    | <integer>                   | the number of armored suits in the maze      |
    | -c                   | --numberOfCreatures       | <integer>                   | the number of creatures in the maze          |
    | -d                   | --numberOfDemons          | <integer>                   | the number of demons in the maze             |
    | -f                   | --numberOfFoodItems       | <integer>                   | the number of food items in the maze         |
    | -g                   | --numberOfGluttons        | <integer>                   | the number of gluttons in the maze           |
    | -k                   | --numberOfKnights         | <integer>                   | the number of knights in the maze            |
    | -l                   | --numberOfCloaks          | <integer>                   | the number of cloaks in the maze             |
    | -m                   | --numberOfTreasureMaps    | <integer>                   | the number of treasure maps in the maze      |
    | -o                   | --numberOfCowards         | <integer>                   | the number of cowards in the maze            |
    | -p                   | --numberOfPotions         | <integer>                   | the number of potions in the maze            |
    | -r                   | --numberOfRooms           | <integer>                   | the number of rooms in the maze              |
    | -s                   | --numberOfSeekers         | <integer>                   | the number of seekers in the maze            |
    | -t                   | --numberOfTreasures       | <integer>                   | the number of treasures in the maze          |
    | -w                   | --numberOfWeapons         | <integer>                   | the number of weapons in the maze            |
    | -x                   | --addGraphicalView        | <integer>                   | adds a graphical view of the maze            |
    | -y                   | --humanPlayerNames        | "<name1>,<name2>,...,<nameN>" | the human player names                     |
    +----------------------+---------------------------+-----------------------------+----------------------------------------------+
    */
    GameConfigurator(CommandLine cmdLine) throws ParseException {
        mazeBuilder = Maze.getNewBuilder(roomFactory);
        buildMazeFromArguments(cmdLine);
    }

    public static void main(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmdLine = parser.parse(getOptions(), args);
            if (cmdLine.hasOption('h')) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("java -jar polymorphia.jar", getOptions());
            } else {
                GameConfigurator gameConfig = new GameConfigurator(cmdLine);
                gameConfig.createAndStartGame(cmdLine.hasOption('x'));
            }
            System.exit(0);
        } catch (ParseException e) {
            System.out.println("Error parsing command line arguments: " + e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar polymorphia.jar", getOptions());
            System.exit(0);
        }
    }

    private void createAndStartGame(boolean addGraphicalView) {
        ObservablePolymorphia polymorphia = new ObservablePolymorphia(mazeBuilder.build());
        if (addGraphicalView) {
            IGameObserver observer = createMazeObserver(polymorphia);
            polymorphia.attach(observer);
        }

        polymorphia.play();
    }

    private void buildMazeFromArguments(CommandLine cmdLine) throws ParseException {
        int numRooms = 6;
        if (cmdLine.hasOption("r")) {
            numRooms = ((Number) cmdLine.getParsedOptionValue("r")).intValue();
        }
        mazeBuilder.createConnectedRooms(MINIMUM_CONNECTIONS, numRooms);

        int numArmorSuits = 2;
        if (cmdLine.hasOption("a")) {
            numArmorSuits = ((Number) cmdLine.getParsedOptionValue("a")).intValue();
        }
        mazeBuilder.addArtifacts(artifactFactory.createArmoredSuits(numArmorSuits));

        int numCreatures = 3;
        if (cmdLine.hasOption("c")) {
            numCreatures = ((Number) cmdLine.getParsedOptionValue("c")).intValue();
        }
        mazeBuilder.addCharacters(characterFactory.createCreatures(numCreatures));

        int numDemons = 3;
        if (cmdLine.hasOption("d")) {
            numDemons = ((Number) cmdLine.getParsedOptionValue("d")).intValue();
        }
        mazeBuilder.addCharacters(characterFactory.createDemons(numDemons));

        int numFoodItems = numRooms;
        if (cmdLine.hasOption("f")) {
            numFoodItems = ((Number) cmdLine.getParsedOptionValue("f")).intValue();
        }
        mazeBuilder.addArtifacts(artifactFactory.createFoodItems(numFoodItems));

        int numGluttons = 2;
        if (cmdLine.hasOption("g")) {
            numGluttons = ((Number) cmdLine.getParsedOptionValue("g")).intValue();
        }
        mazeBuilder.addCharacters(characterFactory.createGluttons(numGluttons));


        int numKnights = 2;
        if (cmdLine.hasOption("k")) {
            numKnights = ((Number) cmdLine.getParsedOptionValue("k")).intValue();
        }
        mazeBuilder.addCharacters(characterFactory.createKnights(numKnights));

        int numCloaks = 2;
        if (cmdLine.hasOption("l")) {
            numCloaks = ((Number) cmdLine.getParsedOptionValue("l")).intValue();
        }
        mazeBuilder.addArtifacts(artifactFactory.createCloaks(numCloaks));

        int numTreasureMaps = 2;
        if (cmdLine.hasOption("m")) {
            numTreasureMaps = ((Number) cmdLine.getParsedOptionValue("m")).intValue();
        }
        mazeBuilder.addArtifacts(artifactFactory.createTreasureMaps(numTreasureMaps));

        int numCowards = 2;
        if (cmdLine.hasOption("o")) {
            numCowards = ((Number) cmdLine.getParsedOptionValue("o")).intValue();
        }
        mazeBuilder.addCharacters(characterFactory.createCowards(numCowards));

        int numPotions = 2;
        if (cmdLine.hasOption("p")) {
            numPotions = ((Number) cmdLine.getParsedOptionValue("p")).intValue();
        }
        mazeBuilder.addArtifacts(artifactFactory.createPotions(numPotions));

        int numSeekers = 1;
        if (cmdLine.hasOption("s")) {
            numSeekers = ((Number) cmdLine.getParsedOptionValue("s")).intValue();
        }
        mazeBuilder.addCharacters(characterFactory.createSeekers(numSeekers));

        int numTreasures = 1;
        if (cmdLine.hasOption("t")) {
            numTreasures = ((Number) cmdLine.getParsedOptionValue("t")).intValue();
        }
        for (int i = 0; i < numTreasures; i++) {
            IArtifact treasure = artifactFactory.createTreasure();
            Character guard = characterFactory.createGuard();
            mazeBuilder.addToSameRoom(guard, treasure);
        }

        int numWeapons = 2;
        if (cmdLine.hasOption("w")) {
            numWeapons = ((Number) cmdLine.getParsedOptionValue("w")).intValue();
        }
        mazeBuilder.addArtifacts(artifactFactory.createWeapons(numWeapons));

        if (cmdLine.hasOption("y")) {
            String[] names = cmdLine.getOptionValue("y").split("\s");
            mazeBuilder.addCharacters(characterFactory.createHumans(names));
        }
    }


    static Options getOptions() {
        Option numArmoredSuits = Option.builder("a")
                .longOpt("numberOfArmoredSuits")
                .argName("numArmorSuits")
                .hasArg()
                .numberOfArgs(1)
                .type(Number.class)
                .desc("the number of armored suits to place in the maze")
                .build();

        Option numCreatures = Option.builder("c")
                .longOpt("numberOfCreatures")
                .argName("numCreatures")
                .hasArg()
                .type(Number.class)
                .numberOfArgs(1)
                .desc("the number of creatures to place in the maze")
                .build();

        Option numDemons = Option.builder("d")
                .longOpt("numberOfDemons")
                .argName("numDemons")
                .hasArg()
                .type(Number.class)
                .numberOfArgs(1)
                .desc("the number of demons to place in the maze")
                .build();

        Option numFoodItems = Option.builder("f")
                .longOpt("numberOfFoodItems")
                .argName("numFoodItems")
                .hasArg()
                .numberOfArgs(1)
                .type(Number.class)
                .desc("the number of food items to place in the maze")
                .build();

        Option help = Option.builder("h")
                .longOpt("help")
                .build();

        Option numGluttons = Option.builder("g")
                .longOpt("numberOfGluttons")
                .argName("numberOfGluttons")
                .hasArg()
                .type(Number.class)
                .numberOfArgs(1)
                .desc("the number of gluttons to place in the maze")
                .build();

        Option numKnights = Option.builder("k")
                .longOpt("numberOfKnights")
                .argName("numberOfKnights")
                .hasArg()
                .type(Number.class)
                .numberOfArgs(1)
                .desc("the number of knights to place in the maze")
                .build();

        Option numCloaks = Option.builder("l")
                .longOpt("numberOfCloaks")
                .argName("numberOfCloaks")
                .hasArg()
                .type(Number.class)
                .numberOfArgs(1)
                .desc("the number of cloaks to place in the maze")
                .build();

        Option numTreasureMaps = Option.builder("m")
                .longOpt("numberOfTreasuresMaps")
                .argName("numberOfTreasuresMaps")
                .hasArg()
                .type(Number.class)
                .numberOfArgs(1)
                .desc("the number of treasure maps to place in the maze")
                .build();

        Option numCowards = Option.builder("o")
                .longOpt("numberOfCowards")
                .argName("numberOfCowards")
                .hasArg()
                .type(Number.class)
                .numberOfArgs(1)
                .desc("the number of cowards to place in the maze")
                .build();

        Option numPotions = Option.builder("p")
                .longOpt("numberOfPotions")
                .argName("numberOfPotions")
                .hasArg()
                .numberOfArgs(1)
                .type(Number.class)
                .desc("the number of potions in the maze")
                .build();

        Option numRooms = Option.builder("r")
                .longOpt("numberOfRooms")
                .argName("numRooms")
                .hasArg()
                .numberOfArgs(1)
                .type(Number.class)
                .desc("the number of rooms in the maze")
                .build();

        Option numSeekers = Option.builder("s")
                .longOpt("numberOfSeekers")
                .argName("numberOfSeekers")
                .hasArg()
                .numberOfArgs(1)
                .type(Number.class)
                .desc("the number of seekers in the maze")
                .build();

        Option numTreasures = Option.builder("t")
                .longOpt("numberOfTreasures")
                .argName("numberOfTreasures")
                .hasArg()
                .numberOfArgs(1)
                .type(Number.class)
                .desc("the number of treasures in the maze")
                .build();

        Option numWeapons = Option.builder("w")
                .longOpt("numberOfWeapons")
                .argName("numberOfWeapons")
                .hasArg()
                .numberOfArgs(1)
                .type(Number.class)
                .desc("the number of weapons in the maze")
                .build();

        Option addGraphicalView = Option.builder("x")
                .longOpt("addGraphicalView")
                .argName("addGraphicalView")
                .desc("adds a graphical view of the maze")
                .build();

        Option humanPlayers = Option.builder("y")
                .longOpt("humanPlayerNames")
                .argName("humanPlayerNames")
                .hasArg()
                .numberOfArgs(1)
                .type(String.class)
                .desc("the human player's name")
                .build();

        Options options = new Options();
        options.addOption(numArmoredSuits);
        options.addOption(numCreatures);
        options.addOption(numDemons);
        options.addOption(numFoodItems);
        options.addOption(numGluttons);
        options.addOption(help);
        options.addOption(numKnights);
        options.addOption(numCloaks);
        options.addOption(numTreasureMaps);
        options.addOption(numCowards);
        options.addOption(numPotions);
        options.addOption(numRooms);
        options.addOption(numSeekers);
        options.addOption(numTreasures);
        options.addOption(numWeapons);
        options.addOption(addGraphicalView);
        options.addOption(humanPlayers);

        return options;
    }

    private IGameObserver createMazeObserver(ObservablePolymorphia game) {
        return MazeObserver.getNewBuilder(game, "Complex maze")
                .useSmartLayoutStrategy()
                .useImageRooms()
                .setDelayInSecondsAfterUpdate(5)
                .setRoomDimension(150)
                .setWidth(2400)
                .setHeight(1200)
                .build();
    }
}
