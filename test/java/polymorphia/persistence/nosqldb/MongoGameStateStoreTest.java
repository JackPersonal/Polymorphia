package polymorphia.persistence.nosqldb;

import com.mongodb.client.MongoCollection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import polymorphia.ObservablePolymorphia;
import polymorphia.maze.Maze;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static polymorphia.ObservablePolymorphiaTest.createComplexMaze;

@Testcontainers
class MongoGameStateStoreIntegrationTest {
    private MongoStoreConfig mongoStoreConfig;

    @Container
    static MongoDBContainer mongo = new MongoDBContainer("mongo:7");

    @AfterEach
    void tearDown() {
        if (mongoStoreConfig != null) {
            mongoStoreConfig.close();
        }
    }

    @Disabled
    void saveAndLoadGameRoundTrip() {
        mongoStoreConfig = new MongoStoreConfig(mongo.getConnectionString(), "polymorphia");
        MongoCollection<GameSnapshot> collection = mongoStoreConfig.gameStatesCollection("gameStates");

        GameSnapshotMapper mapper = new GameSnapshotMapper();

        MongoGameStateStore store = new MongoGameStateStore(collection, mapper);

        Maze maze = createComplexMaze();
        ObservablePolymorphia originalGame = new ObservablePolymorphia(maze);

        String key = store.save(originalGame);
        assertNotNull(key);

        ObservablePolymorphia restoredGame = store.load(key);

        assertNotNull(restoredGame);
        assertEquals(originalGame.getName(), restoredGame.getName());
        assertEquals(originalGame.getTurnCount(), restoredGame.getTurnCount());
        assertEquals(originalGame.isTreasureFound(), restoredGame.isTreasureFound());
    }
}