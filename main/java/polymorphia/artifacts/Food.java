package polymorphia.artifacts;

public class Food extends Artifact {

    protected Food(String name, double healthValue) {
        super(ArtifactType.Food, name, healthValue);
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f)", name, healthValue);
    }

}
