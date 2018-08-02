import view.MainFrame;

import javax.swing.*;

/**
 * <h2>Project Battleship</h2>
 * <p>
 * The player plays Battleship against the computer. The computer doesn't play against the player.
 * The board consists of 10x10 squares.
 * There's a Start/Restart button.
 * While playing, clicking on a squares reveals if a ship has been hit or not.
 * Information about hits, misses and status of remaining ships is shown to the player.
 * There are four different types of ships.
 * The ships are randomly oriented and placed.
 * The ships have at least one square in between each other.
 *
 * Project was written in IntelliJ IDEA 15.
 *
 * @author Tor Gammelgard
 * @version 2015-10-15
 */
public class Battleship {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
