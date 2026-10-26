package polymorphia.artifacts;

public class Cloak extends Artifact {
    public Cloak(String name, double strength) {
        super(ArtifactType.Cloak, name, DEFAULT_HEALTH_VALUE, strength, DEFAULT_MOVING_COST, DEFAULT_ATTACK_VALUE);
    }

    public void showWear() {
        strength--;
    }
}
