package polymorphia.characters;

import org.junit.jupiter.api.Test;
import polymorphia.artifacts.ArtifactFactory;
import polymorphia.maze.Maze;
import polymorphia.maze.Room;
import polymorphia.maze.RoomFactory;

class CowardTest {
    CharacterFactory characterFactory = new CharacterFactory();
    ArtifactFactory artifactFactory = new ArtifactFactory();
    RoomFactory roomFactory = new RoomFactory();

    @Test
    void testCowardMovement() {
        Character character = characterFactory.createCoward();

        Maze maze = Maze.getNewBuilder(roomFactory)
                .createFullyConnectedRooms("startRoom", "neighbor")
                .addToRoom("startRoom", character)
                .addToRoom("startRoom", characterFactory.createCreature())
                .addToRoom("startRoom", artifactFactory.createFood("burger"))
                .build();

        Room startRoom = maze.getRoom("startRoom");
        Room neighbor = maze.getRoom("neighbor");
        character.selectAction(startRoom).execute();
        assert (neighbor.contains(character));  // character chose not to eat, but to run away

        neighbor.add(artifactFactory.createFood("burger"));

        double initialCharacterHealth = character.getHealth();
        character.selectAction(neighbor).execute();
        assert (neighbor.contains(character));  // the character chose to eat and not move
        assert (initialCharacterHealth < character.getHealth());    // because character ate food
    }

}