package polymorphia.commands;

import polymorphia.characters.Character;

public interface ICommand {
    boolean execute();
    Character getCharacter();
    CommandType getType();
    String optionString();
    default String uiLabel() {
        return getType().toString() + ": " + optionString();
    }
}
