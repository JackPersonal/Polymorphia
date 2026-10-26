package polymorphia.characters;

import polymorphia.Die;
import polymorphia.RandomDie;
import polymorphia.strategy.CreatureStrategy;
import polymorphia.strategy.IStrategy;

import java.util.Optional;

public class Creature extends Character {
    static final Double DEFAULT_INITIAL_HEALTH = 3.0;

    public Creature(String name) {
        this(name, DEFAULT_INITIAL_HEALTH);
    }

    public Creature(String name, Double initialHealth) {
        this(name, initialHealth, RandomDie.sixSided());
    }

    public Creature(String name, Double initialHealth, Die die) {
        super(name, initialHealth, die, new CreatureStrategy());
    }

    public Creature(String name, Double initialHealth, Die die, IStrategy strategy) {
        super(name, initialHealth, die, strategy);
    }

    @Override
    public Boolean isCreature() {
        return true;
    }

}
