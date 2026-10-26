package polymorphia.commands;

import polymorphia.artifacts.IArtifact;
import polymorphia.characters.Character;
import polymorphia.maze.Room;

public class CommandFactory {
    public ICommand newFightCommand(Character me, Character foe, Room room) {
        return new FightCommand(me, foe, room);
    }

    public ICommand newSleepCommand(Character me) {
        return new SleepCommand(me);
    }

    public ICommand newTakeCommand(Character me, Room room, IArtifact artifact) {
        return new TakeCommand(me, room, artifact);
    }

    public ICommand newMoveCommand(Character me, Room room, Room newRoom) {
        return new MoveCommand(me, room, newRoom);
    }

    public ICommand newEatCommand(Character me, Room room, IArtifact food) {
        return new EatCommand(me, room, food);
    }

    public ICommand newWearCommand(Character myself, Room room, IArtifact armor) {
        return new WearCommand(myself, room, armor);
    }

    public ICommand newWieldCommand(Character myself, Room room, IArtifact weapon) {
        return new WieldCommand(myself, room, weapon);
    }

    public ICommand newDonCommand(Character myself, Room room, IArtifact cloak) {
        return new DonCommand(myself, room, cloak);
    }

    public ICommand newDrinkCommand(Character myself, Room room, IArtifact potion) {
        return new DrinkCommand(myself, room, potion);
    }

}
