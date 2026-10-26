package polymorphia.maze;

public class RoomFactory {
    public static String[] NAMES = new String[]{"Room of Horrors", "Dungeon", "Pit of Despair", "Sanctuary",
            "Den of Souls", "Map Room", "Fangorn Forest", "Rivendell", "Misty Mountains", "Mordor", "Shire",
            "Hobbiton", "Gondor", "Chamber of Secrets", "Room of Doom", "Room of Gloom"};

    private int currentNameIndex = 0;

    public Room createRoom(String name) {
        return new Room(name);
    }

    public Room createRoom() {
        return createRoom(getNextRoomName());
    }

    private String getNextRoomName() {
        String candidateName = NAMES[currentNameIndex++ % NAMES.length];
        if (currentNameIndex >= NAMES.length) {
            candidateName += (currentNameIndex / NAMES.length);
        }
        return candidateName;
    }

}
