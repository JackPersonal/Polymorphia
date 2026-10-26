package polymorphia.artifacts;

public class Treasure extends Artifact {
    Treasure(String name) {
        super(ArtifactType.Treasure, name, 0.0);
    }

    @Override
    public String toString() {
        return name;
    }
}
