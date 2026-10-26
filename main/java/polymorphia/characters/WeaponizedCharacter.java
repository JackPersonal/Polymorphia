package polymorphia.characters;

import polymorphia.artifacts.IArtifact;

public class WeaponizedCharacter extends DecoratedCharacter {

    public WeaponizedCharacter(Character myself, IArtifact weapon) {
        super(myself, weapon);
    }

    @Override
    public int getRoll() {
        String msg = String.format("%s wields %s and increases roll by %d", super.getName(), artifact.getName(), artifact.getAttackValue());
        logger.info(msg);
        return myself.getRoll() + artifact.getAttackValue();
    }

    @Override
    public String getName() {
        return artifact.getName() + "-wielding " + myself.getName();
    }
}
