package polymorphia;

import java.io.Serializable;
import java.util.Random;

public class RandomDie implements Die, Serializable {
    static private final Random rand = new Random();
    private final int sides;

    public RandomDie(int sides) {
        this.sides = sides;
    }

    public static Die sixSided() {
        return new RandomDie(6);
    }

    public int roll() {
        return rand.nextInt(sides) + 1;
    }
}
