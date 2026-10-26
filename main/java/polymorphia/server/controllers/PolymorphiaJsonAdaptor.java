package polymorphia.server.controllers;

import ooad.gameobserver.intf.IMaze;
import polymorphia.ObservablePolymorphia;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PolymorphiaJsonAdaptor {
    public final String name;
    public final int turn;
    public final boolean gameOver;
    public final List<JsonRoom> rooms = new ArrayList<>();
    public String statusMessage;

    public PolymorphiaJsonAdaptor(ObservablePolymorphia polymorphia) {
        name = polymorphia.getName();
        turn = polymorphia.getTurnCount();
        statusMessage = "Turn " + turn + " just ended.";
        gameOver = polymorphia.isOver();
        if (gameOver) {
            statusMessage += polymorphia.createFinalResults();
        }

//        livingAdventurers = polymorphia.getLivingAdventurers().stream().map(Character::getName).toList();
//        livingCreatures = polymorphia.getLivingCreatures().stream().map(Character::getName).toList();
        IMaze maze = polymorphia.getMaze();
        for (String roomName : maze.getRoomNames()) {
            JsonRoom roomJson = new JsonRoom();
            roomJson.name = roomName;
            roomJson.neighbors = maze.getNeighborsOf(roomName);
            List <String> contents = new ArrayList<>();
            contents.addAll(maze.getCharacters(roomName));
            contents.addAll(maze.getArtifacts(roomName));
            roomJson.contents = contents;
            rooms.add(roomJson);
        }
    }

    public static class JsonRoom {
        public String name;
        public Set<String> neighbors;
        public List<String> contents;
    }
}