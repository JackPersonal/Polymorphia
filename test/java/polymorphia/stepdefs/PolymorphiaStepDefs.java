package polymorphia.stepdefs;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.AfterEach;
import polymorphia.ObservablePolymorphia;
import polymorphia.characters.Character;
import polymorphia.commands.ICommand;
import polymorphia.maze.Room;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PolymorphiaStepDefs {
    World world;

    public PolymorphiaStepDefs(World world) {
        this.world = world;
    }

    @AfterEach
    public void tearDown() {
        world.getGame().detach(world.getObserver());
        world.getGame().stop();
    }

    @When("the game is played in the created maze")
    public void theGameIsPlayedInTheCreatedMaze() {
        ObservablePolymorphia game = world.getGame();
        game.attach(world.getObserver());
        game.play();
    }

    @When("{word} plays a turn")
    public void characterPlaysATurn(String characterName) {
        // I don't use the game here, but I need to start it so that the test observer will be attached to the Event Bus
        world.getGame();
        Character character = world.getCharacter(characterName);
        Room room = world.getRoomWithCharacter(character);
        ICommand command = character.selectAction(room);
        command.execute();
        character.turnTaken();
    }

    @When("{int} turns? (:?is|are) played in the game")
    public void turnIsPlayedInTheGame(Integer numberOfTurns) {
        ObservablePolymorphia game = world.getGame();
        for (int i = 0; i < numberOfTurns; i++) {
            game.playTurn();
        }
    }

    @When("the game plays a turn")
    public void theGamePlaysATurn() {
        ObservablePolymorphia game = world.getGame();
        game.playTurn();
        game.waitUntilIdle();   // Wait for all commands to execute
    }

    @Then("all the adventurers or all of the creatures have died")
    public void allTheAdventurersOrAllOfTheCreaturesHaveDied() {
        assertTrue(!world.getGame().hasLivingAdventurers() || !world.getGame().hasLivingCreatures());
    }

    @Then("a death occurred")
    public void aDeathOccurred() {
        assertTrue(world.getObserver().aDeathOccurred());
    }

    @Then("{int} fights occurred")
    public void fightsOccurred(Integer numFights) {
        assertEquals(numFights, world.getObserver().getEventCount("fight"));
    }

    @Then("a game end occurred")
    public void aGameEndOccurred() {
        assertTrue(world.getObserver().aGameEndOccurred());
    }

    @Then("a fight occurred")
    public void aFightOccurred() {
        assertTrue(world.getObserver().aFightOccurred());
    }

    @Then("no fight occurs")
    public void noFightOccurs() {
        assertTrue(world.getObserver().getEventCount("fight") == 0);
    }

    @Then("the game is over")
    public void theGameIsOver() {
        ObservablePolymorphia game = world.getGame();
        game.waitUntilIdle();   // Wait for all commands to execute
        assertTrue(game.isOver());
    }

}
