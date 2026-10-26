package polymorphia;

import ooad.alternatemazeobserver.AlternateGraphicalMazeObserver;
import ooad.gameobserver.intf.IGameObserver;
import ooad.mazeobserver.MazeObserver;
import org.junit.jupiter.api.Disabled;
import polymorphia.artifacts.ArtifactFactory;
import polymorphia.characters.CharacterFactory;
import polymorphia.maze.Maze;
import polymorphia.maze.RoomFactory;

import static java.lang.Thread.sleep;

public class ObservablePolymorphiaTest {
    private static final RoomFactory roomFactory = new RoomFactory();
    private static final ArtifactFactory artifactFactory = new ArtifactFactory();
    private static final CharacterFactory characterFactory = new CharacterFactory();

    @Disabled
    void testComplexMazeShapeWithObserver() throws InterruptedException {
        Maze maze = createComplexMaze();

        ObservablePolymorphia game = new ObservablePolymorphia(maze);

        MazeObserver mazeObserver = createMazeObserver(game);
        game.attach(mazeObserver);

        // Add alternate Maze Observers
        IGameObserver alternateGraphicalMazeObserver = new AlternateGraphicalMazeObserver(game);
        game.attach(alternateGraphicalMazeObserver);

        game.play();

        game.detach(mazeObserver);
        game.detach(alternateGraphicalMazeObserver);

        sleep(5000);
        System.out.println(maze.toString());
    }

    public static MazeObserver createMazeObserver(ObservablePolymorphia game) {
        return MazeObserver.getNewBuilder(game, "Complex maze")
                .useSmartLayoutStrategy()
                .useImageRooms()
                .setDelayInSecondsAfterUpdate(5)
                .setRoomDimension(150)
                .setWidth(2400)
                .setHeight(1200)
                .build();
    }

    public static Maze createComplexMaze() {
        int gridWidth = 3;
        int gridHeight = 2;
        String gridRoomNamePrefix = "Grid";
        return Maze.getNewBuilder(roomFactory)
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
    }
}