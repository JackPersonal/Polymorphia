package polymorphia.artifacts;

public class Armor extends Artifact {
    public static double DEFAULT_HEALTH_VALUE = 0.0;
    public static double DEFAULT_MOVING_COST = 0.5;
    public static int DEFAULT_ATTACK_VALUE = 0;


    public Armor(String name, double strength) {
        this(name, strength, DEFAULT_MOVING_COST);
    }

    public Armor(String name, double strength, double movingCost) {
        super(ArtifactType.Armor, name, DEFAULT_HEALTH_VALUE, strength, movingCost, DEFAULT_ATTACK_VALUE);
    }

    @Override
    public String toString() {
        String formattedStrength = String.format("%.2f", getStrengthValue());
        return getName() + " armor(strength:" + formattedStrength + ", movingCost:" + getMovingCost() + ")";
    }

}