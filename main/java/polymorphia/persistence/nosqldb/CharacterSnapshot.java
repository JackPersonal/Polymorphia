package polymorphia.persistence.nosqldb;

import java.util.ArrayList;
import java.util.List;

public class CharacterSnapshot {
    private String kind;
    private String name;
    private double health;
    private boolean alive;
    private boolean visible = true;
    private StrategySnapshot strategy;
    private List<ArtifactSnapshot> inventory = new ArrayList<>();

    public CharacterSnapshot() {
        this.strategy = new StrategySnapshot();
    }

    public String getKind() {
        return kind;
    }

    public CharacterSnapshot setKind(String kind) {
        this.kind = kind;
        return this;
    }

    public String getName() {
        return name;
    }

    public CharacterSnapshot setName(String name) {
        this.name = name;
        return this;
    }

    public double getHealth() {
        return health;
    }

    public CharacterSnapshot setHealth(double health) {
        this.health = health;
        return this;
    }

    public boolean isAlive() {
        return alive;
    }

    public CharacterSnapshot setAlive(boolean alive) {
        this.alive = alive;
        return this;
    }

    public boolean isVisible() {
        return visible;
    }

    public CharacterSnapshot setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public StrategySnapshot getStrategy() {
        return strategy;
    }

    public CharacterSnapshot setStrategy(StrategySnapshot strategy) {
        this.strategy = strategy;
        return this;
    }

    public List<ArtifactSnapshot> getInventory() {
        return inventory;
    }

    public CharacterSnapshot setInventory(List<ArtifactSnapshot> inventory) {
        this.inventory = inventory;
        return this;
    }
}