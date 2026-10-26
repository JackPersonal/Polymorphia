package polymorphia;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DieTest {

    @Test
    void testRoll() {
        Die die = new RandomDie(6);
        int result = die.roll();
        assertTrue(result >= 1 && result <= 6, "Die roll should be between 1 and 6");
    }

    @Test
    void testFixedDie() {
        Die fixedDie = new FixedDie(4);
        assertEquals(4, fixedDie.roll());
    }
}