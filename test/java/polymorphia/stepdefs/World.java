package polymorphia.stepdefs;

import polymorphia.ObservablePolymorphia;
import polymorphia.TestObserver;
import polymorphia.artifacts.IArtifact;
import polymorphia.characters.Character;
import polymorphia.maze.Maze;
import polymorphia.maze.Room;
import polymorphia.maze.RoomFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class World {
    Map<String, Character> characters = new HashMap<>();
    Map<String, Double> initialCharactersHealth = new HashMap<>();
    ObservablePolymorphia game;
    RoomFactory roomFactory;
    Maze.Builder mazeBuilder;
    Map<String, IArtifact> artifacts = new HashMap<>();
    private Maze maze;
    TestObserver observer;

    public World(RoomFactory roomFactory, TestObserver observer) {
        this.roomFactory = roomFactory;
        this.mazeBuilder = Maze.getNewBuilder(roomFactory);
        this.observer = observer;
    }

    public void putArtifact(String artifactName, IArtifact artifact) {
        artifacts.put(artifactName, artifact);
    }

    public IArtifact getArtifact(String artifactName) {
        return artifacts.get(artifactName);
    }

    public Character getCharacter(String name) {
        Character coreCharacter = characters.get(name);
        Optional<Room> room = getMaze().getRoomWithCharacter(coreCharacter.getBaseName());
        Character wrappedCharacter = room.get().getCharacterWithName(name).get();
        return wrappedCharacter;
    }

    public Room getRoomWithCharacter(Character character) {
        return maze.getRoomWithCharacter(character.getBaseName()).get();
    }

    public Character putCharacter(String characterName, Character character) {
        characters.put(characterName, character);
        assert character != null;
        initialCharactersHealth.put(characterName, character.getHealth());
        return character;
    }

    public double getInitialCharacterHealth(String characterName) {
        return initialCharactersHealth.get(characterName);
    }

    public ObservablePolymorphia getGame() {
        if (game == null) {
            game = new ObservablePolymorphia(getMaze());
            game.attach(observer);
        }
        return game;
    }

    Maze getMaze() {
        if (maze == null) {
            maze = getMazeBuilder().build();
        }
        return maze;
    }

    public Maze.Builder getMazeBuilder() {
        return mazeBuilder;
    }

    public TestObserver getObserver() {
        return observer;
    }
}
