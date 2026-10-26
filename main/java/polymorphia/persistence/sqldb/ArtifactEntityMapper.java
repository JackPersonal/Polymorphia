package polymorphia.persistence.sqldb;

import polymorphia.artifacts.ArtifactType;
import polymorphia.artifacts.IArtifact;

public class ArtifactEntityMapper {
    public polymorphia.persistence.sqldb.ArtifactEntity toEntity(IArtifact artifact) {
        return new polymorphia.persistence.sqldb.ArtifactEntity(
                artifact.getClass().getSimpleName() == null ? null : ArtifactType.valueOf(artifact.getClass().getSimpleName()),
                artifact.getName(),
                artifact.getHealthValue(),
                artifact.getStrengthValue(),
                artifact.getMovingCost(),
                artifact.getAttackValue()
        );
    }
}