package polymorphia.persistence.nosqldb;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import polymorphia.ObservablePolymorphia;
import polymorphia.persistence.GameStateStore;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;


// TODO: Put this in the service startup code
//MongoClient mongoClient = MongoClients.create(connectionString);
//
//        CodecRegistry pojoCodecRegistry = fromRegistries(
//                MongoClientSettings.getDefaultCodecRegistry(),
//                fromProviders(PojoCodecProvider.builder().automatic(true).build())
//        );
//
//        MongoDatabase database = mongoClient.getDatabase("polymorphia")
//                .withCodecRegistry(pojoCodecRegistry);
//
//        MongoCollection<GameSnapshot> collection = database.getCollection("gameStates", GameSnapshot.class);

public class MongoGameStateStore implements GameStateStore {
    private final MongoCollection<GameSnapshot> collection;
    private final GameSnapshotMapper mapper;

    public MongoGameStateStore(MongoCollection<GameSnapshot> collection, GameSnapshotMapper mapper) {
        this.collection = collection;
        this.mapper = mapper;
    }

    @Override
    public String save(ObservablePolymorphia polymorphia) {
        GameSnapshot snapshot = mapper.toSnapshot(polymorphia);
        ReplaceOptions replaceOptions = new ReplaceOptions().upsert(true);
        collection.replaceOne(eq("_id", snapshot.getId()), snapshot, replaceOptions);
        return snapshot.getId();
    }

    @Override
    public ObservablePolymorphia load(String key) {
        GameSnapshot snapshot = collection.find(eq("_id", key)).first();
        if (snapshot == null) {
            throw new IllegalArgumentException("No saved game found for key: " + key);
        }
        return mapper.fromSnapshot(snapshot);
    }

    @Override
    public List<ObservablePolymorphia> loadAllGames() {
        return List.of();
    }
}