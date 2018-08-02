package controller;

import model.Board;

/**
 * The listener interface for receiving changes made to the <code>Board</code>.<br>
 * Classes that implements IBoard (for example Board) can add BoardListener and notify these when
 * changes to the board has been made.
 *
 * @author Tor Gammelgard
 * @version 2015-10-15
 *
 * @see model.IBoard
 * @see Board#addListener(BoardListener)
 * @see Board#removeListener(BoardListener)
 */
public interface BoardListener {

    /**
     * Invoked when the <code>Board</code> has changed.
     *
     * @see Board#fireBoardChanged()
     */
    void boardChanged();

    /**
     * Invoked when the <code>Board</code> has changed a square.
     *
     * @param row a row index
     * @param col a column index
     * @see Board#fireSquaredChanged(Board.Square)
     */
    void squareChanged(int row, int col);
}
