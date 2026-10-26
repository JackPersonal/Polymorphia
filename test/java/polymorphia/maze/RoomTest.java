package polymorphia.maze;

import org.junit.jupiter.api.Test;
import polymorphia.characters.CharacterFactory;
import polymorphia.characters.Character;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {
    CharacterFactory characterFactory = new CharacterFactory();

    @Test
    void getRandomNeighbor() {
        Room room = new Room("onlyRoom");
        Room neighbor = new Room("neighbor");
        room.addNeighbor(neighbor);

        assertEquals(room.getRandomNeighbor(), neighbor);
    }

    @Test
    void testGetRandomNeighborOnRoomWithNoNeighbors() {
        Room room = new Room("onlyRoom");

        assertNull(room.getRandomNeighbor());
    }

    @Test
    void testToString() {
        Room room = new Room("onlyRoom");
        room.add(characterFactory.createKnight("Bilbo"));
        room.add(characterFactory.createCreature("Ogre"));

        System.out.println(room);

        assertTrue(room.toString().contains("onlyRoom"));
        assertTrue(room.toString().contains("Bilbo"));
        assertTrue(room.toString().contains("Ogre"));
    }

    @Test
    void testGettingNotMe() {
        Room room = new Room("onlyRoom");
        Character bilbo = characterFactory.createKnight("Bilbo");
        Character ogre = characterFactory.createCreature("Ogre");
        room.add(bilbo);
        room.add(ogre);

        Optional<Character> notMe = room.getRandomCharacterNotMe(ogre);
        assertTrue(notMe.isPresent());
        assertEquals(notMe.get(), bilbo);
    }
}
