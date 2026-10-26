package polymorphia.characters;

import org.slf4j.Logger;
import polymorphia.Die;
import polymorphia.EventBus;
import polymorphia.RandomDie;
import polymorphia.artifacts.IArtifact;
import polymorphia.commands.ICommand;
import polymorphia.maze.Room;
import polymorphia.strategy.IStrategy;
import polymorphia.strategy.SleepStrategy;
import polymorphia.strategy.StrategyFactory;

import java.io.ObjectInputStream;
import java.io.Serializable;

public abstract class Character implements Serializable {
    private static final Double COWARD_THRESHOLD = 4.0;
    static Logger logger = org.slf4j.LoggerFactory.getLogger(Character.class);
    static StrategyFactory strategyFactory = new StrategyFactory();

    public static final Double HEALTH_LOST_IN_FIGHT_REGARDLESS_OF_OUTCOME = 0.5;
    public static final Double HEALTH_LOST_IN_MOVING_ROOMS = 0.25;
    static final Double HEALTH_LOST_EACH_TURN = 0.05;

    private transient Object lock = new Object();

    protected String name;
    private volatile Double health;
    private IStrategy strategy;
    private Die die;

    // This is only for serialization purposes
    protected Character() {
    }

    private void readObject(ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        lock = new Object();
    }

    public void setStrategy(IStrategy strategy) {
        this.strategy = strategy;
    }

    public Character(String name, Double health) {
        this(name, health, RandomDie.sixSided());
    }

    public Character(String name, Double health, Die die) {
        this(name, health, die, new SleepStrategy());
    }

    public Character(String name, Double initialHealth, Die die, IStrategy strategy) {
        this.name = name;
        this.health = initialHealth;
        this.die = die;
        this.strategy = strategy;
    }

    public String toString() {
        String formattedHealth = String.format("%.2f", getHealth());
        return getName() + "(" + formattedHealth + ")";
    }

    protected void loseHealth(double healthPoints) {
        synchronized (lock) {
            if (getHealth() <= 0) {
                // already dead, probably called for mandatory health loss for having a fight
                return;
            }     // already dead, probably called for mandatory health loss for having a fight

            setHealth(getHealth() - healthPoints);

            if (isAlive() && isFighter() && getHealth() < COWARD_THRESHOLD) {
                setStrategy(strategyFactory.newCowardStrategy());
                logger.info("{} is now a coward!", name);
            }

            if (getHealth() <= 0) {
                String msg = name + " just died!";
                logger.info(msg);
                EventBus.getInstance().postEvent(msg);
            }
        }
    }

    private boolean isFighter() {
        return getStrategy().isFighter();
    }

    protected void setHealth(double healthValue) {
        synchronized (lock) {
            this.health = healthValue;
        }
    }

    void gainHealth(Double healthPoints) {
        synchronized (lock) {
            setHealth(getHealth() + healthPoints);
        }
    }

    public Double getHealth() {
        return health;
    }

    public String getName() {
        return name;
    }

    public Boolean isAlive() {
        return getHealth() > 0;
    }

    protected void loseFightDamage(double fightDamage) {
        synchronized (lock) {
            loseHealth(fightDamage);
        }
    }

    public Boolean isAdventurer() {
        return false;
    }

    public Boolean isCreature() {
        return false;
    }
    
    public boolean fight(Character foe) {
        synchronized (lock) {
            boolean won = false;
            Integer instigatorRoll = getRoll();
            Integer foeRoll = foe.getRoll();
            String fightMessage = this + " is fighting " + foe + " and rolled " + instigatorRoll + " vs " + foeRoll;
            logger.info(fightMessage);

            if (instigatorRoll > foeRoll) {
                foe.loseFightDamage(instigatorRoll - foeRoll);
                won = true;
            } else if (foeRoll > instigatorRoll) {
                loseFightDamage(foeRoll - instigatorRoll);
            }

            loseHealth(Character.HEALTH_LOST_IN_FIGHT_REGARDLESS_OF_OUTCOME);
            foe.loseHealth(Character.HEALTH_LOST_IN_FIGHT_REGARDLESS_OF_OUTCOME);
            EventBus.getInstance().postEvent(fightMessage);

            // If a glutton kills a creature, it becomes a fighter
            if (isAlive() && !foe.isAlive() && isGlutton()) {
                setStrategy(strategyFactory.newFighterStrategy());
                logger.info("{} is now a fighter!", getName());
            }
            if (!isAlive() && foe.isAlive() && foe.isGlutton()) {
                foe.setStrategy(strategyFactory.newFighterStrategy());
                logger.info("{} is now a fighter!", foe.getName());
            }
            return won;
        }
    }

    private boolean isGlutton() {
        return getStrategy().isGlutton();
    }

    protected IStrategy getStrategy() {
        return strategy;
    }

    protected int getRoll() {
        return getDie().roll();
    }

    protected Die getDie() {
        return die;
    }

    public ICommand selectAction(Room currentLocation) {
        return getStrategy().selectAction(this, currentLocation);
    }

    public void movedTo(Room nextLocation) {
        logger.info("{} moved to {}", getName(), nextLocation.getName());
        loseHealth(getMovingCost());
    }

    public void setDie(Die die) {
        this.die = die;
    }

    public boolean eat(IArtifact food) {
        this.gainHealth(food.getHealthValue());
        logger.info("{} ate {}!", getName(), food);
        return true;
    }

    public boolean isGuard() {
        return false;
    }

    public void commandSubmitted() {
        // Do nothing -- just a possible hook
    }

    public void turnTaken() {
        loseHealth(HEALTH_LOST_EACH_TURN);
    }

    public boolean isCoward() {
        return getStrategy().isCoward();
    }

    protected double getMovingCost() {
        return HEALTH_LOST_IN_MOVING_ROOMS;
    }

    public String getBaseName() {
        return name;
    }

    public boolean isVisible() {
        return true;
    }

    // needed for api subclass
    public boolean isAPIPlayer() {
        return false;
    }

}
