package polymorphia;

import ooad.gameobserver.intf.IMaze;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import polymorphia.maze.Maze;
import polymorphia.maze.Room;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MazeAdapter implements IMaze {
    private static final Logger logger = LoggerFactory.getLogger(MazeAdapter.class);

    private final List<Room> rooms;

    private final Map<String, Room> roomMap = new HashMap<>();

    public MazeAdapter(Maze maze) {
        this(maze.getRooms());
    }

    public MazeAdapter(List<Room> rooms) {
        this.rooms = rooms;
        for (Room currentRoom : this.rooms) {
            roomMap.put(currentRoom.getName(), currentRoom);
        }
    }

    @Override
    public Set<String> getRoomNames() {
        return roomMap.keySet();
    }

    @Override
    public Set<String> getNeighborsOf(String roomName) {
        return roomMap.get(roomName).getNeighbors().stream().map(Room::getName).collect(Collectors.toSet());
    }

    @Override
    public List<String> getArtifacts(String roomName) {
        logger.debug("Adapter getting contents of room {}", roomName);
        Room room = roomMap.get(roomName);
        return room.getArtifacts().stream().map(Object::toString).toList();
    }

    @Override
    public List<String> getCharacters(String roomName) {
        Room room = roomMap.get(roomName);
        return room.getLivingCharacters().stream().map(Object::toString).toList();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (String roomName : getRoomNames()) {
            builder.append(roomName).append("\n   Neighbors: ");
            for (String neighborName : getNeighborsOf(roomName)) {
                builder.append(neighborName).append(",");
            }
            builder.append("\n   characters: ");
            for (String characterName : getCharacters(roomName)) {
                builder.append(characterName).append(",");
            }
            builder.append("\n   artifacts: ");
            for (String artifactName : getArtifacts(roomName)) {
                builder.append(artifactName).append(",");
            }
            builder.append("\n\n");
        }
        return builder.toString();
    }
}
