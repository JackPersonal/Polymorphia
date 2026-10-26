package polymorphia;

import ooad.gameobserver.intf.IGame;
import ooad.gameobserver.intf.IGameObserver;
import ooad.gameobserver.intf.IMaze;
import ooad.gameobserver.intf.IMazeSubject;
import org.slf4j.Logger;
import polymorphia.characters.Character;
import polymorphia.maze.Maze;
import polymorphia.maze.Room;
import polymorphia.observers.IPolymorphiaEventsObserver;


public class ObservablePolymorphia extends Polymorphia implements IGame, IMazeSubject, IPolymorphiaEventsObserver {
    static Logger logger = org.slf4j.LoggerFactory.getLogger(ObservablePolymorphia.class);

    public ObservablePolymorphia(Maze maze) {
        super(maze);
        // Polymorphia also listens for events, specifically when the treasure gets found
        EventBus.getInstance().attach(this);
    }

    public ObservablePolymorphia(String name, Maze maze) {
        super(name, maze);
        // Polymorphia also listens for events, specifically when the treasure gets found
        EventBus.getInstance().attach(this);
    }

    @Override
    public void update(EventType eventType, Object eventObject) {
        switch (eventType) {
            case TREASURE_FOUND:
                setTreasureFound();
                break;
            case ADD_CHARACTER:
                Character newCharacter = (Character) eventObject;
                addToRandomRoom(newCharacter);
                break;
            default:
                logger.info("Unhandled event type: " + eventType);
        }
    }

    private void addToRandomRoom(Character newCharacter) {
        Room room = maze.getRandomRoom();
        room.add(newCharacter);
    }

    @Override
    public void attach(IGameObserver observer) {
        EventBus.getInstance().attach(observer);
    }

    @Override
    public void detach(IGameObserver observer) {
        EventBus.getInstance().detach(observer);
    }

    @Override
    public IMaze getMaze() {
        return new MazeAdapter(maze);
    }

    @Override
    public void playTurn() {
//        logger.info("playTurn triggered");
        super.playTurn();
        EventBus.getInstance().postEvent("Finished turn " + turnCount);
    }
}
