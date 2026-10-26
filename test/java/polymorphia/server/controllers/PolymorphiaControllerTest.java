package polymorphia.server.controllers;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import polymorphia.ObservablePolymorphia;
import polymorphia.artifacts.ArtifactFactory;
import polymorphia.characters.CharacterFactory;
import polymorphia.maze.Maze;
import polymorphia.maze.RoomFactory;
import polymorphia.server.PolymorphiaServerApplication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {PolymorphiaServerApplication.class, PolymorphiaController.class})
class PolymorphiaControllerTest {
    CharacterFactory characterFactory = new CharacterFactory();
    RoomFactory roomFactory = new RoomFactory();
    ArtifactFactory artifactFactory = new ArtifactFactory();

    PolymorphiaController polymorphiaController;

    @Autowired
    public PolymorphiaControllerTest(PolymorphiaController polymorphiaController) {
        this.polymorphiaController = polymorphiaController;
    }

    @Test
    public void contextLoads() {
    }

    @Test
    void createGame() {
        int originalGameCount = polymorphiaController.getGameNames().size();

        String playerName = "Professor";
        PolymorphiaParameters polymorphiaParameters = new PolymorphiaParameters(null, playerName,
                2, 2, 7, 1,
                2, 2, 2, 10);
        ResponseEntity<?> response = polymorphiaController.createGame(polymorphiaParameters);
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());

        PolymorphiaJsonAdaptor jsonAdaptor = (PolymorphiaJsonAdaptor) response.getBody();
        assert jsonAdaptor != null;
        assertFalse(jsonAdaptor.gameOver);
        System.out.println(polymorphiaController.getGameNames());
        assertTrue(polymorphiaController.getGameNames().size() == originalGameCount + 1);
    }

    @Test
    void getGame() {
        PolymorphiaParameters polymorphiaParameters = new PolymorphiaParameters(null, null,
                2, 4,2, 1, 1,
                2, 10, 2);
        ResponseEntity<?> response = polymorphiaController.createGame(polymorphiaParameters);
        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
        String gameName = polymorphiaController.getGameNames().getFirst();

        response = polymorphiaController.getGame(gameName);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        PolymorphiaJsonAdaptor jsonAdaptor = (PolymorphiaJsonAdaptor) response.getBody();
        assert jsonAdaptor != null;
    }

    @Test
    void getGames() {
        ResponseEntity<?> response = polymorphiaController.getGames();
        assertTrue(response.getStatusCode().is2xxSuccessful());

        System.out.println(response.getBody());
    }

    @Test
    void getAvailableCommands() {
        // Create game with twos room and one adventurer in it with
        // one creature and one of all artifacts in starting room
        ObservablePolymorphia game = new ObservablePolymorphia(createTwoRoomMazeWithCreatureAndAPIPlayerAndFood());
        polymorphiaController.addGame(game);
        ResponseEntity<?> response = polymorphiaController.getAvailableCommands(game.getName(), "Bilbo");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println(response.getBody());
        List choices = (List) response.getBody();
        assert choices != null;
        assertTrue(choices.contains("SLEEP:"));
        assertTrue(choices.contains("MOVE: Next"));
        assertTrue(choices.contains("EAT: Apple"));
        assertTrue(choices.contains("FIGHT: Ogre"));
    }

    @Test
    void testPostCommand() {
        // Create game with twos room and one adventurer in it with
        // one creature and one of all artifacts in starting room
        ObservablePolymorphia game = new ObservablePolymorphia(createTwoRoomMazeWithCreatureAndAPIPlayerAndFood());
        polymorphiaController.addGame(game);
        ResponseEntity<?> response = polymorphiaController.submitCommand(game.getName(), "Bilbo", "MOVE: Next");
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        System.out.println(response.getBody());
    }

    @Test
    void getGameNotFound() {
        ResponseEntity<?> response = polymorphiaController.getGame("not-a-real-game");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void saveGameNotFound() {
        ResponseEntity<?> response = polymorphiaController.saveGame("not-a-real-game");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void playTurnNotFound() {
        ResponseEntity<?> response = polymorphiaController.playTurn("not-a-real-game");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getAvailableCommandsNotFoundForPlayer() {
        ObservablePolymorphia game = new ObservablePolymorphia(createTwoRoomMazeWithCreatureAndAPIPlayerAndFood());
        polymorphiaController.addGame(game);

        ResponseEntity<?> response = polymorphiaController.getAvailableCommands(game.getName(), "Nope");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void submitCommandBadCommand() {
        ObservablePolymorphia game = new ObservablePolymorphia(createTwoRoomMazeWithCreatureAndAPIPlayerAndFood());
        polymorphiaController.addGame(game);

        ResponseEntity<?> response = polymorphiaController.submitCommand(game.getName(), "Bilbo", "NOT_A_REAL_COMMAND");
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createGameWithProvidedName() {
        String givenName = "MyNamedGame";
        PolymorphiaParameters params = new PolymorphiaParameters(givenName, null,
                2, 2, 2, 1, 1, 1, 2, 2);

        ResponseEntity<?> response = polymorphiaController.createGame(params);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        PolymorphiaJsonAdaptor body = (PolymorphiaJsonAdaptor) response.getBody();
        assertNotNull(body);
        assertEquals(givenName, body.name);
    }

    private Maze createTwoRoomMazeWithCreatureAndAPIPlayerAndFood() {
        return Maze.getNewBuilder(roomFactory)
                .createFullyConnectedRooms("Starting", "Next")
                .addToRoom("Starting", characterFactory.createAPIPlayer("Bilbo"))
                .addToRoom("Starting", characterFactory.createCreature("Ogre"))
                .addToRoom("Starting", artifactFactory.createFood("Apple"))
                .build();

    }
}
