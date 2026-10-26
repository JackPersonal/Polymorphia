Feature: Games play with weaponized characters
  As a game player
  I want to play a game with a weapons in the maze
  So that I have a richer set of choices


  Scenario: Weapons are available in the maze and picked up by knights for extra protection during fights
    Given a maze with the following ring of rooms:
      | Starting |
    And a knight named Galahad in the Starting room with a fixed die of 4
    And the Starting room contains the following artifacts:
      | Type   | Name    | Health Value | Moving Cost | Defense Value | Attack Value |
      | Weapon | sword   | 0.0          | 0.3         | 0.0           | 1            |
      | Food   | cupcake | 1.0          | 0.0         | 0.0           | 0            |

    When Galahad plays a turn

    And there is no Weapon in Starting room

    Given a demon named Satan in the Starting room with a fixed die of 2

    When Galahad plays a turn
    Then a fight occurred

    # The sword's attack value of 1, makes the damage inflicted 3 instead of 2, plus standard
    # fighting loss of 0.5
    And "Satan" has health 11.5