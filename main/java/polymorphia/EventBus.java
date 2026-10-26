package polymorphia;

import ooad.gameobserver.intf.IGame;
import ooad.gameobserver.intf.IGameObserver;
import polymorphia.observers.IPolymorphiaEvents;
import polymorphia.observers.IPolymorphiaEventsObserver;

import java.util.ArrayList;
import java.util.List;

public class EventBus implements IGame, IPolymorphiaEvents {
    private List<IGameObserver> observers = new ArrayList<>();
    private List<IPolymorphiaEventsObserver> polymorphiaEventsObservers = new ArrayList<>();

    private static EventBus instance;

    private EventBus() {
    }

    public static EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    @Override
    public void attach(IGameObserver observer) {
        observers.add(observer);
    }

    @Override
    public void detach(IGameObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void attach(IPolymorphiaEventsObserver observer) {
        polymorphiaEventsObservers.add(observer);
    }

    public void postEvent(String event) {
        for (IGameObserver observer : observers) {
            observer.update(event);
        }
    }

    public void postEvent(EventType eventType, Object eventObject) {
        for (IPolymorphiaEventsObserver observer : polymorphiaEventsObservers) {
            observer.update(eventType, eventObject);
        }
    }

}
