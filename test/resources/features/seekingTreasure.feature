Feature: Behavior of treasure-seeking character
  As a game player
  I want to see a character methodically search a maze for the treasure
  So that I can see that the treasure will eventually be found

  Scenario: A Seeker will always enter a room it hasn't visited given a choice
    Given a maze with the following rooms:
      | Room     | Neighbors |
      | Starting | Next1     |
      | Next1    | Next2     |
      | Next2    | Next3     |
      | Next3    | Finish    |
    And a seeker named Bilbo in the Starting room

    When Bilbo plays a turn
    Then Bilbo is in the Next1 room

    When Bilbo plays a turn
    Then Bilbo is in the Next2 room

    When Bilbo plays a turn
    Then Bilbo is in the Next3 room

    When Bilbo plays a turn
    Then Bilbo is in the Finish room


  Scenario: A Seeker will find the treasure in a directed way, not a random way
    Given a fully-connected maze with the following rooms:
      | Starting |
      | This     |
      | That     |
      | Other    |
    And a seeker named Bilbo in the Starting room
    And a demon named Satan in the This room
    And a guard named Smaug in the Other room
    And a treasure is hidden in Other room

    When Bilbo plays a turn
    When Bilbo plays a turn
    When Bilbo plays a turn
    When Bilbo plays a turn

    When the game is played in the created maze

    Then the game is over


  Scenario: A Seeker will find the treasure in another maze
    Given a maze with the following rooms:
      | Room     | Neighbors  |
      | Starting | This, That |
      | This     | Starting   |
      | That     | Other      |
      | Other    | That       |
    And a seeker named Bilbo in the Starting room
    And a guard named Satan in the Other room
    And a guard named Smaug in the That room
    And a treasure is hidden in That room

    When Bilbo plays a turn
    When Bilbo plays a turn
    When Bilbo plays a turn
    When Bilbo plays a turn

    When the game is played in the created maze

    # Because treasure is found
    Then the game is over
