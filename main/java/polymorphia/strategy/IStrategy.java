package polymorphia.strategy;

import polymorphia.characters.Character;
import polymorphia.commands.ICommand;
import polymorphia.maze.Room;

public interface IStrategy {
    ICommand selectAction(Character myself, Room room);
    default boolean isFighter() { return false; }
    default boolean isSeeker() { return false; };
    default boolean isGuard() { return false; };
    default boolean isCoward() { return false; };
    default boolean isGlutton() { return false; };
}
