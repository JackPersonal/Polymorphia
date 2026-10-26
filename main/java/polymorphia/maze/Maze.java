package polymorphia.maze;

import org.slf4j.Logger;
import polymorphia.artifacts.IArtifact;
import polymorphia.characters.Character;

import java.io.Serializable;
import java.util.*;
import java.util.stream.IntStream;

public class Maze implements Iterable<Room>, Serializable {
    private static final Random rand = new Random();

    private List<Room> rooms;

    private Maze() {
        this(new ArrayList<>());
    }

    public Maze(List<Room> rooms) {
        this(rooms, new ArrayList<>());
    }

    public Maze(List<Room> rooms, List<Character> characters) {
        this.rooms = rooms;
        characters.forEach(this::addToRandomRoom);
    }

    @Override
    public Iterator<Room> iterator() {
        return new RoomIterator();
    }

    public Optional<Room> getRoomWithCharacter(String baseName) {
        return rooms.stream()
                .filter(room -> room.containsCharacterWithName(baseName))
                .findFirst();
    }

    public int numLivingCharacters() {
        return rooms.stream()
                .mapToInt(room -> room.getLivingCharacters().size())
                .sum();
    }

    public Optional<Character> getCharacter(String userName) {
        return rooms.stream()
                .flatMap(room -> room.getLivingCharacters().stream())
                .filter(character -> character.getBaseName().contains(userName))
                .findFirst();
    }

    class RoomIterator implements Iterator<Room> {
        int index = 0;

        @Override
        public boolean hasNext() {
            return index < rooms.size();
        }

        @Override
        public Room next() {
            return rooms.get(index++);
        }
    }

    public int size() {
        return rooms.size();
    }

    public String toString() {
        return String.join("\n\n", rooms.stream().map(Object::toString).toList());
    }

    public Boolean hasLivingCreatures() {
        return rooms.stream().anyMatch(Room::hasLivingCreatures);
    }

    public Boolean hasLivingAdventurers() {
        return rooms.stream().anyMatch(Room::hasLivingAdventurers);
    }

    public Room getRandomRoom() {
        return rooms.get(rand.nextInt(rooms.size()));
    }

    public List<Character> getLivingCreatures() {
        return getLivingCharacters().stream()
                .filter(Character::isCreature)
                .toList();
    }

    public List<Character> getLivingCharacters() {
        return rooms.stream()
                .flatMap(room -> room.getLivingCharacters().stream())
                .toList();
    }

    private void addToRandomRoom(Character character) {
        getRandomRoom().add(character);
    }

    public boolean hasLivingCharacters() {
        return getLivingCharacters().stream()
                .anyMatch(Character::isAlive);
    }

    public List<Character> getLivingAdventurers() {
        return getLivingCharacters().stream()
                .filter(Character::isAdventurer)
                .toList();
    }

    public List<Room> getRooms() {
        return List.copyOf(rooms);
    }

    private void addRoom(Room room) {
        rooms.add(room);
    }

    public static Builder getNewBuilder(RoomFactory roomFactory) {
        return new Builder(roomFactory);
    }

