package model;

import controller.BoardListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A model for the Battleship's game board. Can place out ships randomly and start a new game.
 * Handles basic operations to the board like setting a square or resetting the board.
 *
 * @author Tor Gammelgard
 * @version 2015-10-15
 * @see model.IBoard
 */

public class Board implements IBoard {

    public static final int ROWS = 10;
    public static final int COLUMNS = 10;

    private List<List<Square>> board;
    private List<BoardListener> listeners;
    private List<Ship> ships;

    /**
     * A counter for keeping track of how many times the <code>placeShips</code> is called recursively.
     */
    private int fcnCallCounter = 0;

    public Board() {
        listeners = new ArrayList<>();
        ships = new ArrayList<>();
        board = new ArrayList<>();
        for (int i = 0; i < ROWS; i++) {
            board.add(new ArrayList<>());
            for (int j = 0; j < COLUMNS; j++) {
                board.get(i).add(new Square(i, j));
            }
        }
    }

    /**
     * Clears and resets the entire board and notifies all the listeners.
     */
    public void clearBoard() {
        for (List<Square> row : board)
            for (Square square : row) {
                square.setHit(false);
                square.setShip(null);
                square.setOccupied(false);
            }
        ships.clear();

        fireBoardChanged();
    }

    /**
     * Sets a square's boolean hit if it's not already set.
     *
     * @param row a row index
     * @param col a column index
     *
     * @return true if the square wasn't already hit
     */
    public boolean setSquare(int row, int col) {
        Square sq = board.get(row).get(col);
        if (sq.isHit())
            return false;
        else {
            if (sq.isOccupied()) {
                sq.getShip().addHit();
            }
            sq.setHit(true);

            fireSquaredChanged(sq);

            return true;
        }
    }

    /**
     * Gets a copy of the square at (row, col)
     *
     * @param row a row index
     * @param col a column index
     *
     * @return a copy of the square
     */
    public Square getSquare(int row, int col) {
        return board.get(row).get(col).getCopy();
    }

    /**
     * Gets a copy of all the ships in play.
     *
     * @return a copy of the all ships
     */
    public ArrayList<Ship> getShips() {
        return new ArrayList<>(ships);
    }

    /**
     * Notifies all listeners that a change to the board has been made.
     */
    public void fireBoardChanged() {
        if (listeners != null) {
            for (BoardListener listener : listeners)
                listener.boardChanged();
        }
    }

    /**
     * Notifies all listeners that a change to a square has been made.
     *
     * @param sq the square that was changed
     */
    public void fireSquaredChanged(Square sq) {
        if (listeners != null) {
            for (BoardListener listener : listeners)
                listener.squareChanged(sq.row, sq.col);
        }
    }

