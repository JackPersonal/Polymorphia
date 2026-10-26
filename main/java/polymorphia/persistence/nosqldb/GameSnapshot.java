package polymorphia.persistence.nosqldb;

public class GameSnapshot {
    private String id;
    private String name;
    private int turnCount;
    private boolean treasureFound;
    private MazeSnapshot maze;

    public GameSnapshot() {
        this.maze = new MazeSnapshot();
    }

    public GameSnapshot(String id, String name, int turnCount, boolean treasureFound, MazeSnapshot maze) {
        this.id = id;
        this.name = name;
        this.turnCount = turnCount;
        this.treasureFound = treasureFound;
        this.maze = maze;
    }

    public String getId() {
        return id;
    }

    public GameSnapshot setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public GameSnapshot setName(String name) {
        this.name = name;
        return this;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public GameSnapshot setTurnCount(int turnCount) {
        this.turnCount = turnCount;
        return this;
    }

    public boolean isTreasureFound() {
        return treasureFound;
    }

    public GameSnapshot setTreasureFound(boolean treasureFound) {
        this.treasureFound = treasureFound;
        return this;
    }

    public MazeSnapshot getMaze() {
        return maze;
    }

    public GameSnapshot setMaze(MazeSnapshot maze) {
        this.maze = maze;
        return this;
    }
}
