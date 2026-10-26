package polymorphia.persistence;

import ooad.mazeobserver.MazeObserver;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import polymorphia.ObservablePolymorphia;
import polymorphia.maze.Maze;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static polymorphia.ObservablePolymorphiaTest.createComplexMaze;
import static polymorphia.ObservablePolymorphiaTest.createMazeObserver;

class GameStateFileStoreTest {

    @Test
    void testSavingAndRestoringPolymorphia() {
        Maze maze = createComplexMaze();
        ObservablePolymorphia polymorphia = new ObservablePolymorphia(maze);
        GameStateStore gameStateFileStore = new GameStateFileStore();
        String filename = gameStateFileStore.save(polymorphia);

        ObservablePolymorphia restoredGame = gameStateFileStore.load(filename);

        assertEquals(polymorphia.getTurnCount(), restoredGame.getTurnCount());
    }

    @Disabled
    void testSavingGameInMiddleAndRestarting() {
        Maze maze = createComplexMaze();
        ObservablePolymorphia game = new ObservablePolymorphia(maze);
        MazeObserver mazeObserver = createMazeObserver(game);
        game.attach(mazeObserver);

        // Play some turns
        for (int turnNum = 0; turnNum < 2; turnNum++) {
            game.playTurn();
        }

        // Stop game, detach observer, save it
        game.detach(mazeObserver);
        GameStateStore gameStateFileStore = new GameStateFileStore();
        String filename = gameStateFileStore.save(game);

        ObservablePolymorphia restartedGame = gameStateFileStore.load(filename);
        MazeObserver newMazeObserver = createMazeObserver(restartedGame);
        restartedGame.attach(newMazeObserver);
        restartedGame.play();

        assertTrue(restartedGame.isOver());
        restartedGame.detach(newMazeObserver);
    }
}