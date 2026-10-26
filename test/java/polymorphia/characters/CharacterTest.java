package polymorphia.characters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import polymorphia.maze.Maze;
import polymorphia.maze.Room;
import polymorphia.maze.RoomFactory;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterTest {
    static Logger logger = org.slf4j.LoggerFactory.getLogger(CharacterTest.class);

    CharacterFactory characterFactory = new CharacterFactory();
    Character joe;

    @BeforeEach
    void setUp() {
        joe = characterFactory.createKnight("Joe");
    }

    @Test
    void testToString() {
        System.out.println(joe);

        assertTrue(joe.toString().contains("Joe"));
    }

    @Test
    void isAlive() {
        assertTrue(joe.isAlive());
    }

    @Test
    void testLoseHealthAndDeath() {
        joe.loseHealth(2.0);
        assertEquals(CharacterFactory.DEFAULT_KNIGHT_HEALTH - 2.0, joe.getHealth());

        joe.loseHealth(joe.getHealth());
        logger.info(joe.toString());
        assertFalse(joe.isAlive());
    }

    @Test
    void testFightingMandatoryLossOfHalfAPoint() {
        Character ogre = characterFactory.createCreature("Ogre");
        double joeHealthBeforeFight = joe.getHealth();
        joe.fight(ogre);
        logger.info("After the fight, Joe's health is: " + joe.getHealth());
        assertTrue(joe.getHealth() <= joeHealthBeforeFight - Character.HEALTH_LOST_IN_FIGHT_REGARDLESS_OF_OUTCOME);
    }

    @Test
    void testMovingRoomLossOfQuarterPoint() {
        Maze maze = Maze.getNewBuilder(new RoomFactory())
                .createFullyConnectedRooms("currentRoom", "newRoom")
                .addToRoom("currentRoom", joe)
                .build();

        // Since nothing is in the current room with Joe, he should move to the new room
        Room currentRoom = maze.getRoom("currentRoom");
        joe.selectAction(currentRoom).execute();

        Room newRoom = maze.getRoom("newRoom");
        logger.info("After moving rooms, Joe's health is: " + joe.getHealth());

        // health lost in each turn is assessed by Polymorphia and not in play here
        assertEquals(CharacterFactory.DEFAULT_KNIGHT_HEALTH - Character.HEALTH_LOST_IN_MOVING_ROOMS, joe.getHealth());
        assertTrue(newRoom.contains(joe));
    }
}
