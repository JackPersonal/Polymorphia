package polymorphia.characters;

import polymorphia.RandomDie;
import polymorphia.strategy.StrategyFactory;

public class APIPlayer extends Adventurer {
    private static final StrategyFactory STRATEGY_FACTORY = new StrategyFactory();

    public APIPlayer(String name) { // constructor
        super(name, CharacterFactory.DEFAULT_KNIGHT_HEALTH, RandomDie.sixSided(),
                STRATEGY_FACTORY.newFighterStrategy());
    }

    @Override
    public boolean isAPIPlayer() {
        return true;
    }
}
