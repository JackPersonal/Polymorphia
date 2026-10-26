package polymorphia.stepdefs;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import polymorphia.FixedDie;
import polymorphia.characters.Character;
import polymorphia.characters.CharacterFactory;
import polymorphia.maze.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CharacterStepDefs {
    World world;
    CharacterFactory characterFactory = new CharacterFactory();

    public CharacterStepDefs(World world) {
        this.world = world;
    }

    public Character createCharacter(String characterType, String characterName) {
        Character character = null;
        switch (characterType) {
            case "knight":
                character = characterFactory.createKnight(characterName);
                break;
            case "glutton":
                character = characterFactory.createGlutton(characterName);
                break;
            case "coward":
                character = characterFactory.createCoward(characterName);
                break;
            case "creature":
                character = characterFactory.createCreature(characterName);
                break;
            case "demon":
                character = characterFactory.createDemon(characterName);
                break;
            case "guard":
                character = characterFactory.createGuard(characterName);
                break;
            case "seeker":
                character = characterFactory.createSeeker(characterName);
                break;
            case "random":
                character = characterFactory.createRandom(characterName);
                break;
            default:
                System.out.println("Error -- shouldn't get here");
        }
        return world.putCharacter(characterName, character);
    }

    public Character createCharacterWithHealth(String characterType, String characterName, double initialHealth) {
        Character character = null;
        switch (characterType) {
            case "knight":
            case "adventurer":
            case "fighter":
                character = characterFactory.createKnight(characterName, initialHealth);
                break;
            case "glutton":
                character = characterFactory.createGlutton(characterName, initialHealth);
                break;
            case "coward":
                character = characterFactory.createCoward(characterName, initialHealth);
                break;
            case "creature":
                character = characterFactory.createCreature(characterName, initialHealth);
                break;
            case "demon":
                character = characterFactory.createDemon(characterName, initialHealth);
                break;
            case "guard":
                character = characterFactory.createGuard(characterName, initialHealth);
                break;
            case "seeker":
                character = characterFactory.createSeeker(characterName, initialHealth);
                break;
            default:
                System.out.println("Error -- shouldn't get here");
        }
        return world.putCharacter(characterName, character);
    }


    @When("this fight occurs:")
    public void thisFightOccurs(List<Map<String, String>> fightData) {
        List<Character> fighters = new ArrayList<>();
        for (int i = 0; i <= 1; i++) {
            Character fighter = world.getCharacter(fightData.get(i).get("foe"));
            int rollValue = Integer.parseInt(fightData.get(i).get("roll"));
            fighter.setDie(new FixedDie(rollValue));
            fighters.add(fighter);
        }
        fighters.getFirst().fight(fighters.getLast());
    }

    @Then("{string} has health {double}")
    public void hasHealth(String name, double expectedHealth) {
        Character character = world.getCharacter(name);
        System.out.println(character.getName() + " has health " + character.getHealth());
        System.out.println("Expected health: " + expectedHealth);
        assertEquals(expectedHealth, character.getHealth());
    }

    @Then("{word} picked up the Armor")
    public void characterPickedUpTheArmor(String characterName) {
        Character character = world.getCharacter(characterName);
        Room currentRoom = world.getRoomWithCharacter(character);
        Character armoredCharacter = currentRoom.getLivingCharacters().stream()
                .filter(c -> c.getName().contains(characterName))
                .findFirst()
                .get();
        assertTrue(armoredCharacter.getName().toLowerCase().contains("armor"));
    }

    @And("{word} has lost the default fight cost in health")
    public void characterHasLostTheDefaultFightCostInHealth(String characterName) {
        Character character = world.getCharacter(characterName);
        Double initialHealth = world.getInitialCharacterHealth(characterName);
        assertEquals(initialHealth - Character.HEALTH_LOST_IN_FIGHT_REGARDLESS_OF_OUTCOME, character.getHealth());
    }

    @And("{word} lost the default-moving cost plus {double} in health")
    public void numberOfDefaultFightCostLosses(String characterName, double additionalCost) {
        Character character = world.getCharacter(characterName);
        Double initialHealth = world.getInitialCharacterHealth(characterName);
        Double expectedHealth = initialHealth - Character.HEALTH_LOST_IN_MOVING_ROOMS - additionalCost;
        assertTrue(expectedHealth >= character.getHealth());
    }

    @And("{word} has lost {int} times the default fight cost in health")
    public void characterHasLostNTimesTheDefaultFightCostInHealth(String characterName, int numberOfDefaultFightCostLosses) {
        Character character = world.getCharacter(characterName);
        Double initialHealth = world.getInitialCharacterHealth(characterName);
        Double expectedHealth = initialHealth - Character.HEALTH_LOST_IN_FIGHT_REGARDLESS_OF_OUTCOME * numberOfDefaultFightCostLosses;
        assertTrue(expectedHealth >= character.getHealth());

    }

    @Then("{word} has been in a fight")
    public void characterHasBeenInAFight(String characterName) {
        assertTrue(world.getObserver().aCharacterHadAFight(characterName));
    }
}
