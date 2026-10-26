Feature: Games play with reincarnated characters
  As a game player
  I want to play a game with a potions that reincarnate in the maze
  So that I have a richer set of choices


  Scenario: Potions are available in the maze and drank up by characters so that they can reincarnate
    Given a maze with the following ring of rooms:
      | Starting |
      | Other    |
    And a glutton named Galahad in the Starting room with a fixed die of 1
    And the Starting room contains the following artifacts:
      | Type   | Name           | Health Value | Moving Cost | Defense Value | Attack Value |
      | Potion | elixir of life | 3.5          | 0.3         | 0.0           | 0            |
      | Food   | cupcake        | 1.0          | 0.0         | 0.0           | 0            |

    When Galahad plays a turn

    Then there is no Potion in Starting room

    Given a demon named Satan in the Starting room with a fixed die of 8

    When Satan plays a turn
    Then a death occurred
    And "Galahad" has health 3.5
    # This is because Galahad reincarnates with the health of the elixir