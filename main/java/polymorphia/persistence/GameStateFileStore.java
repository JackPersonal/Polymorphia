package polymorphia.persistence;

import org.slf4j.Logger;
import polymorphia.ObservablePolymorphia;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameStateFileStore implements GameStateStore {
    static final Logger logger = org.slf4j.LoggerFactory.getLogger(GameStateFileStore.class);
    static final String GAME_STORE_DIRECTORY = "games/";
    static final String GAME_FILE_EXTENSION = ".dat";

    @Override
    public String save(ObservablePolymorphia polymorphia) {
        String filePath = GAME_STORE_DIRECTORY + polymorphia.getName() + GAME_FILE_EXTENSION;
        saveToFile(filePath, polymorphia);
        logger.info("Game state saved to " + filePath);
        return filePath;
    }

    private void saveToFile(String filePath, ObservablePolymorphia polymorphia) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(polymorphia);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save objects", e);
        }
    }

    @SuppressWarnings("unchecked")
    public ObservablePolymorphia load(String filePath) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            return (ObservablePolymorphia) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load objects", e);
        }
    }

    @Override
    public List<ObservablePolymorphia> loadAllGames() {
        List<ObservablePolymorphia> games = new ArrayList<>();
        File directory = new File(GAME_STORE_DIRECTORY);
        if (!directory.exists()) {
            logger.warn("Game store directory does not exist: " + GAME_STORE_DIRECTORY);
            return games;
        }
        File[] files = directory.listFiles((dir, name) -> name.endsWith(GAME_FILE_EXTENSION));
        if (files == null) {
            logger.warn("Failed to list files in game store directory: " + GAME_STORE_DIRECTORY);
            return games;
        }
        for (File file : files) {
            try {
                ObservablePolymorphia game = load(file.getPath());
                games.add(game);
                logger.info("Loaded game from " + file.getPath());
            } catch (RuntimeException e) {
                logger.error("Failed to load game from " + file.getPath(), e);
            }
        }
        return games;
    }
}

