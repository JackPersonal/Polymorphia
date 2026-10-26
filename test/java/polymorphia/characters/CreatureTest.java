package polymorphia.characters;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreatureTest {
    CharacterFactory characterFactory = new CharacterFactory();

    @Test
    public void testConstructor() {
        Character hero = characterFactory.createCreature("Orc");
        assertEquals("Orc", hero.getName());
        assertEquals(CharacterFactory.DEFAULT_CREATURE_HEALTH, hero.getHealth());
    }
}