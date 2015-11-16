package model;

/**
 * A model of the ship.
 *
 * @author Tor Gammelgard
 * @version 2015-10-17
 */
public class Ship {

    /** An ID counter used with the creation of new ships. */
    private static int id_counter = 0;

    /**
     * An enum for the different ship types.
     */
    public enum ShipType {
        CARRIER(6),
        BATTLESHIP(4),
        SUBMARINE(3),
        PATROL_BOAT(2);

        private int length;

        ShipType(int length) {
            this.length = length;
        }

        /**
         * @return the ship's length
         */
        public int getLength() {
            return length;
        }
    }

    /** What kind of ship this is */
    private ShipType shipType;

    /** coordinates of the ship's 'head' */
    private int row, col;

    /** placement direction of the ship */
    private Direction direction;

    /** a hit counter */
    private int hits;

    /** a bool for keeping track of the ship's status */
    private boolean isSunk;

    /** A unique ID */
    private int id;

    /** Private constructor */
    private Ship() {
        id = id_counter++;
    }

    /** Static initialization block */
    public static Ship createShip(ShipType shipType) {
        Ship s = new Ship();
        s.shipType = shipType;
        s.hits = 0;
        s.setDirection(Direction.RIGHT);         // default direction
        return s;
    }

    /**
     * Copy getter
     *
     * @return a copy of this ship
     */
    public Ship getCopy() {
        Ship s = createShip(this.getShipType());
        s.row = this.row;
        s.col = this.col;
        s.direction = this.direction;
        s.hits = this.hits;
        s.isSunk = this.isSunk;
        s.id = this.id;
        return s;
    }

    /**
     * Getter
     *
     * @return the ship type
     */
    public ShipType getShipType() {
        return shipType;
    }

    /**
     * Getter
     *
     * @return the ship's id
     */
    public int getId() {
        return id;
    }

    /**
     * Setter
     *
     * @param row the row index of the new location
     * @param col the column index of the new location
     */
    public void setLocation(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Adds a hit and checks if the ship sank.
     */
    public void addHit() {
        hits++;
        if (hits == getLength())
            isSunk = true;
    }

    /**
     * Getter
     *
     * @return the number of hits to the ship
     */
    public int getHits() {
        return hits;
    }

    /**
     * Getter
     *
     * @return true if the ship is sunk
     */
    public boolean isSunk() {
        return isSunk;
    }

    /**
     * Getter
     *
     * @return the row index of the ship's position
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter
     *
     * @return the column index of the ship's position
     */
    public int getCol() {
        return col;
    }

    /**
     * Getter
     *
     * @return the ship's length
     */
    public int getLength() {
        return shipType.getLength();
    }

    /**
     * Getter
     *
     * @return the direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Setter
     *
     * @param direction the new direction
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

}
