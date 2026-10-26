Feature: Behavior of various characters
  As a game player
  I want to see how various characters react to different situations
  So that I can see how they interact with each other

  Scenario: A Knight moves if nothing is in the current room
    Given a fully-connected maze with the following rooms:
      | Starting |
      | Final    |
    And a knight named Galahad in the Starting room

    When Galahad plays a turn

    Then Final room contains Galahad

  Scenario: A Knight will fight a Creature even if he can run away
    Given a fully-connected maze with the following rooms:
      | Starting |
      | Other    |
    And a knight named Galahad in the Starting room
    And a creature named Ogre in the Starting room

    When Galahad plays a turn

    Then Ogre lost some health

  Scenario: A Knight will eat food if no creature is in the room
    Given a fully-connected maze with the following rooms:
      | Starting |
      | Other    |
    And the Starting room contains the following food items:
      | Name    | Value |
      | cupcake | 1.0   |
    And a knight named Galahad in the Starting room

    When Galahad plays a turn

    Then the Starting room no longer contains cupcake
    And Galahad is in the Starting room
    And Galahad has gained 0.95 in health

  Scenario: A Glutton will eat if it can
    Given a fully-connected maze with the following rooms:
      | Starting |
      | Final    |
    And a glutton named Tiny in the Starting room
    And the Starting room contains the following food items:
      | Name    | Value |
      | cupcake | 1.0   |

    When Tiny plays a turn

    Then the Starting room no longer contains cupcake
    # Minus the 0.05 per turn health loss
    And Tiny has gained 0.95 in health


  Scenario: Coward will run away if it can, ignoring food
    Given a fully-connected maze with the following rooms:
      | Starting |
      | Other    |
    And the Starting room contains the following food items:
      | Name  | Value |
      | apple | 1.0   |
    And a coward named RunAway in the Starting room
    And a creature named Ogre in the Starting room

    When RunAway plays a turn

    Then Ogre did not lose some health
    And RunAway lost some health
    And RunAway is in the Other room


  Scenario: Glutton will eat food if it is available over fighting or running
    Given a fully-connected maze with the following rooms:
      | Starting |
      | Other    |
    And the Starting room contains the following food items:
      | Name  | Value |
      | apple | 1.0   |
    And a glutton named Eats-a-lot in the Starting room
    And a creature named Ogre in the Starting room

    When Eats-a-lot plays a turn

    Then Eats-a-lot is in the Starting room
    And the Starting room has no food items
        # Minus the 0.05 per turn health loss
    And Eats-a-lot has gained 0.95 in health
    And Ogre did not lose some health


  Scenario: A Demon will fight a Glutton even when food and neighboring rooms are available
    Given a fully-connected maze with the following rooms:
      | Starting |
      | Other    |
    And a glutton named Eats-a-lot in the Starting room
    And a demon named Satan in the Starting room
    And the Starting room contains the following food items:
      | Name  | Value |
      | apple | 1.0   |

    When Satan plays a turn

    Then Eats-a-lot lost some health
    And Satan lost some health