    public Room getRoom(String roomName) {
        return rooms.stream()
                .filter(room -> room.getName().contains(roomName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No room with name " + roomName));
    }

    public static class Builder {
        static Logger logger = org.slf4j.LoggerFactory.getLogger(Builder.class);
        public static String[] grid2x2RoomNames = new String[]{"Northwest", "Northeast", "Southwest", "Southeast"};
        public static String[] grid3x3RoomNames = new String[]{"Northwest", "North", "Northeast", "West", "Center", "East", "Southwest", "South", "Southeast"};

        final Maze maze = new Maze();
        Map<String, Room> roomMap = new HashMap<>();
        private Boolean distributeRandomly = true;
        private boolean useBidirectionalConnections = true;
        private int currentRoomIndex = 0;
        private final RoomFactory roomFactory;

        private Builder(RoomFactory roomFactory) {
            this.roomFactory = roomFactory;
        }

        private Room nextRoom() {
            if (distributeRandomly) {
                return maze.getRandomRoom();
            }
            return maze.getRooms().get(currentRoomIndex++ % maze.getRooms().size());
        }

        private Room nextRoomNotMe(Room room) {
            Room nextRoom = nextRoom();
            while (nextRoom == room) {
                nextRoom = nextRoom();
            }
            return nextRoom;
        }

        public Builder createRoom(String name) {
            Room initialRoom = roomFactory.createRoom(name);
            maze.addRoom(initialRoom);
            return this;
        }

        public Builder createGridOfRooms(int rows, int columns) {
            String[] roomNames = new String[rows * columns];
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    roomNames[row + column * rows] = "Grid[" + row + "," + column + "]";
                }
            }
            return createGridOfRooms(rows, columns, roomNames);
        }

        public Builder createGridOfRooms(int rows, int columns, String[] roomNames) {
            Room[][] roomGrid = new Room[rows][columns];
            List<Room> rooms = new ArrayList<>();
            // Notice -- don't use i and j. Use row and column -- they are better
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    Room newRoom = roomFactory.createRoom(roomNames[row * columns + column]);
                    roomGrid[row][column] = newRoom;
                    rooms.add(newRoom);
                    roomMap.put(newRoom.getName(), newRoom);
                }
            }
            maze.rooms.addAll(rooms);

