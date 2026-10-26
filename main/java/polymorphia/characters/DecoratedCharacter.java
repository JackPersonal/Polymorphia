package polymorphia.characters;

import polymorphia.Die;
import polymorphia.artifacts.IArtifact;
import polymorphia.strategy.IStrategy;

public abstract class DecoratedCharacter extends Character {
    Character myself;
    IArtifact artifact;

    public DecoratedCharacter(Character self, IArtifact artifact) {
        myself = self;
        this.artifact = artifact;
    }

    @Override
    public boolean isVisible() { return myself.isVisible(); };

    @Override
    public String getName() {
        return myself.getName();
    }

    @Override
    public String getBaseName() {
        return myself.getBaseName();
    }

    @Override
    public Double getHealth() {
        return myself.getHealth();
    }

    @Override
    protected void setHealth(double health) {
        myself.setHealth(health);
    }

    @Override
    protected Die getDie() {
        return myself.getDie();
    }

    @Override
    public void setDie(Die die) {
        myself.setDie(die);
    }

    @Override
    protected IStrategy getStrategy() {
        return myself.getStrategy();
    }

    @Override
    public void setStrategy(IStrategy strategy) {
        myself.setStrategy(strategy);
    }

    @Override
    public Boolean isAdventurer() {
        return myself.isAdventurer();
    }

    @Override
    protected double getMovingCost() {
        return artifact.getMovingCost() + myself.getMovingCost();
    }

    @Override
    protected void loseHealth(double healthToLose) {
        myself.loseHealth(healthToLose);
    }
}
