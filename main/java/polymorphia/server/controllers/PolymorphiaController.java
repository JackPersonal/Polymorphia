package polymorphia.server.controllers;

import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import polymorphia.CommandExecutor;
import polymorphia.ObservablePolymorphia;
import polymorphia.artifacts.ArtifactFactory;
import polymorphia.characters.Character;
import polymorphia.characters.CharacterFactory;
import polymorphia.commands.ICommand;
import polymorphia.maze.Maze;
import polymorphia.maze.Room;
import polymorphia.maze.RoomFactory;
import polymorphia.persistence.GameStateFileStore;
import polymorphia.persistence.GameStateStore;
import polymorphia.strategy.BaseStrategy;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class PolymorphiaController {
    /**
     * Same idea as {@code GameConfigurator}: at least this many connections per
     * room when possible.
     */
    private static Logger logger = org.slf4j.LoggerFactory.getLogger(CommandExecutor.class);

    private static final int MINIMUM_ROOM_CONNECTIONS = 3;

    private final Map<String, ObservablePolymorphia> games = new ConcurrentHashMap<>();
    private final Random random = new Random();
    private final RoomFactory roomFactory = new RoomFactory();
    private final CharacterFactory characterFactory = new CharacterFactory();
    private final ArtifactFactory artifactFactory = new ArtifactFactory();

    private GameStateStore gameStateStore = new GameStateFileStore();

    public PolymorphiaController() {
        loadSavedGames();
    }

    private void loadSavedGames() {
        List<ObservablePolymorphia> savedGames = gameStateStore.loadAllGames();
        for (ObservablePolymorphia game : savedGames) {
            games.put(game.getName(), game);
        }
    }

    public List<String> getGameNames() {
        return games.keySet().stream().sorted().toList();
    }

    @GetMapping("/api/games")
    public ResponseEntity<?> getGames() {
        return new ResponseEntity<>(getGameNames(), HttpStatus.OK); // return 200 (OK status)
    }

    @GetMapping("/api/game/{gameId}")
    public ResponseEntity<?> getGame(@PathVariable(required = false) String gameId) {
        if (!games.containsKey(gameId)) {
            return new ResponseEntity<>("Game not found!", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(new PolymorphiaJsonAdaptor(games.get(gameId)), HttpStatus.OK);
        }
    }

    /// make maze, name game, create game instance to make observable, register
    /// game, return it
    @PostMapping("/api/game/create")
    public ResponseEntity<?> createGame(@Validated @RequestBody PolymorphiaParameters params) {

        Maze maze = createMaze(params); // build maze via record

        String requestedName = params.name();
        String gameName = (requestedName == null || requestedName.isBlank())
                ? "Polymorphia_" + random.nextInt(1_000_000)
                : requestedName; // get random name if none given, goal is to avoid non-unique names (hence the million)

        // Wrap the maze in the observable game
        ObservablePolymorphia game = new ObservablePolymorphia(gameName, maze);

        // Register so GET routes can find game
        games.put(game.getName(), game);

        // PolymorphiaJsonAdaptor converts poly into parsable json
        return new ResponseEntity<>(new PolymorphiaJsonAdaptor(game), HttpStatus.CREATED); // ret 201 (created)
    }

    @PutMapping("/api/game/{gameId}/save")
    public ResponseEntity<?> saveGame(@PathVariable String gameId) {
        // only save game if id is valid
        if (!games.containsKey(gameId)) {
            return new ResponseEntity<>("Game not found!", HttpStatus.NOT_FOUND);
        }
        gameStateStore.save(games.get(gameId));
        return new ResponseEntity<>("Game Saved!", HttpStatus.OK);
    }

    @PutMapping("/api/game/{gameId}/playTurn")
    public ResponseEntity<?> playTurn(@PathVariable String gameId) {
        if (!games.containsKey(gameId)) {
            return new ResponseEntity<>("Game not found!", HttpStatus.NOT_FOUND);
        }
        ObservablePolymorphia game = games.get(gameId);
        game.playTurn();
        game.waitUntilIdle();
        return new ResponseEntity<>(new PolymorphiaJsonAdaptor(game), HttpStatus.OK);
    }

    @GetMapping("/api/game/{gameId}/{userName}/availableCommands")
    public ResponseEntity<?> getAvailableCommands(@PathVariable String gameId, @PathVariable String userName) {
        // pseudocode: find character + their room;
        // BaseStrategy.getPossibleActions(...); map each to uiLabel(); return list
        if(!games.containsKey(gameId)){
            return new ResponseEntity<>("Game not found!", HttpStatus.NOT_FOUND);
        }

        ObservablePolymorphia game = games.get(gameId); //need to access correct game

        Optional<Character> character = game.getCharacter(userName); //need to access correct character
        if(character.isEmpty()){
            return new ResponseEntity<>("Player not found: " + userName, HttpStatus.NOT_FOUND);
        }

        //need to know the room the character's in so we know what commadns we can run
        Optional<Room> room = game.getRoomWithCharacter(character.get());
        if(room.isEmpty()){
            return new ResponseEntity<>("Room not found for player: " + userName, HttpStatus.NOT_FOUND);
        }

        List<String> cmd_labels = BaseStrategy.getPossibleActions(character.get(), room.get())
                .stream() //get it in a form we can mess with
                .map(ICommand::uiLabel) //map each ICommand object to its string uiLabel
                .toList(); //collects stream back into a list<string>

        return new ResponseEntity<>(cmd_labels, HttpStatus.OK);
    }

    @PostMapping("/api/game/{gameId}/{userName}/submitCommand/{commandString:.+}") //:.+ treats the whole text (like FIGHT: ... instead of FIGHT) as the var
    public ResponseEntity<?> submitCommand(@PathVariable String gameId,
            @PathVariable String userName, @PathVariable String commandString) {
        // pseudocode: decode commandString; match one of possible ICommands by
        // uiLabel(); game.submitCommand(cmd); waitUntilIdle(); return 201 + json
        // adaptor (or errors)
//        logger.info("submitCommand runs");
        if(!games.containsKey(gameId)){
            return new ResponseEntity<>("Game not found!", HttpStatus.NOT_FOUND);
        }

        ObservablePolymorphia game = games.get(gameId);

        Optional<Character> character = game.getCharacter(userName);
        if(character.isEmpty()){
            return new ResponseEntity<>("Player not found: " + userName, HttpStatus.NOT_FOUND);
        }

        Optional<Room> room = game.getRoomWithCharacter(character.get());
        if(room.isEmpty()){
            return new ResponseEntity<>("Room not found for player: " + userName, HttpStatus.NOT_FOUND);
        }

        // The commandString arrives URL-encoded from the browser (e.g. "FIGHT%3A+Troll")
        // URLDecoder converts it back to the original label (e.g. "FIGHT: Troll")
        String decoded = java.net.URLDecoder.decode(commandString, java.nio.charset.StandardCharsets.UTF_8);

        Optional<ICommand> matched = BaseStrategy.getPossibleActions(character.get(), room.get())
                .stream()
                .filter(cmd->cmd.uiLabel().equals(decoded)) //dont need to map every ICommand object, just access the one we're submitting
                .findFirst(); //should just be one matching command, but handles case if there are multiple or 0 matches by wrapping it in Optional<>
        if(matched.isEmpty()){
            return new ResponseEntity<>("Unknown command: " + decoded, HttpStatus.BAD_REQUEST);
        }

        // Hand the matched command off to the game's command queue
        game.submitCommand(matched.get());
        // Block this thread until the command executor has finished processing
        game.waitUntilIdle();

        return new ResponseEntity<>(new PolymorphiaJsonAdaptor(game), HttpStatus.CREATED);
//        return new ResponseEntity<>("Command submitted", HttpStatus.CREATED);
    }

    // USING PARAMS, CREATE AND RETURN MAZE USING PREMADE COMMANDS
    private Maze createMaze(PolymorphiaParameters params) {
        Maze.Builder builder = Maze.getNewBuilder(roomFactory);

        builder.createConnectedRooms(MINIMUM_ROOM_CONNECTIONS, params.numRooms()); // how records are accessed(.varName())

        builder.addArtifacts(artifactFactory.createArmoredSuits(params.numArmor()));

        builder.addCharacters(characterFactory.createCharacters(
                params.numKnights(),
                params.numCowards(),
                params.numGluttons(),
                params.numCreatures(),
                params.numDemons(),
                0));

        builder.addArtifacts(artifactFactory.createFoodItems(params.numFood()));
        
        // make valid players (api players)
        if (params.playerName() != null && !params.playerName().isBlank()) {
            builder.add(characterFactory.createAPIPlayer(params.playerName()));
        }

        return builder.build();
    }

    public void addGame(ObservablePolymorphia observablePolymorphia) {
        games.put(observablePolymorphia.getName(), observablePolymorphia);
    }
}