            // Now connect the rooms
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    Room currentRoom = roomGrid[row][column];
                    Room neighbor;
                    if (row > 0) {
                        neighbor = roomGrid[row - 1][column];
                        currentRoom.addNeighbor(neighbor, useBidirectionalConnections);
                    }
                    if (column > 0) {
                        neighbor = roomGrid[row][column - 1];
                        currentRoom.addNeighbor(neighbor, useBidirectionalConnections);
                    }
                }
            }
            return this;
        }

        public Builder create2x2Grid() {
            return createGridOfRooms(2, 2, grid2x2RoomNames);
        }

        public Builder create3x3Grid() {
            return createGridOfRooms(3, 3, grid3x3RoomNames);
        }

        public Builder createFullyConnectedRooms(String... roomNames) {
            List<Room> rooms = new ArrayList<>();
            for (String roomName : roomNames) {
                Room currentRoom = roomFactory.createRoom(roomName);
                roomMap.put(currentRoom.getName(), currentRoom);
                rooms.add(currentRoom);
            }
            maze.rooms.addAll(rooms);
            connectRooms(rooms, rooms.size() - 1);
            return this;
        }

        public Builder createFullyConnectedRooms(Integer numRooms) {
            List<Room> rooms = new ArrayList<>();
            IntStream.range(0, numRooms).forEach(_ -> {
                Room currentRoom = roomFactory.createRoom();
                roomMap.put(currentRoom.getName(), currentRoom);
                rooms.add(currentRoom);
            });
            maze.rooms.addAll(rooms);
            fullyConnectRooms(rooms);
            return this;
        }

        public Builder fullyConnectRooms(List<Room> rooms) {
            for (Room room : rooms) {
                for (Room otherRoom : rooms) {
                    room.addNeighbor(otherRoom, true);
                }
            }
            return this;
        }

        public Builder createConnectedRooms(Integer minConnections, Integer numRooms) {
            IntStream.range(0, numRooms).forEach(_ -> {
                Room currentRoom = roomFactory.createRoom();
                roomMap.put(currentRoom.getName(), currentRoom);
                maze.rooms.add(currentRoom);
            });
            connectRooms(maze.rooms, minConnections);
            return this;
        }

        public Builder addNeighborToRoom(String fromRoom, String toRoom) {
            Room from = roomMap.get(fromRoom);
            Room to = roomMap.get(toRoom);
            from.addNeighbor(to, useBidirectionalConnections);
            return this;
        }

        public Builder connectRooms(Integer minConnections) {
            return connectRooms(maze.rooms, minConnections);
        }

        public Builder connectRooms(List<Room> rooms, Integer minConnections) {
            if (maze.size() < 2) {
                return this;
            }
            int realMinimumConnections = Math.min(minConnections, Math.max(maze.size() - 1, 1));
            for (Room room : rooms) {
                while (room.numberOfNeighbors() < realMinimumConnections) {
                    Room neighbor = nextRoomNotMe(room);
                    if (!room.hasNeighbor(neighbor)) {
                        room.addNeighbor(neighbor, useBidirectionalConnections);
                    }
                }
                assert room.numberOfNeighbors() >= realMinimumConnections;
            }
            return this;
        }

        public Builder add(Character character) {
            nextRoom().add(character);
            return this;
        }

        public Builder addCharacters(List<Character> characters) {
            for (Character character : characters) {
                nextRoom().add(character);
            }
            return this;
        }

        public Builder add(IArtifact artifact) {
            nextRoom().add(artifact);
            return this;
        }

        public Builder addArtifacts(List<IArtifact> artifacts) {
            for (IArtifact artifact : artifacts) {
                nextRoom().add(artifact);
            }
            return this;
        }

        public Builder addRoomIfNotAlreadyInMaze(String roomName) {
            if (!roomMap.containsKey(roomName)) {
                roomMap.put(roomName, roomFactory.createRoom(roomName));
                maze.addRoom(roomMap.get(roomName));
            }
            return this;
        }

        public Maze build() {
            if (maze.size() == 0) {
                logger.warn("No rooms created. Creating a single room.");
                addRoomIfNotAlreadyInMaze("Only Room");
            }
            for (Room room : maze.rooms) {
                if (room.numberOfNeighbors() == 0) {
                    logger.warn("Room {} has no neighbors. Connecting it to another room.", room.getName());
                    room.addNeighbor(nextRoom(), useBidirectionalConnections);
                }
            }
            if (!maze.hasLivingAdventurers()) {
                logger.error("No adventurers created. Terminating game.");
                throw new IllegalStateException("No adventurers created. Terminating game.");
            }
            return maze;
        }

        public Builder addToRoom(String roomName, Character character) {
            roomMap.get(roomName).add(character);
            return this;
        }

        public Builder addToRoom(String roomName, IArtifact artifact) {
            roomMap.get(roomName).add(artifact);
            return this;
        }

        public Room getRoom(String roomName) {
            return roomMap.get(roomName);
        }

        public Builder distributeSequentially() {
            distributeRandomly = false;
            return this;
        }

        public Builder distributeRandomly() {
            distributeRandomly = true;
            return this;
        }

        public Builder useUnidirectionalConnections() {
            useBidirectionalConnections = false;
            return this;
        }

        public Builder useBidirectionalConnections() {
            useBidirectionalConnections = true;
            return this;
        }

        public Builder addToSameRoom(Character character, IArtifact artifact) {
            Room room = nextRoom();
            room.add(character);
            room.add(artifact);
            return this;
        }

        public Builder createStraightLineRooms(String... roomNames) {
            return createLinearlyConnectedRooms(false, roomNames);
        }

        private Builder createLinearlyConnectedRooms(boolean makeRing, String... roomNames) {
            List<Room> rooms = new ArrayList<>();
            for (String roomName : roomNames) {
                Room room = roomFactory.createRoom(roomName);
                roomMap.put(roomName, room);
                rooms.add(room);
            }
            for (int i = 0; i < rooms.size() - 1; i++) {
                rooms.get(i).addNeighbor(rooms.get(i + 1), useBidirectionalConnections);
            }
            maze.rooms.addAll(rooms);
            return this;
        }

        public Builder createRingOfConnectedRooms(String... roomNames) {
            return createLinearlyConnectedRooms(true, roomNames);
        }

        public Builder createAGridOfRooms(int gridWidth, int gridHeight, String gridRoomNamePrefix) {
            String[] roomNames = new String[gridHeight * gridWidth];
            for (int row = 0; row < gridHeight; row++) {
                for (int column = 0; column < gridWidth; column++) {
                    roomNames[row * gridWidth + column] = gridRoomNamePrefix + row + "-" + column;
                }
            }
            return createGridOfRooms(gridHeight, gridWidth, roomNames);
        }
    }
}
