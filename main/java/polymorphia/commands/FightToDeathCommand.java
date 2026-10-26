package polymorphia.commands;

import polymorphia.characters.Character;
import polymorphia.maze.Room;

public class FightToDeathCommand extends FightCommand {
    public FightToDeathCommand(Character character, Room room, Character opponent) {
        super(character, opponent, room);
        this.type = CommandType.FIGHT_TO_THE_DEATH;
    }

}
