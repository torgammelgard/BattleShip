package model;

import controller.BoardListener;

import java.util.ArrayList;

/**
 * An interface which serves as a layer between the Model and the View. Instead of the View holding a reference to
 * the Board directly, the View can hold a reference to IBoard which limits what the View can access.
 *
 * @author Tor Gammelgard
 * @version 2015-10-23
 * @see Board
 */
public interface IBoard {
    Board.Square getSquare(int r, int c);

    ArrayList<Ship> getShips();

    void addListener(BoardListener boardListener);

    void removeListener(BoardListener boardListener);
}
