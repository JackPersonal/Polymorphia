package polymorphia.characters;

import org.junit.jupiter.api.Test;
import polymorphia.artifacts.ArtifactFactory;
import polymorphia.maze.Maze;
import polymorphia.maze.Room;
import polymorphia.maze.RoomFactory;

class GluttonTest {
    CharacterFactory characterFactory = new CharacterFactory();
    ArtifactFactory artifactFactory = new ArtifactFactory();
    RoomFactory roomFactory = new RoomFactory();

    @Test
    void testGluttonPreferenceForEatingOverFightingOrMoving() {
        Character character = characterFactory.createGlutton();

        Maze maze = Maze.getNewBuilder(roomFactory)
                .createFullyConnectedRooms("startRoom", "neighbor")
                .addToRoom("startRoom", character)
                .addToRoom("startRoom", characterFactory.createCreature())
                .addToRoom("startRoom", artifactFactory.createFood("burger"))
                .build();

        double initialCharacterHealth = character.getHealth();
        Room startRoom = maze.getRoom("startRoom");

        character.selectAction(startRoom).execute();
        assert (startRoom.contains(character));  // character chose to eat, instead of run away
        assert (character.getHealth() > initialCharacterHealth);    // because character ate food

        Room neighbor = maze.getRoom("neighbor");
        character.selectAction(startRoom).execute();
        assert (neighbor.contains(character));  // the character chose to run away rather than fight
    }
}