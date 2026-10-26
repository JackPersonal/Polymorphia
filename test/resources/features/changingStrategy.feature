Feature: How the strategy of various characters change
  As a game player
  I want to see how various characters change strategy based on past events
  So that I can see how they interact with each other

  Scenario: A fighter will become a coward once his health drops below Coward Threshold
    Given a maze with the following rooms:
      | Room     | Neighbors |
      | Starting | Other     |
      | Other    | Starting  |

    And a knight Bilbo with health 6 in the Starting room
    And a creature Goblin with health 3 in the Starting room
    And Bilbo has a fixed die of 2
    And Goblin has a fixed die of 5

    # Here Bilbo plays like a knight, fighting creatures if he can
    When Bilbo plays a turn

    Then a fight occurred
    # Bilbo will have lost mandatory fight points of 0.5 twice but the aging cost of 0.05
    And "Bilbo" has health 2.45

    # Here Bilbo plays like a coward, running away
    When Bilbo plays a turn

    Then Bilbo is in the Other room


  Scenario: A glutton will become a fighter once they have killed a creature
    Given a maze with the following rooms:
      | Room     | Neighbors |
      | Starting | Other     |
      | Other    | Starting  |

    And a glutton Bilbo with health 5 in the Starting room
    And a creature Goblin with health 2 in the Starting room
    And Bilbo has a fixed die of 5
    And Goblin has a fixed die of 2

    # Here Goblin fights Bilbo, and Bilbo will have killed Goblin in the first turn
    When Goblin plays a turn

    Then a fight occurred
    And a death occurred

    Given a creature Goblin with health 4 in the Starting room
    And the Starting room contains the following food items:
      | Name    | Value |
      | cupcake | 1.0   |

    # Now Bilbo plays like a fighter, fighting creatures if he can
    When Bilbo plays a turn
    Then 2 fights occurred


  Scenario: A coward will become a seeker once they have picked up a map
    Given a maze with the following rooms:
      | Room     | Neighbors |
      | Starting | Other     |
      | Other    | Starting  |

    And the Other room contains the following artifacts:
      | Type        | Name         | Health Value | Defense Value | Moving Cost | Attack Value |
      | Food        | cupcake      | 1.0          | 0             | 0           | 0            |
      | TreasureMap | Treasure Map | 0.0          | 0             | 0           | 0            |
      | Treasure    | chest        | 0.0          | 0             | 0           | 0            |

    And a coward Bilbo with health 5 in the Starting room
    And a creature Goblin with health 2 in the Starting room

    When Bilbo plays a turn
    Then Bilbo is in the Other room

    When Bilbo plays a turn
    Then the Other room no longer contains Treasure Map

    When Bilbo plays a turn
    Then the Other room no longer contains Chest
