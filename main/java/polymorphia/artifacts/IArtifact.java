package polymorphia.artifacts;

public interface IArtifact {
    String getName();

    double getHealthValue();

    double getStrengthValue();

    double getMovingCost();

    boolean isType(ArtifactType type);

    int getAttackValue();
}
