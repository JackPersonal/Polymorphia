Feature: gamePlayWithRandomStrategy
  As a game player
  I want to play a game with characters choosing a random strategy
  So that I can see how the game proceeds

  Scenario: a small game with one random character
    Given a fully-connected maze with the following rooms:
      | Starting |
      | Treasure |
    And a knight named Inane in the Starting room
    And a guard named Satan in the Treasure room
    And the Treasure room contains the following artifacts:
      | Type     | Name  | Health Value | Moving Cost | Defense Value | Attack Value |
      | Treasure | chest | 0.0          | 0.0         | 0.0           | 0            |

    When the game is played in the created maze

    Then the game is over