    /**
     * Adds a listener to this board.
     *
     * @param listener a reference to a BoardListener
     *
     * @see BoardListener
     */
    public void addListener(BoardListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener.
     *
     * @param listener a reference to a BoardListener
     *
     * @see BoardListener
     */
    public void removeListener(BoardListener listener) {
        listeners.remove(listener);
    }

    /**
     * Checks to see if the position (row, col) is on the board.
     *
     * @param row the row index
     * @param col the column index
     *
     * @return true if the location (row, col) is on the board
     */
    private boolean isOnBoard(int row, int col) {
        if (row < 0 || row >= ROWS)
            return false;
        return !(col < 0 || col >= COLUMNS);
    }

    /**
     * Checks if the ship can be rotated to the next direction.
     *
     * @param ship the ship to be rotated
     *
     * @return true if the ship can be rotated
     */
    private boolean isLegalRotation(Ship ship) {
        int r = ship.getRow();
        int c = ship.getCol();

        Direction oldDirection = ship.getDirection();
        Direction newDirection = ship.getDirection().getNext();

        ship.setDirection(newDirection);
        if (!isLegalPlace(ship, r, c)) {
            ship.setDirection(oldDirection);
            return false;
        }
        ship.setDirection(oldDirection);
        return true;
    }

    /**
     * Tries to rotate the ship.
     *
     * @param ship the ship to be rotated
     *
     * @return true if the ship was successfully rotated
     */
    public boolean rotateShip(Ship ship) {

        if (!isLegalRotation(ship))
            return false;

        Direction newDirection = ship.getDirection().getNext();

        eraseShip(ship);

        ship.setDirection(newDirection);

        placeShip(ship, ship.getRow(), ship.getCol());

        return true;
    }

    /**
     * Tries to move the ship to a new location.
     *
     * @param ship the ship to be moved
     * @param row  destination row index
     * @param col  destination column index
     *
     * @return true if the ship was successfully moved
     */
    public boolean moveShip(Ship ship, int row, int col) {

        if (!isOnBoard(row, col) || !isLegalPlace(ship, row, col))
            return false;

        eraseShip(ship);
        placeShip(ship, row, col);
        ship.setLocation(row, col);      // update the ship's information of where it is
        return true;
    }

    /**
     * Erases a ship from the board.
     *
     * @param ship the ship to be removed
     */
    private void eraseShip(Ship ship) {
        int r = ship.getRow();
        int c = ship.getCol();
        for (int i = 0; i < ship.getLength(); i++) {
            board.get(r).get(c).setOccupied(false);
            board.get(r).get(c).setShip(null);
            if (ship.getDirection().equals(Direction.RIGHT))
                c++;
            else if (ship.getDirection().equals(Direction.DOWN))
                r++;
            else if (ship.getDirection().equals(Direction.LEFT))
                c--;
            else if (ship.getDirection().equals(Direction.UP))
                r--;
        }
    }


    /**
     * Checks if the place (r, c) is a legal placement of the ship.
     *
     * @param ship - ship to be placed
     * @param r    - row
     * @param c    - col
     *
     * @return - true if (r, c) is a legal place
     */
    private boolean isLegalPlace(Ship ship, int r, int c) {

        Square sq;

        // behind the ship
        Coord[] coords = new Coord[ship.getLength() * 2 + 2];
        Coord[] shipcoords = new Coord[ship.getLength()];

        if (ship.getDirection().equals(Direction.UP))
            coords[0] = new Coord(r + 1, c);
        else if (ship.getDirection().equals(Direction.DOWN))
            coords[0] = new Coord(r - 1, c);
        else if (ship.getDirection().equals(Direction.LEFT))
            coords[0] = new Coord(r, c + 1);
        else if (ship.getDirection().equals(Direction.RIGHT))
            coords[0] = new Coord(r, c - 1);

        // sides of the ship
        int counter = 0;
        for (int i = 0; i < ship.getLength(); i++) {
            if (ship.getDirection().equals(Direction.UP)) {
                coords[2 * i + 1] = new Coord(r - i, c - 1);
                shipcoords[counter++] = new Coord(r - i, c);
                coords[2 * i + 2] = new Coord(r - i, c + 1);
            } else if (ship.getDirection().equals(Direction.DOWN)) {
                coords[2 * i + 1] = new Coord(r + i, c - 1);
                shipcoords[counter++] = new Coord(r + i, c);
                coords[2 * i + 2] = new Coord(r + i, c + 1);
            } else if (ship.getDirection().equals(Direction.LEFT)) {
                coords[2 * i + 1] = new Coord(r - 1, c - i);
                shipcoords[counter++] = new Coord(r, c - i);
                coords[2 * i + 2] = new Coord(r + 1, c - i);
            } else if (ship.getDirection().equals(Direction.RIGHT)) {
                coords[2 * i + 1] = new Coord(r - 1, c + i);
                shipcoords[counter++] = new Coord(r, c + i);
                coords[2 * i + 2] = new Coord(r + 1, c + i);
            }
        }

        // forward of the coord
        if (ship.getDirection().equals(Direction.UP))
            coords[coords.length - 1] = new Coord(r - ship.getLength(), c);
        else if (ship.getDirection().equals(Direction.DOWN))
            coords[coords.length - 1] = new Coord(r + ship.getLength(), c);
        else if (ship.getDirection().equals(Direction.LEFT))
            coords[coords.length - 1] = new Coord(r, c - ship.getLength());
        else if (ship.getDirection().equals(Direction.RIGHT))
            coords[coords.length - 1] = new Coord(r, c + ship.getLength());


        for (Coord coord : shipcoords) {
            if (!isOnBoard(coord.getR(), coord.getC()))
                return false;
        }

        for (Coord coord : shipcoords) {
            sq = board.get(coord.getR()).get(coord.getC());
            if (sq.isOccupied() && sq.getShip() != ship)
                return false;
        }

        for (Coord coord : coords) {
            if (isOnBoard(coord.getR(), coord.getC())) {
                sq = board.get(coord.getR()).get(coord.getC());
                if (sq.isOccupied() && sq.getShip() != ship)
                    return false;
            }
        }

        return true;
    }

    /**
     * A helper class for representing a location.
     */
    private class Coord {
        private int r, c;

        Coord(int r, int c) {
            this.r = r;
            this.c = c;
        }

        /**
         * @return the row
         */
        int getR() {
            return r;
        }

        /**
         * @return the column
         */
        int getC() {
            return c;
        }
    }


    /**
     * Tries to place the ship (with internal direction) at (r, c)
     *
     * @param ship - the ship to be placed
     * @param r    - row
     * @param c    - column
     *
     * @return - true if ship was placed
     */
    private boolean placeShip(Ship ship, int r, int c) {

        if (!isLegalPlace(ship, r, c))
            return false;
        else {
            ship.setLocation(r, c);
            for (int i = 0; i < ship.getLength(); i++) {
                board.get(r).get(c).setOccupied(true);
                board.get(r).get(c).setShip(ship);
                if (ship.getDirection().equals(Direction.RIGHT))
                    c++;
                else if (ship.getDirection().equals(Direction.DOWN))
                    r++;
                else if (ship.getDirection().equals(Direction.LEFT))
                    c--;
                else if (ship.getDirection().equals(Direction.UP))
                    r--;
            }
        }

        return true;
    }

    /**
     * Recursive placement method
     *
     * @param ships - All ships to be placed
     * @param i     - index in the ships array (use 0 to start this method)
     *
     * @return - true if all ships were successfully placed
     */
    private boolean placeShips(Ship[] ships, int i) {
        int r = 0;
        int c = 0;
        fcnCallCounter++;

        // check if all ships have been successfully placed
        if (i == ships.length)
            return true;

        //
        while (true) {
            if (placeShip(ships[i], r, c))
                if (placeShips(ships, i + 1))
                    return true;
                else
                    eraseShip(ships[i]);

            if (++c >= COLUMNS) {
                c = 0;
                if (++r >= ROWS) {
                    if (ships[i].getDirection().equals(Direction.RIGHT)) {
                        ships[i].setDirection(ships[i].getDirection().getNext());
                        c = 0;
                        r = 0;
                    } else {
                        ships[i].setDirection(Direction.RIGHT);
                        break;
                    }
                }
            }

        }
        return false;
    }

    /**
     * Tries to place all ships randomly on the board by first placing them on the board and then
     * randomly tries to rotate and move them (1000 times).
     *
     * @param s an array of ships to be placed
     *
     * @return true if all the ships were successfully placed
     */
    private boolean placeAllShipsRandomly(Ship[] s) {

        fcnCallCounter = 0;

        Collections.addAll(ships, s);

        // Upper constraint : if the total length of all ships is greater than the
        // total number of squares on the board, then placement is impossible.
        int totLength = 0;
        for (Ship ship : s) {
            totLength += ship.getLength();
        }
        if (totLength > ROWS*COLUMNS) {
            System.out.println("Upper constr");
            return false;
        }

        // go ahead and try to find a place for all the ships
        if (placeShips(s, 0)) {
            System.out.println("Found place for all ships! : " + fcnCallCounter);
        } else {
            System.out.println("Didn't find place for all ships! : " + fcnCallCounter);
            return false;
        }

        Random rand = new Random(System.currentTimeMillis());

        int tmp;                    // temporary int for picking random numbers
        int c = 0;                  // counter for successful translations and rotations
        int numberOfTries = 0;      // counter for upper limit of number of tries
        int inc;                    // randomly picked increment (-1 or 1)
        Ship ship;                  // randomly picked ship to be moved

        while (c < 1000 || numberOfTries > 10000) {
            numberOfTries++;
            ship = ships.get(rand.nextInt(ships.size()));
            tmp = rand.nextInt(3);
            inc = 2 * rand.nextInt(2) - 1;
            // randomly pick to move randomly in row or col direction or rotate the ship
            if (tmp == 0) {
                if (moveShip(ship, ship.getRow() + inc, ship.getCol()))
                    c++;
            } else if (tmp == 1) {
                if (moveShip(ship, ship.getRow(), ship.getCol() + inc))
                    c++;
            } else {
                if (rotateShip(ship))
                    c++;
            }
        }
        return true;
    }

    /**
     * Starts a new game.
     *
     * @param ships an array of ships
     *
     * @return true if a new game was started
     */
    public boolean newGame(Ship[] ships) {

        clearBoard();

        return placeAllShipsRandomly(ships);

    }

    /**
     * A basic component of the <code>Board</code>
     * Holds information such as hit, occupied and ship occupying the square.
     */
    public class Square {

        private boolean hit = false;
        private boolean occupied = false;
        private int row, col;
        private Ship ship;

        private Square(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public boolean isHit() {
            return hit;
        }

        /**
         * Copy getter
         *
         * @return a copy of this square
         */
        public Square getCopy() {
            Square square = new Square(this.row, this.col);
            square.setHit(hit);
            square.setOccupied(occupied);
            if (ship != null)
                square.setShip(ship.getCopy());
            return square;
        }

        /**
         * Setter
         *
         * @param hit true if square is hit
         */
        private void setHit(boolean hit) {
            this.hit = hit;
        }

        /**
         * Getter
         *
         * @return true if the square is occupied
         */
        public boolean isOccupied() {
            return occupied;
        }

        /**
         * Setter
         *
         * @param occupied true if square is occupied
         */
        private void setOccupied(boolean occupied) {
            this.occupied = occupied;
        }

        /**
         * Getter
         *
         * @return a reference to the ship
         */
        public Ship getShip() {
            return ship;
        }

        /**
         * Setter
         *
         * @param ship the ship
         */
        private void setShip(Ship ship) {
            this.ship = ship;
        }

    }
}
