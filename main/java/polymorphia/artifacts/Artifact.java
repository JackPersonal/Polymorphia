package polymorphia.artifacts;


import java.io.Serializable;

public abstract class Artifact implements IArtifact, Serializable {
    private static final long serialVersionUID = 1L;

    public static double DEFAULT_STRENGTH = 0.0;
    public static double DEFAULT_MOVING_COST = 0.0;
    public static double DEFAULT_HEALTH_VALUE = 0.0;
    public static int DEFAULT_ATTACK_VALUE = 0;

    protected final String name;
    protected final double healthValue;
    protected double strength;
    protected final double movingCost;
    protected final int attackValue;
    private final ArtifactType type;

    public Artifact(ArtifactType type, String name, double healthValue, double strength, double movingCost, int attackValue) {
        this.healthValue = healthValue;
        this.type = type;
        this.name = name;
        this.strength = strength;
        this.movingCost = movingCost;
        this.attackValue = attackValue;
    }

    public Artifact(ArtifactType type, String name, double healthValue) {
        this(type, name, healthValue, DEFAULT_STRENGTH, DEFAULT_MOVING_COST, DEFAULT_ATTACK_VALUE);
    }

    public boolean isType(ArtifactType type) {
        return this.type == type;
    }

    public String getName() {
        return name;
    }

    public double getHealthValue() {
        return healthValue;
    }

    public double getStrengthValue() {
        return strength;
    }

    public double getMovingCost() {
        return movingCost;
    }

    public int getAttackValue() {
        return attackValue;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Artifact artifact = (Artifact) other;
        return name.equals(artifact.name) &&
                healthValue == artifact.healthValue &&
                movingCost == artifact.movingCost &&
                strength == artifact.strength &&
                attackValue == artifact.attackValue &&
                type == artifact.type;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        long temp;

        temp = Double.doubleToLongBits(healthValue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));

        temp = Double.doubleToLongBits(strength);
        result = 31 * result + (int) (temp ^ (temp >>> 32));

        temp = Double.doubleToLongBits(movingCost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));

        result = 31 * result + attackValue;
        result = 31 * result + type.hashCode();

        return result;
    }
}
