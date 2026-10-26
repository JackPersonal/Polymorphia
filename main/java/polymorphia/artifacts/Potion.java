package polymorphia.artifacts;

public class Potion extends Artifact {
    public Potion(String name, double healthValue) {
        super(ArtifactType.Potion, name, healthValue);
    }

    @Override
    public String toString() {
        return getName() + "(" + String.format("%.2f", getHealthValue()) + ")";
    }
}
