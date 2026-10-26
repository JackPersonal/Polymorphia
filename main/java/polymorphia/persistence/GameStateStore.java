package polymorphia.persistence;

import polymorphia.ObservablePolymorphia;

import java.util.List;

public interface GameStateStore {
    String save(ObservablePolymorphia polymorphia);
    ObservablePolymorphia load(String key);

    List<ObservablePolymorphia> loadAllGames();
}
