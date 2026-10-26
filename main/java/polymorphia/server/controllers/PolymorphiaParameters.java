package polymorphia.server.controllers;

public record PolymorphiaParameters(
        String name, String playerName, int numRooms, int numCreatures,
        int numKnights, int numCowards, int numGluttons, int numDemons, int numFood, int numArmor) {
}