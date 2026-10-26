package polymorphia.observers;

import ooad.gameobserver.intf.IGameObserver;

public interface IPolymorphiaEvents {
    void attach(IPolymorphiaEventsObserver observer);
}
