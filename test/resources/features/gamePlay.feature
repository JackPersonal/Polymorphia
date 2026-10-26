Feature: Details of game play
  As a game player
  I want to play a game with a variety of characters
  So that I can see how they interact with each other


  Scenario: Play the simplest game
    Given a maze with the following attributes:
      | number of rooms     | 1 |
      | number of knights   | 1 |
      | number of creatures | 1 |

    When the game is played in the created maze

    Then the game is over
    And a fight occurred
    And a death occurred
    And all the adventurers or all of the creatures have died


  Scenario: Play a complicated game
    Given a maze with the following attributes:
      | number of rooms         | 11 |
      | number of food items    | 10 |
      | number of knights       | 3  |
      | number of gluttons      | 1  |
      | number of cowards       | 1  |
      | number of seekers       | 1  |
      | number of randoms       | 1  |
      | number of creatures     | 7  |
      | number of demons        | 4  |
      | number of armored suits | 2  |
      | number of weapons       | 2  |
      | number of potions       | 2  |
      | number of treasures     | 2  |
      | number of maps          | 2  |
      | number of cloaks        | 2  |

    When the game is played in the created maze

    Then the game is over
    And all the adventurers or all of the creatures have died


  Scenario: Fighting
    Given a maze with the following rooms:
      | Room     | Neighbors |
      | Starting | Other     |
      | Other    | Starting  |

    Given an adventurer Bilbo with health 5 in the Starting room
    And a creature Goblin with health 3 in the Starting room
    And Bilbo has a fixed die of 4
    And Goblin has a fixed die of 2

    When the game plays a turn

    # Both characters will fight each other, so two fights occur
    Then 2 fights occurred

    # Bilbo will have lost mandatory fight points of 0.5 twice
    And "Bilbo" has health 3.95

    # And the Goblin will be dead
    And a death occurred

