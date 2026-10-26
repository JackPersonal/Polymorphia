package polymorphia.maze;

import polymorphia.artifacts.ArtifactType;
import polymorphia.artifacts.IArtifact;
import polymorphia.characters.Character;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

public class Room implements Serializable {
    static private final Random rand = new Random();

    private transient Object lock = new Object();

    private final String name;
    private final Set<Room> neighbors = new CopyOnWriteArraySet<>();
    private final Set<Character> characters = new HashSet<>();
    private final Set<IArtifact> artifacts = new HashSet<>();

    public Room(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Character> getLivingCreatures() {
        return getLivingCharacters().stream()
                .filter(Character::isCreature)
                .toList();
    }

    public List<String> getContents() {
        List<String> characterStrings = new ArrayList<>(getLivingCharacters().stream()
                .map(Object::toString)
                .toList());

        for (IArtifact artifact : getArtifacts()) {
            characterStrings.add(artifact.toString());
        }

        return characterStrings;
    }

    void addNeighbor(Room neighbor) {
        // Make sure we are never a neighbor of ourselves
        if (this != neighbor) {
            this.neighbors.add(neighbor);
        }
    }

    void addNeighbor(Room neighbor, boolean bidirectional) {
        this.addNeighbor(neighbor);
        if (bidirectional) {
            neighbor.addNeighbor(this);
        }
    }

    public String toString() {
        String representation = "\t" + name + ":\n\t\t";
        representation += String.join("\n\t\t", getContents());
        representation += "\n";
        return representation;
    }

    public void add(Character character) {
        synchronized (lock) {
            characters.add(character);
        }
    }

    public Boolean hasLivingCreatures() {
        return getLivingCharacters().stream().anyMatch(Character::isCreature);
    }

    public Boolean hasLivingAdventurers() {
        return getLivingCharacters().stream().anyMatch(Character::isAdventurer);
    }

    public void remove(Character character) {
        synchronized (lock) {
            characters.remove(character);
        }
    }

    public Optional<Character> getRandomCreature() {
        return getLivingCreatures().stream().findAny();
    }

    public Room getRandomNeighbor() {
        if (neighbors.isEmpty()) {
            return null;
        }
        return neighbors.stream().toList().get(rand.nextInt(neighbors.size()));
    }

    public List<Character> getLivingCharacters() {
        synchronized (lock) {
            return characters.stream()
                    .filter(Character::isAlive)
                    .toList();
        }
    }

    public boolean hasNeighbor(Room neighbor) {
        return neighbors.contains(neighbor);
    }

    public int numberOfNeighbors() {
        return neighbors.size();
    }

    public boolean hasNeighbors() {
        return !neighbors.isEmpty();
    }

    public boolean contains(Character character) {
        synchronized (lock) {
            return characters.contains(character);

        }
    }

    public boolean contains(IArtifact artifact) {
        synchronized (lock) {
            return artifacts.contains(artifact);
        }
    }

    public void add(IArtifact artifact) {
        synchronized (lock) {
            artifacts.add(artifact);
        }
    }

    public boolean hasFood() {
        return getArtifact(ArtifactType.Food).isPresent();
    }

    public Optional<IArtifact> getFood() {
        return getArtifact(ArtifactType.Food);
    }

    public List<Room> getNeighbors() {
        return List.copyOf(neighbors);
    }

    public void remove(IArtifact artifact) {
        synchronized (lock) {
            artifacts.remove(artifact);
        }
    }

    public Optional<IArtifact> getTreasure() {
        return getArtifact(ArtifactType.Treasure);
    }

    public Optional<IArtifact> getArtifact(ArtifactType type) {
        synchronized (lock) {
            return artifacts.stream().filter(a -> a.isType(type)).findFirst();
        }
    }

    public Optional<Character> getGuardOrCreature() {
        List<Character> creatures = getLivingCreatures();
        Optional<Character> possiblyGuard = creatures.stream().filter(Character::isGuard).findFirst();
        if (possiblyGuard.isPresent()) {
            return possiblyGuard;
        } else {
            return creatures.stream().findFirst();
        }
    }

    public void add(List<IArtifact> artifacts) {
        synchronized (lock) {
            this.artifacts.addAll(artifacts);
        }
    }

    public List<IArtifact> getArtifacts() {
        synchronized (lock) {
            return new ArrayList<>(artifacts);
        }
    }

    List<IArtifact> getArtifacts(ArtifactType type) {
        synchronized (lock) {
            return getArtifacts().stream()
                    .filter(artifact -> artifact.isType(type))
                    .toList();
        }
    }

    public Optional<Character> getRandomCharacterNotMe(Character me) {
        return getLivingCharacters().stream()
                .filter(Character::isVisible)
                .filter(character -> !character.equals(me))
                .findFirst();
    }

    public Optional<IArtifact> getMap() {
        return getArtifact(ArtifactType.TreasureMap);
    }

    public void replace(Character myself, Character newMe) {
        synchronized (lock) {
            if (characters.contains(myself)) {
                characters.remove(myself);
                characters.add(newMe);
            }
        }
    }

    public List<IArtifact> getFoodItems() {
        return getArtifacts(ArtifactType.Food);
    }

    public Boolean hasLivingVisibleAdventurers() {
        return !getLivingVisibleAdventurers().isEmpty();
    }

    public List<Character> getLivingVisibleAdventurers() {
        return getLivingCharacters().stream()
                .filter(Character::isAdventurer)
                .filter(Character::isVisible)
                .toList();
    }

    public Optional<Character> getCharacterWithName(String name) {
        return getLivingCharacters().stream()
                .filter(character -> character.getBaseName().contains(name))
                .findFirst();
    }

    public boolean containsCharacterWithName(String name) {
        return getLivingCharacters().stream()
                .anyMatch(character -> character.getBaseName().contains(name));
    }

    private void readObject(ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        lock = new Object();
    }
}
