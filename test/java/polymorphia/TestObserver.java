package polymorphia;

import ooad.gameobserver.intf.IGameObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestObserver implements IGameObserver {
    private static final List<String> EVENTS = List.of("fight", "died", "game end");
    private final Map<String, List<String>> eventMessages = new HashMap<>();

    @Override
    public void update(String message) {
        for (String event : EVENTS) {
            if (message.contains(event)) {
                eventMessages.putIfAbsent(event, new ArrayList<>());
                List<String> messages = eventMessages.get(event);
                messages.add(message);
            }
        }
    }

    public int getEventCount(String event) {
        return eventMessages.getOrDefault(event, List.of()).size();
    }

    public boolean aFightOccurred() {
        return getEventCount("fight") > 0;

    }

    public boolean aDeathOccurred() {
        return getEventCount("died") > 0;

    }

    public boolean aGameEndOccurred() {
        return getEventCount("game end") > 0;
    }

    public boolean aCharacterHadAFight(String characterName) {
        return eventMessages.get("fight").stream().anyMatch(message -> message.contains(characterName));
    }
}
