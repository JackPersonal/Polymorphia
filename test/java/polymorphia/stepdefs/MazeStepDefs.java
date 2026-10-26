package polymorphia.stepdefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import polymorphia.Die;
import polymorphia.FixedDie;
import polymorphia.artifacts.ArtifactFactory;
import polymorphia.artifacts.ArtifactType;
import polymorphia.artifacts.IArtifact;
import polymorphia.characters.Character;
import polymorphia.characters.CharacterFactory;
import polymorphia.maze.Room;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class MazeStepDefs {
    private static final int DEFAULT_NUMBER_OF_ROOMS = 5;
    private static final int DEFAULT_MIN_CONNECTIONS = 2;
    private static final String BIDIRECTIONAL = "2-way";
    private static final CharacterFactory characterFactory = new CharacterFactory();
    private static final ArtifactFactory artifactFactory = new ArtifactFactory();

    CharacterStepDefs characterStepDefs;
    private final World world;
    
    public MazeStepDefs(World world, CharacterStepDefs characterStepDefs) {
        this.world = world;
        this.characterStepDefs = characterStepDefs;
    }

    @Given("a maze with the following attributes:")
    public void aMazeWithTheFollowingAttributes(Map<String, Integer> mazeArgs) {
        int numRooms = DEFAULT_NUMBER_OF_ROOMS;
        int numConnections = DEFAULT_MIN_CONNECTIONS;
        for (Map.Entry<String, Integer> entry : mazeArgs.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();
            switch (key) {
                case "number of rooms":
                    numRooms = value;
                    break;
                case "number of connections":
                    numConnections = value;
                    break;
            }
        }

        world.getMazeBuilder().createConnectedRooms(numConnections, numRooms);
        addToRooms(mazeArgs);
    }

    private void addToRooms(Map<String, Integer> mazeArgs) {
        for (Map.Entry<String, Integer> entry : mazeArgs.entrySet()) {
            String key = entry.getKey();
            int value = entry.getValue();
            switch (key) {
                case "number of food items":
                    world.getMazeBuilder().addArtifacts(artifactFactory.createFoodItems(value));
                    break;
                case "number of knights":
                    world.getMazeBuilder().addCharacters(characterFactory.createKnights(value));
                    break;
                case "number of gluttons":
                    world.getMazeBuilder().addCharacters(characterFactory.createGluttons(value));
                    break;
                case "number of cowards":
                    world.getMazeBuilder().addCharacters(characterFactory.createCowards(value));
                    break;
                case "number of randoms":
                    world.getMazeBuilder().addCharacters(characterFactory.createRandoms(value));
                    break;
                case "number of creatures":
                    world.getMazeBuilder().addCharacters(characterFactory.createCreatures(value));
                    break;
                case "number of demons":
                    world.getMazeBuilder().addCharacters(characterFactory.createDemons(value));
                    break;
                case "number of armored suits":
                    world.getMazeBuilder().addArtifacts(artifactFactory.createArmoredSuits(value));
                    break;
                case "number of weapons":
                    world.getMazeBuilder().addArtifacts(artifactFactory.createWeapons(value));
                    break;
                case "number of potions":
                    world.getMazeBuilder().addArtifacts(artifactFactory.createPotions(value));
                    break;
                case "number of treasures":
                    world.getMazeBuilder().addArtifacts(artifactFactory.createTreasures(value));
                    break;
                case "number of maps":
                    world.getMazeBuilder().addArtifacts(artifactFactory.createMaps(value));
                    break;
                case "number of cloaks":
                    world.getMazeBuilder().addArtifacts(artifactFactory.createCloaks(value));
                    break;
            }
        }
    }

    @And("^a treasure is hidden in (\\w+) room$")
    public void treasureIsHiddenInRoom(String roomName) {
        Room room = world.getMazeBuilder().getRoom(roomName);
        room.add(artifactFactory.createTreasure());
    }

    @And("^a (knight|glutton|coward|creature|demon|seeker|guard|adventurer|random) named (.+) in the (\\w+) room$")
    public void createCharacterAndPutInRoom(String characterType, String characterName, String roomName) {
        Character character = characterStepDefs.createCharacter(characterType, characterName);
        world.getMazeBuilder().addToRoom(roomName, character);
    }

    @And("^a (knight|glutton|coward|creature|demon|seeker|guard|adventurer) named (.+) in the (\\w+) room with a fixed die of (\\d)$")
    public void createCharacterAndPutInRoom(String characterType, String characterName, String roomName, int fixedRollValue) {
        Character character = characterStepDefs.createCharacter(characterType, characterName);
        character.setDie(new FixedDie(fixedRollValue));
        world.getMazeBuilder().addToRoom(roomName, character);
    }

    @Then("{word} room contains {word}")
    public void roomContainsCharacter(String roomName, String characterName) {
        Room room = world.getMaze().getRoom(roomName);
        Character character = world.getCharacter(characterName);
        assertTrue(room.contains(character));
    }

    @Given("a fully-connected maze with the following rooms:")
    public void aFullyConnectedMazeWithTheFollowingRooms(List<String> roomNames) {
        world.getMazeBuilder().createFullyConnectedRooms(roomNames.toArray(new String[0]));
    }

    @And("the {word} room contains the following food items:")
    public void roomContainsTheFollowingFoodItems(String roomName, List<Map<String, String>> foodDescriptions) {
        Room room = world.getMazeBuilder().getRoom(roomName);
        for (Map<String, String> foodDescription : foodDescriptions) {
            IArtifact food = artifactFactory.createFood(
                    foodDescription.get("Name"),
                    Double.parseDouble(foodDescription.get("Value")));
            room.add(food);
            world.putArtifact(food.getName(), food);
        }
    }

    @And("the {word} room contains the following artifacts:")
    public void roomContainsTheFollowingArtifacts(String roomName, List<Map<String, String>> artifactDescriptions) {
        Room room = world.getMazeBuilder().getRoom(roomName);
        for (Map<String, String> artifactDescription : artifactDescriptions) {
            IArtifact artifact = artifactFactory.create(
                    ArtifactType.valueOf(artifactDescription.get("Type")),
                    artifactDescription.get("Name"),
                    Double.parseDouble(artifactDescription.get("Health Value")),
                    Double.parseDouble(artifactDescription.get("Defense Value")),
                    Double.parseDouble(artifactDescription.get("Moving Cost")),
                    Integer.valueOf(artifactDescription.get("Attack Value")));
            room.add(artifact);
            world.putArtifact(artifact.getName(), artifact);
        }
    }


    @Then("^the (.+) room no longer contains (.+)$")
    public void roomNoLongerContainsFoodItem(String roomName, String foodName) {
        Room room = world.getMaze().getRoom(roomName);
        assertFalse(room.contains(world.getArtifact(foodName)));
    }

    @And("^(.+) is now(| not) in the (.+) room$")
    public void characterIsInRoom(String characterName, String not, String roomName) {
        Character character = world.getCharacter(characterName);
        Room room = world.getRoomWithCharacter(character);
        if (not.equals(" not")) {
            assertNotEquals(roomName, room.getName());
        } else {
            assertEquals(roomName, room.getName());
        }
    }

    @And("{word} did not lose some health")
    public void characterDidNotLoseSomeHealth(String characterName) {
        Character character = world.getCharacter(characterName);
        assertEquals(world.getInitialCharacterHealth(characterName), character.getHealth());
    }

    @And("{word} lost some health")
    public void characterLostSomeHealth(String characterName) {
        Character character = world.getCharacter(characterName);
        assertTrue(character.getHealth() < world.getInitialCharacterHealth(characterName));
    }

    @And("{word} has gained {double} in health")
    public void characterHasGainedInHealth(String characterName, Double lostHealth) {
        Character character = world.getCharacter(characterName);
        assertEquals(world.getInitialCharacterHealth(characterName) + lostHealth, character.getHealth());
    }

    @And("the {word} room has {int} food items")
    public void startingRoomHasNoFoodItems(String roomName, int expectedFoodItems) {
        Room room = world.getMaze().getRoom(roomName);
        assertEquals(expectedFoodItems, room.getFoodItems().size());
    }

    @Given("^a (unidirectional|bidirectional), (randomly|sequentially)-distributed maze with the following attributes:$")
    public void connectedDistributedMazeWithTheFollowingAttributes(String roomConnectionType, String distributionType, Map<String, Integer> mazeArgs) {
        if (Objects.equals(roomConnectionType, "bidirectional")) {
            world.getMazeBuilder().useBidirectionalConnections();
        } else {
            world.getMazeBuilder().useUnidirectionalConnections();
        }
        if (Objects.equals(distributionType, "randomly")) {
            world.getMazeBuilder().distributeRandomly();
        } else {
            world.getMazeBuilder().distributeSequentially();
        }
        aMazeWithTheFollowingAttributes(mazeArgs);
    }

    @Given("a maze with the following ring of rooms:")
    public void aMazeWithTheFollowingRooms(List<String> roomNames) {
        world.getMazeBuilder().createRingOfConnectedRooms(roomNames.toArray(new String[0]));
    }

    @Given("a maze with the following rooms:")
    public void aMazeWithTheFollowingFooms(List<Map<String, String>> roomNeighbors) {
        for (Map<String, String> roomKeyedNeighbors : roomNeighbors) {
            world.getMazeBuilder().createRoom(roomKeyedNeighbors.get("Room"));
        }

        for (Map<String, String> roomKeyedNeighbors : roomNeighbors) {
            String roomName = roomKeyedNeighbors.get("Room");
            world.getMazeBuilder().addRoomIfNotAlreadyInMaze(roomName);
            List<String> neighbors = Arrays.asList(roomKeyedNeighbors.get("Neighbors").split(","));
            for (String neighborName : neighbors) {
                world.getMazeBuilder().addRoomIfNotAlreadyInMaze(neighborName.trim());
                world.getMazeBuilder().addNeighborToRoom(roomName, neighborName.trim());
            }
        }
    }

    @Given("^an? (adventurer|knight|fighter|creature|glutton|coward|seeker|guard|demon) (.*) with health (\\d+) in the (.*) room$")
    public void aCharacterWithHealthInTheStartingRoom(String type, String characterName, Integer initialHealth, String roomName) {
        Character character = characterStepDefs.createCharacterWithHealth(type, characterName, initialHealth);
        world.getMazeBuilder().addToRoom(roomName, character);
    }

    @And("each room is connected to {int} other rooms via {word} connections")
    public void eachRoomIsConnectedToOtherRoomsViaConnections(int numberOfConnections, String connectionType) {
        if (Objects.equals(connectionType, "bidirectional")) {
            world.getMazeBuilder().useBidirectionalConnections();
        } else {
            world.getMazeBuilder().useUnidirectionalConnections();
        }
        world.getMazeBuilder().connectRooms(numberOfConnections);
    }

    @Given("^(.*) has a fixed die of (\\d+)$")
    public void characterHasAFixedDieOf(String characterName, Integer dieValue) {
        Character character = world.getCharacter(characterName);
        Die die = new FixedDie(dieValue);
        character.setDie(die);
    }

    @And("{word} is in the {word} room")
    public void characterIsInTheRoom(String characterName, String roomName) {
        world.getMazeBuilder().addToRoom(roomName, world.getCharacter(characterName));
    }

    @And("there is no {word} in {word} room")
    public void thereIsNoArtifactTypeInRoom(String artifactType, String roomName) {
        Room room = world.getMazeBuilder().getRoom(roomName);
        Optional<IArtifact> artifact = room.getArtifact(ArtifactType.valueOf(artifactType));
        assertFalse(artifact.isPresent());
    }

    @And("the {word} room has no food items")
    public void theRoomHasNoFoodItems(String roomName) {
        assertFalse(world.getMaze().getRoom(roomName).hasFood());
    }
}
