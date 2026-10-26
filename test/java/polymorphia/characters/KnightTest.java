package polymorphia.characters;

import org.junit.jupiter.api.Test;
import polymorphia.artifacts.ArtifactFactory;
import polymorphia.maze.Maze;
import polymorphia.maze.Room;
import polymorphia.maze.RoomFactory;

class KnightTest {
    CharacterFactory characterFactory = new CharacterFactory();
    ArtifactFactory artifactFactory = new ArtifactFactory();
    RoomFactory roomFactory = new RoomFactory();

    @Test
    void testKnightPreferenceForFightingOverEatingOrMoving() {
        Character character = characterFactory.createKnight();
        Maze maze = Maze.getNewBuilder(roomFactory)
                .createFullyConnectedRooms("startRoom", "neighbor")
                .addToRoom("startRoom", character)
                .addToRoom("startRoom", characterFactory.createCreature())
                .addToRoom("startRoom", artifactFactory.createFood("burger"))
                .build();

        Room startRoom = maze.getRoom("startRoom");
        double initialCharacterHealth = character.getHealth();

        character.selectAction(startRoom).execute();
        assert (startRoom.contains(character));  // character chose to fight, instead of run away or eat
        assert (initialCharacterHealth > character.getHealth());    // because character ate food
    }
}