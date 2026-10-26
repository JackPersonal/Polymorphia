package polymorphia.persistence.nosqldb;

public class ArtifactSnapshot {
    private String kind;
    private String name;
    private double healthValue;
    private double strength;
    private double movingCost;
    private int attackValue;

    public ArtifactSnapshot() {
    }

    public String getKind() {
        return kind;
    }

    public ArtifactSnapshot setKind(String kind) {
        this.kind = kind;
        return this;
    }

    public String getName() {
        return name;
    }

    public ArtifactSnapshot setName(String name) {
        this.name = name;
        return this;
    }

    public double getHealthValue() {
        return healthValue;
    }

    public ArtifactSnapshot setHealthValue(double healthValue) {
        this.healthValue = healthValue;
        return this;
    }

    public double getStrength() {
        return strength;
    }

    public ArtifactSnapshot setStrength(double strength) {
        this.strength = strength;
        return this;
    }

    public double getMovingCost() {
        return movingCost;
    }

    public ArtifactSnapshot setMovingCost(double movingCost) {
        this.movingCost = movingCost;
        return this;
    }

    public int getAttackValue() {
        return attackValue;
    }

    public ArtifactSnapshot setAttackValue(int attackValue) {
        this.attackValue = attackValue;
        return this;
    }
}