package polymorphia.persistence.nosqldb;

import polymorphia.ObservablePolymorphia;
import polymorphia.artifacts.ArtifactFactory;
import polymorphia.artifacts.ArtifactType;
import polymorphia.artifacts.IArtifact;
import polymorphia.characters.Character;
import polymorphia.characters.CharacterFactory;
import polymorphia.characters.CharacterType;
import polymorphia.maze.Maze;
import polymorphia.maze.Room;
import polymorphia.maze.RoomFactory;
import polymorphia.strategy.IStrategy;
import polymorphia.strategy.StrategyFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameSnapshotMapper {
    private final RoomFactory roomFactory = new RoomFactory();
    private final CharacterFactory characterFactory = new CharacterFactory();
    private final ArtifactFactory artifactFactory = new ArtifactFactory();
    private final StrategyFactory strategyFactory = new StrategyFactory();

    public GameSnapshot toSnapshot(ObservablePolymorphia game) {
        GameSnapshot snapshot = new GameSnapshot();
        snapshot.setId(game.getName());
        snapshot.setName(game.getName());
        snapshot.setTurnCount(game.getTurnCount());
        snapshot.setTreasureFound(game.isTreasureFound());
        // TODO: fix with factory or something
//        snapshot.setMaze(toMazeSnapshot(game.getMaze()));
        return snapshot;
    }

    public MazeSnapshot toMazeSnapshot(Maze maze) {
        List<RoomSnapshot> roomSnapshots = new ArrayList<>();
        for (Room room : maze.getRooms()) {
            roomSnapshots.add(toRoomSnapshot(room));
        }
        return new MazeSnapshot(roomSnapshots);
    }

    private RoomSnapshot toRoomSnapshot(Room room) {
        RoomSnapshot snapshot = new RoomSnapshot();
        snapshot.setName(room.getName());

        snapshot.setNeighborNames(
                room.getNeighbors().stream()
                        .map(Room::getName)
                        .toList()
        );

        List<CharacterSnapshot> characters = new ArrayList<>();
        for (Character character : room.getLivingCharacters()) {
            characters.add(toCharacterSnapshot(character));
        }
        snapshot.setCharacters(characters);

        List<ArtifactSnapshot> artifacts = new ArrayList<>();
        for (IArtifact artifact : room.getArtifacts()) {
            artifacts.add(toArtifactSnapshot(artifact));
        }
        snapshot.setArtifacts(artifacts);

        return snapshot;
    }

    private CharacterSnapshot toCharacterSnapshot(Character character) {
        CharacterSnapshot snapshot = new CharacterSnapshot();
        snapshot.setKind(character.getClass().getSimpleName());
        snapshot.setName(character.getBaseName());
        snapshot.setHealth(character.getHealth());
        snapshot.setStrategy(toStrategySnapshot(character));
        return snapshot;
    }

    private StrategySnapshot toStrategySnapshot(Character character) {
        StrategySnapshot snapshot = new StrategySnapshot();
        // TODO: fix with factory or something
//        IStrategy strategy = character.getStrategy();
//        snapshot.setKind(strategy == null ? null : strategy.getClass().getSimpleName());
        return snapshot;
    }

    private ArtifactSnapshot toArtifactSnapshot(IArtifact artifact) {
        ArtifactSnapshot snapshot = new ArtifactSnapshot();
        snapshot.setKind(artifact.getClass().getSimpleName());
        snapshot.setName(artifact.getName());
        snapshot.setHealthValue(artifact.getHealthValue());
        snapshot.setStrength(artifact.getStrengthValue());
        snapshot.setMovingCost(artifact.getMovingCost());
        snapshot.setAttackValue(artifact.getAttackValue());
        return snapshot;
    }

    public ObservablePolymorphia fromSnapshot(GameSnapshot snapshot) {
        Maze maze = fromMazeSnapshot(snapshot.getMaze());
        ObservablePolymorphia game = new ObservablePolymorphia(snapshot.getName(), maze);
        restoreGameState(game, snapshot);
        return game;
    }

    private void restoreGameState(ObservablePolymorphia game, GameSnapshot snapshot) {

    }

    public Maze fromMazeSnapshot(MazeSnapshot snapshot) {
        Map<String, Room> roomByName = new HashMap<>();
        List<Room> rooms = new ArrayList<>();

        for (RoomSnapshot roomSnapshot : snapshot.getRooms()) {
            Room room = roomFactory.createRoom(roomSnapshot.getName());
            rooms.add(room);
            roomByName.put(room.getName(), room);
        }

        for (RoomSnapshot roomSnapshot : snapshot.getRooms()) {
            Room room = roomByName.get(roomSnapshot.getName());

            for (String neighborName : roomSnapshot.getNeighborNames()) {
                Room neighbor = roomByName.get(neighborName);
                if (neighbor != null) {
                    // TODO: fix without breaking encapsulation, maybe with a factory or something
//                    room.addNeighbor(neighbor, false);
                }
            }

            for (IArtifact artifact : restoreArtifacts(roomSnapshot.getArtifacts())) {
                room.add(artifact);
            }

            for (Character character : restoreCharacters(roomSnapshot.getCharacters())) {
                room.add(character);
            }
        }

        return new Maze(rooms);
    }

    private List<IArtifact> restoreArtifacts(List<ArtifactSnapshot> snapshots) {
        List<IArtifact> artifacts = new ArrayList<>();
        for (ArtifactSnapshot snapshot : snapshots) {
            artifacts.add(fromArtifactSnapshot(snapshot));
        }
        return artifacts;
    }

    private List<Character> restoreCharacters(List<CharacterSnapshot> snapshots) {
        List<Character> characters = new ArrayList<>();
        for (CharacterSnapshot snapshot : snapshots) {
            characters.add(fromCharacterSnapshot(snapshot));
        }
        return characters;
    }

    private Character fromCharacterSnapshot(CharacterSnapshot snapshot) {
        Character character = characterFactory.createCharacter(
                toCharacterType(snapshot),
                snapshot.getName(),
                snapshot.getHealth(),
                toStrategy(snapshot.getStrategy())
        );

        return character;
    }

    private CharacterType toCharacterType(CharacterSnapshot snapshot) {
        return switch (snapshot.getKind()) {
            case "Adventurer" -> CharacterType.ADVENTURER;
            case "Creature" -> CharacterType.CREATURE;
            default -> throw new IllegalArgumentException("Unsupported character kind: " + snapshot.getKind());
        };
    }

    private IStrategy toStrategy(StrategySnapshot snapshot) {
        if (snapshot == null || snapshot.getKind() == null) {
            return strategyFactory.newSleepStrategy();
        }

        return switch (snapshot.getKind()) {
            case "FighterStrategy" -> strategyFactory.newFighterStrategy();
            case "CowardStrategy" -> strategyFactory.newCowardStrategy();
            case "GluttonStrategy" -> strategyFactory.newGluttonStrategy();
            case "GuardStrategy" -> strategyFactory.newGuardStrategy();
            case "HumanStrategy" -> strategyFactory.newHumanStrategy();
            case "RandomStrategy" -> strategyFactory.newRandomStrategy();
            case "SeekerStrategy" -> strategyFactory.newSeekerStrategy();
            case "CreatureStrategy" -> strategyFactory.newCreatureStrategy();
            case "SleepStrategy" -> strategyFactory.newSleepStrategy();
            default -> throw new IllegalArgumentException("Unsupported strategy kind: " + snapshot.getKind());
        };
    }

    private IArtifact fromArtifactSnapshot(ArtifactSnapshot snapshot) {
        ArtifactType type = ArtifactType.valueOf(snapshot.getKind());
        return artifactFactory.create(
                type,
                snapshot.getName(),
                snapshot.getHealthValue(),
                snapshot.getStrength(),
                snapshot.getMovingCost(),
                snapshot.getAttackValue()
        );
    }
}