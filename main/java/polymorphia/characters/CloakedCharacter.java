package polymorphia.characters;

import polymorphia.artifacts.Cloak;
import polymorphia.artifacts.IArtifact;

public class CloakedCharacter extends DecoratedCharacter {

    CloakedCharacter(Character self, IArtifact cloak) {
        super(self, cloak);
    }

    @Override
    public boolean isVisible() {
        if (artifact.getStrengthValue() > 0) {
            String msg = String.format("%s wasn't seen! Cloak has value of %f", getName(), artifact.getStrengthValue());
            logger.info(msg);
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        String decoratedName = super.getName();
        if (artifact.getStrengthValue() > 0) {
            decoratedName = artifact.getName() + "-wearing " + decoratedName;
        }
        return decoratedName;
    }

    @Override
    public void turnTaken() {
        super.turnTaken();
        ((Cloak) artifact).showWear();
    }
}
