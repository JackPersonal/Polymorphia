Feature: Games played with armored characters
  As a game player
  I want to play a game with a armor in the maze
  So that I have a richer set of choices

  Scenario: Armored suits are available in the maze and worn by knights for extra protection during fights
    Given a maze with the following ring of rooms:
      | Starting |
    And a knight named Galahad in the Starting room with a fixed die of 4
    And the Starting room contains the following artifacts:
      | Type  | Name    | Health Value | Moving Cost | Defense Value | Attack Value |
      | Armor | iron    | 0.0          | 0.3         | 2.0           | 0            |
      | Food  | cupcake | 1.0          | 0.0         | 0.0           | 0            |

    When Galahad plays a turn

    Then there is no Armor in Starting room

    Given a demon named Satan in the Starting room with a fixed die of 6
    When Galahad plays a turn

    Then a fight occurred
    And Galahad has been in a fight
    And Satan has been in a fight


  Scenario: Armored suits are heavy and incur extra health costs when moving
    Given a maze with the following ring of rooms:
      | Starting |
      | Other    |
    And a knight named Galahad in the Starting room
    And the Starting room contains the following artifacts:
      | Type  | Name | Health Value | Defense Value | Moving Cost | Attack Value |
      | Armor | iron | 0.0          | 2.0           | 0.3         | 0            |

    When Galahad plays a turn

    Then Galahad picked up the Armor
    And there is no Armor in Starting room

    When Galahad plays a turn

    Then Galahad is now in the Other room
    And Galahad lost the default-moving cost plus 0.3 in health


  Scenario: Knight wears two suits of armor to get even more protection
    Given a maze with the following ring of rooms:
      | Starting |
    And a knight named Galahad in the Starting room with a fixed die of 1
    And the Starting room contains the following artifacts:
      | Type  | Name    | Health Value | Moving Cost | Defense Value | Attack Value |
      | Armor | iron    | 0.0          | 0.3         | 2.0           | 0            |
      | Armor | mithril | 0.0          | 0.1         | 3.0           | 0            |

    When Galahad plays a turn

    Then Galahad picked up the Armor

    When Galahad plays a turn
    # Now Galahad is wearing two suits of armor
    Then there is no Armor in Starting room

    Given a demon named Satan in the Starting room with a fixed die of 6
    When Galahad plays a turn

    Then a fight occurred

    # Despite losing the fight by 2, the armor prevents the loss
    # But both fight each other, so they both lose double the default fight cost
    And Galahad has been in a fight
    And Satan has been in a fight