Feature: Games play with cloaked characters
  As a game player
  I want to play a game with a cloaks in the maze
  So that I have a richer set of choices


  Scenario: Cloaks are available in the maze and picked up by characters to hide from creatures
    Given a maze with the following ring of rooms:
      | Starting |
      | Other    |
    And a coward named Galahad in the Starting room with a fixed die of 4
    And the Starting room contains the following artifacts:
      | Type  | Name         | Health Value | Moving Cost | Defense Value | Attack Value |
      | Cloak | invisibility | 0.0          | 0.3         | 2.0           | 0            |

    When Galahad plays a turn

    Then there is no Cloak in Starting room

    Given a demon named Satan in the Starting room with a fixed die of 6

    When Satan plays a turn
    Then Satan is now in the Other room
    # This is because Satan cannot see Galahad and so moves


  Scenario: Cloaks wear out and then the character is visible again

    Given a maze with the following ring of rooms:
      | Starting |
    And a coward named Galahad in the Starting room
    And the Starting room contains the following artifacts:
      | Type  | Name         | Health Value | Moving Cost | Defense Value | Attack Value |
      | Cloak | invisibility | 0.0          | 0.0         | 1.0           | 0            |

    When Galahad plays a turn
    Then there is no Cloak in Starting room

    Given a demon named Satan in the Starting room

    When Satan plays a turn
    Then no fight occurs
    # This is because Satan cannot see Galahad

    When Galahad plays a turn
    # Now the cloak has disintegrated and Satan will see Galahad
    And Satan plays a turn

    Then a fight occurred

