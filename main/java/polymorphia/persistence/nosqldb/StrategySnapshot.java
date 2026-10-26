package polymorphia.persistence.nosqldb;

import java.util.HashMap;
import java.util.Map;

public class StrategySnapshot {
    private String kind;
    private Map<String, Object> state = new HashMap<>();

    public StrategySnapshot() {
    }

    public String getKind() {
        return kind;
    }

    public StrategySnapshot setKind(String kind) {
        this.kind = kind;
        return this;
    }

    public Map<String, Object> getState() {
        return state;
    }

    public StrategySnapshot setState(Map<String, Object> state) {
        this.state = state;
        return this;
    }
}