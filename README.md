![release version](https://img.shields.io/badge/release-v1.1-blue.svg)
![battleship branch master](https://travis-ci.org/torgammelgard/Battleship.svg?branch=master)

# Battleship
 * The player plays Battleship against the computer. The computer doesn't play against the player.
 * The board consists of 10x10 squares.
 * There's a Start/Restart button.
 * While playing, clicking on a squares reveals if a ship has been hit or not.
 * Information about hits, misses and status of remaining ships is shown to the player.
 * There are four different types of ships.
 * The ships are randomly oriented and placed.
 * The ships have at least one square in between each other.

# Instructions to run the application (Version 1.0)
 * set VM options to : -splash:src/res/splash.png in your IDE
 
# Instructions to run the application (Version 1.1)
 - git clone https://github.com/torgammelgard/Battleship.git
 - cd Battleship
 - mvn package
 - java -jar ./target/battleship-1.1-SNAPSHOT.jar
