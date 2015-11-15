package model;

/**
 * @author Tor Gammelgard
 * @version 2015-10-17
 */
public class Ship {

    private static int id_counter = 0;

    public enum ShipType {
        CARRIER(6),
        BATTLESHIP(4),
        SUBMARINE(3),
        PATROL_BOAT(2);

        private int length;

        ShipType(int length) {
            this.length = length;
        }

        public int getLength() {
            return length;
        }
    }

    private ShipType shipType;              // what kind of ship this is
    private int row, col;                   // coordinates of the ship's 'head'
    private Direction direction;            // placement direction of the ship
    private int hits;
    private boolean isSunk;
    private int id;

    private Ship() {
        id = id_counter++;
    }

    public static Ship createShip(ShipType shipType) {
        Ship s = new Ship();
        s.shipType = shipType;
        s.hits = 0;
        s.setDirection(Direction.RIGHT);         // default direction
        return s;
    }

    public static Ship createCopy(Ship ship) {
        Ship s = new Ship();
        s.shipType = ship.getShipType();
        s.hits = 0;
        s.setDirection(ship.getDirection());
        s.isSunk = ship.isSunk();
        return s;
    }

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

    public ShipType getShipType() {
        return shipType;
    }

    public int getId(){return id;}

    public void setLocation(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void addHit() {
        hits++;
        if (hits == getLength())
            isSunk = true;
    }

    public int getHits() {
        return hits;
    }

    public boolean isSunk() {
        return isSunk;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getLength() {
        return shipType.getLength();
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

}
