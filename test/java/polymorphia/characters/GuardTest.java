package polymorphia.characters;

import org.junit.jupiter.api.Test;
import polymorphia.maze.Maze;
import polymorphia.maze.Room;
import polymorphia.maze.RoomFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GuardTest {
    CharacterFactory characterFactory = new CharacterFactory();
    RoomFactory roomFactory = new RoomFactory();

    @Test
    public void testConstructor() {
        Character guard = characterFactory.createGuard("Guard");
        assertEquals("Guard", guard.getName());
        assertEquals(CharacterFactory.DEFAULT_GUARD_HEALTH, guard.getHealth());
    }

    @Test
    void testGuardNotMoving() {
        Character guard = characterFactory.createGuard("Guard");

        Maze maze = Maze.getNewBuilder(roomFactory)
                .createFullyConnectedRooms("currentRoom", "newRoom")
                .addToRoom("currentRoom", guard)
                .addToRoom("newRoom", characterFactory.createKnight("Required For Game"))
                .build();

        Room currentRoom = maze.getRoom("currentRoom");

        // The guard should not move to the new room
        guard.selectAction(currentRoom).execute();

        assertTrue(currentRoom.contains(guard));
    }
}
