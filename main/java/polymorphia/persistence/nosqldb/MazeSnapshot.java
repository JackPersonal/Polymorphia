package polymorphia.persistence.nosqldb;

import java.util.ArrayList;
import java.util.List;

public class MazeSnapshot {
    private List<RoomSnapshot> rooms = new ArrayList<>();

    public MazeSnapshot() {
    }

    public MazeSnapshot(List<RoomSnapshot> rooms) {
        this.rooms = rooms;
    }

    public List<RoomSnapshot> getRooms() {
        return rooms;
    }

    public MazeSnapshot setRooms(List<RoomSnapshot> rooms) {
        this.rooms = rooms;
        return this;
    }
}
