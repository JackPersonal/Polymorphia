package polymorphia.persistence.nosqldb;

import java.util.ArrayList;
import java.util.List;

public class RoomSnapshot {
    private String name;
    private List<String> neighborNames = new ArrayList<>();
    private List<CharacterSnapshot> characters = new ArrayList<>();
    private List<ArtifactSnapshot> artifacts = new ArrayList<>();

    public RoomSnapshot() {
    }

    public RoomSnapshot(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public RoomSnapshot setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getNeighborNames() {
        return neighborNames;
    }

    public RoomSnapshot setNeighborNames(List<String> neighborNames) {
        this.neighborNames = neighborNames;
        return this;
    }

    public List<CharacterSnapshot> getCharacters() {
        return characters;
    }

    public RoomSnapshot setCharacters(List<CharacterSnapshot> characters) {
        this.characters = characters;
        return this;
    }

    public List<ArtifactSnapshot> getArtifacts() {
        return artifacts;
    }

    public RoomSnapshot setArtifacts(List<ArtifactSnapshot> artifacts) {
        this.artifacts = artifacts;
        return this;
    }
}