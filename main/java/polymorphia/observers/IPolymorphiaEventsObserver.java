package polymorphia.observers;

import polymorphia.EventType;

public interface IPolymorphiaEventsObserver {
    void update(EventType eventType, Object eventObject);
}
