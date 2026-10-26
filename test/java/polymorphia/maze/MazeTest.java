package polymorphia.maze;

import ooad.mazeobserver.MazeObserver;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polymorphia.ObservablePolymorphia;
import polymorphia.Polymorphia;
import polymorphia.artifacts.ArtifactFactory;
import polymorphia.characters.Adventurer;
import polymorphia.characters.Character;
import polymorphia.characters.CharacterFactory;
import polymorphia.characters.Creature;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MazeTest {
    private static final Random random = new Random();
    private static final Logger logger = LoggerFactory.getLogger(MazeTest.class);
    private static final RoomFactory roomFactory = new RoomFactory();
    private static final ArtifactFactory artifactFactory = new ArtifactFactory();
    private static final CharacterFactory characterFactory = new CharacterFactory();


    @Test
    void testCreationOf2x2Grid() {
        Maze maze = createPopulated2x2Maze();
        assertEquals(4, maze.size());
        logger.info(maze.toString());
    }

    static Maze createPopulated2x2Maze() {
        Character bilbo = characterFactory.createKnight("Bilbo");
        Character fiona = characterFactory.createKnight("Fiona");
        Character ogre = characterFactory.createCreature("Ogre");
        List<Character> characters = List.of(bilbo, fiona, ogre);
        return Maze.getNewBuilder(new RoomFactory())
                .create2x2Grid()
                .addCharacters(characters)
                .build();
    }

    @Test
    void testCreationOf3x3Grid() {
        Maze maze = createPopulated3x3Maze();
        assertEquals(9, maze.size());
        logger.info(maze.toString());
    }

    static Maze createPopulated3x3Maze() {
        Character bilbo = characterFactory.createKnight("Bilbo");
        Character fiona = characterFactory.createKnight("Fiona");
        Character ogre = characterFactory.createCreature("Ogre");
        Character dragon = characterFactory.createCreature("Dragon");
        Character orc = characterFactory.createCreature("Orc");
        Character medusa = characterFactory.createCreature("Medusa");
        Character troll = characterFactory.createCreature("Troll");
        List<Character> characters = List.of(bilbo, fiona, ogre, dragon, orc, medusa, troll);
        return Maze.getNewBuilder(new RoomFactory())
                .create3x3Grid()
                .addCharacters(characters)
                .build();
    }

    @Test
    void testHasLivingAdventurers() {
        Character bilbo = characterFactory.createKnight("Bilbo");
        Character fiona = characterFactory.createKnight("Fiona");
        Maze maze = Maze.getNewBuilder(roomFactory)
                .create2x2Grid()
                .addCharacters(List.of(bilbo, fiona))
                .build();

        assertTrue(maze.hasLivingAdventurers());
        assertEquals(2, maze.getLivingAdventurers().size());
    }

    @Test
    void testHasLivingCreatures() {
        List<Character> creatures = List.of(
                characterFactory.createCreature("Ogre"),
                characterFactory.createCreature("Dragon"));
        Maze maze = Maze.getNewBuilder(roomFactory)
                .create2x2Grid()
                .addCharacters(creatures)
                .add(characterFactory.createKnight("Game Requires One"))
                .build();
        assertTrue(maze.hasLivingCreatures());
        assertEquals(2, maze.getLivingCreatures().size());
    }

    @Test
    void testToString() {
        List<Character> characters = List.of(
                characterFactory.createKnight("Bilbo"),
                characterFactory.createCreature("Ogre"));
        Maze maze = Maze.getNewBuilder(roomFactory)
                .create2x2Grid()
                .addCharacters(characters)
                .add(characterFactory.createKnight("Game Requires One"))
                .build();
        String mazeString = maze.toString();
        logger.info(mazeString);

        assertTrue(mazeString.contains("Bilbo"));
        assertTrue(mazeString.contains("Ogre"));
    }

    @Test
    void testSequentialDistribution() {
        Maze maze = Maze.getNewBuilder(roomFactory)
                .createFullyConnectedRooms(10)
                .add(characterFactory.createKnight("Game Requires One"))
                .distributeSequentially()
                .addArtifacts(artifactFactory.createFoodItems(10))
                .build();

        logger.info(maze.toString());
        // Since we added 10 food items into a 10-room maze, with sequential distribution, each room should have some food.
        for (Room room : maze.getRooms()) {
            assertTrue(room.hasFood());
        }
    }

    @Test
    void testRandomDistribution() {
        Maze maze = Maze.getNewBuilder(roomFactory)
                .createFullyConnectedRooms(100)
                .add(characterFactory.createKnight("Game Requires One"))
                .distributeRandomly()
                .addArtifacts(artifactFactory.createFoodItems(100))
                .build();

        logger.info(maze.toString());
        // Since we added a hundred food items into a hundred-room maze, with random distribution,
        // it's likely (not assured) that one room will not have any food in it.
        assertTrue(maze.getRooms().stream().anyMatch(room -> !room.hasFood()));
    }

    @Test
    void testComplexMazeShape() {
        int gridWidth = 3;
        int gridHeight = 2;
        String gridRoomNamePrefix = "Grid";
        Maze maze = Maze.getNewBuilder(roomFactory)
                .useBidirectionalConnections()
                .createStraightLineRooms("Rivendell", "Mordor", "BagEnd")
                .createRingOfConnectedRooms("Crystal", "Lava Pool", "Fangorn Forest")
                .addNeighborToRoom("BagEnd", "Crystal")
                .createFullyConnectedRooms("Room 1", "Room 2", "Room 3", "Room 4")
                .addNeighborToRoom("Room 1", "Fangorn Forest")
                .createAGridOfRooms(gridWidth, gridHeight, gridRoomNamePrefix)
                .addNeighborToRoom("Room 4", gridRoomNamePrefix + "0-0")
                .addCharacters(characterFactory.createCharacters(8))
                .addArtifacts(artifactFactory.createFoodItems(8))
                .addToSameRoom(characterFactory.createGuard(), artifactFactory.createTreasure())
                .build();

        Polymorphia game = new Polymorphia(maze);
        game.play();

        logger.info(maze.toString());
    }

    @Disabled
    void testComplexMazeShapeWithObserver() throws InterruptedException {
        int gridWidth = 3;
        int gridHeight = 2;
        String gridRoomNamePrefix = "Grid";
        Maze maze = Maze.getNewBuilder(roomFactory)
                .createStraightLineRooms("S:Rivendell", "S:Mordor", "S:BagEnd")
                .createRingOfConnectedRooms("R:Crystal", "R:Lava Pool", "R:Fangorn Forest")
                .addNeighborToRoom("S:BagEnd", "R:Crystal")
                .createFullyConnectedRooms("FC 1", "FC 2", "FC 3", "FC 4")
                .addNeighborToRoom("FC 1", "R:Fangorn Forest")
                .createAGridOfRooms(gridWidth, gridHeight, gridRoomNamePrefix)
                .addNeighborToRoom("FC 4", gridRoomNamePrefix + "0-0")
                .addCharacters(characterFactory.createCharacters(12))
                .addArtifacts(artifactFactory.createFoodItems(8))
                .addToSameRoom(characterFactory.createGuard(), artifactFactory.createTreasure())
                .build();

        ObservablePolymorphia game = new ObservablePolymorphia(maze);

        MazeObserver mazeObserver = MazeObserver.getNewBuilder(game, "Complex maze")
                .useSmartLayoutStrategy()
                .useImageRooms()
                .setDelayInSecondsAfterUpdate(5)
                .setRoomDimension(150)
                .setWidth(3000)
                .setHeight(1500)
                .build();
        game.attach(mazeObserver);

        game.play();

        Thread.currentThread().sleep(5000);
        logger.info(maze.toString());
    }

}
