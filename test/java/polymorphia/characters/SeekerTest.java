package polymorphia.characters;

import org.junit.jupiter.api.Test;
import polymorphia.artifacts.ArtifactFactory;
import polymorphia.maze.Maze;
import polymorphia.maze.Room;
import polymorphia.maze.RoomFactory;

public class SeekerTest {
    CharacterFactory characterFactory = new CharacterFactory();
    ArtifactFactory artifactFactory = new ArtifactFactory();
    RoomFactory roomFactory = new RoomFactory();

    @Test
    public void testSeekerMovement() {
        Character character = characterFactory.createSeeker();

        Maze maze = Maze.getNewBuilder(roomFactory)
                .createFullyConnectedRooms("startRoom", "neighbor1", "neighbor2")
                .addToRoom("startRoom", character)
                .add(characterFactory.createCreature())
                .build();

        Room startRoom = maze.getRoom("startRoom");
        Room neighbor1 = maze.getRoom("neighbor1");
        Room neighbor2 = maze.getRoom("neighbor2");

        character.selectAction(startRoom).execute();
        assert (neighbor1.contains(character) || neighbor2.contains(character));

        // character will never visit the starting room before the other unvisited room
        if (neighbor1.contains(character)) {
            character.selectAction(neighbor1).execute();
            assert (neighbor2.contains(character));
        } else {
            character.selectAction(neighbor2).execute();
            assert (neighbor1.contains(character));
        }
    }

    @Test
    public void testSeekerIgnoresFood() {
        Character character = characterFactory.createSeeker();

        Maze maze = Maze.getNewBuilder(roomFactory)
                .createFullyConnectedRooms("startRoom", "neighbor1", "neighbor2")
                .addToRoom("startRoom", character)
                .add(characterFactory.createCreature())
                .add(artifactFactory.createFood("burger"))
                .build();

        Room startRoom = maze.getRoom("startRoom");
        Room neighbor1 = maze.getRoom("neighbor1");
        Room neighbor2 = maze.getRoom("neighbor2");

        character.selectAction(startRoom).execute();
        assert (neighbor1.contains(character) || neighbor2.contains(character));
    }

    @Test
    public void testSeekerFightsOnlyIfTreasureAndCreatureInRoom() {
        Character character = characterFactory.createSeeker();
        Character guard = characterFactory.createGuard();

        Maze maze = Maze.getNewBuilder(roomFactory)
                .createFullyConnectedRooms("startRoom", "neighbor")
                .addToRoom("startRoom", character)
                .addToRoom("startRoom", guard)
                .addToRoom("startRoom", artifactFactory.createFood("burger"))
                .addToRoom("startRoom", artifactFactory.createTreasure())
                .build();

        Room startRoom = maze.getRoom("startRoom");

        double initialSeekerHealth = character.getHealth();
        double initialGuardHealth = guard.getHealth();
        character.selectAction(startRoom).execute();

        // Seeker did not move
        assert (startRoom.contains(character));
        assert (initialSeekerHealth > character.getHealth());
        assert (initialGuardHealth > guard.getHealth());
    }
}
