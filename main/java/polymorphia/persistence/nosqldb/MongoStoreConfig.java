package polymorphia.persistence.nosqldb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoStoreConfig {
    private final MongoClient mongoClient;
    private final MongoDatabase database;

    public MongoStoreConfig(String connectionString, String databaseName) {
        this.mongoClient = MongoClients.create(connectionString);

        CodecRegistry pojoCodecRegistry = fromRegistries(
                com.mongodb.MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build())
        );

        this.database = mongoClient.getDatabase(databaseName)
                .withCodecRegistry(pojoCodecRegistry);
    }

    public MongoCollection<GameSnapshot> gameStatesCollection(String collectionName) {
        return database.getCollection(collectionName, GameSnapshot.class);
    }

    public void close() {
        mongoClient.close();
    }
}
