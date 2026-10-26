package polymorphia.observers;

import ooad.gameobserver.intf.IGameObserver;
import ooad.gameobserver.intf.IGameObserverProvider;
import ooad.gameobserver.intf.IMutableServiceContext;
import ooad.gameobserver.intf.SimpleServiceContext;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SPIGameObserverTest {

    @Test
    void getMazeObserverViaSPI() {
        List<IGameObserver> observers = new ArrayList<>();

//        Maze maze = Maze.getNewBuilder(new RoomFactory())
//                .create2x2Grid()
//                .build();
//        ObservablePolymorphia polymorphia = new ObservablePolymorphia(maze);
        IMutableServiceContext serviceContext = new SimpleServiceContext();
//        serviceContext.register(IMazeSubject.class, polymorphia);

        ServiceLoader<IGameObserverProvider> loader = ServiceLoader.load(IGameObserverProvider.class);
        Iterator<IGameObserverProvider> iterator = loader.iterator();

        while (iterator.hasNext()) {
            IGameObserverProvider gameObserverProvider = iterator.next();
            System.out.println("Found provider implementation: " + gameObserverProvider.getClass().getName());
            assertNotNull(gameObserverProvider);

            try {
                IGameObserver gameObserver = gameObserverProvider.create(serviceContext);
                System.out.println("\tFound observer implementation: " + gameObserver.getClass().getName());
                assertNotNull(gameObserver);
                observers.add(gameObserver);
            } catch (Exception e) {
                System.out.println("\tError creating IGameObserver from provider " + gameObserverProvider.getClass().getName() + ": " + e.getMessage());
            }
        }
        System.out.println("Found " + observers.size() + " observers");
        System.out.println(observers);
        assertTrue(observers.size() > 0);
    }
}
