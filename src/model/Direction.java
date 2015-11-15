package model;

/**
 * An enumeration for the direction of a ship.
 *
 * @author Tor Gammelgard
 * @version 2015-10-17
 * @see Ship
 */
public enum Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    /**
     * Cyclic getNext method
     */
    public Direction getNext() {
        return values()[(ordinal() + 1) % values().length];
    }
}
