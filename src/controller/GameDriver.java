package controller;

import helpers.GradeLevel;
import model.Board;
import model.Ship;
import view.BoardPanel;

/**
 * @author Tor Gammelgard
 * @version 2015-10-18
 */
public class GameDriver {

    private Controller controller;

    private int missedShots;

    private Board board;

    private Ship[] createShipsForGradeG() {
        return new Ship[]{Ship.createShip(Ship.ShipType.SUBMARINE)};
    }

    private Ship[] createShipsForGradeVG() {
        Ship[] ships = new Ship[]{
                Ship.createShip(Ship.ShipType.CARRIER),
                Ship.createShip(Ship.ShipType.BATTLESHIP),
                Ship.createShip(Ship.ShipType.BATTLESHIP),
                Ship.createShip(Ship.ShipType.SUBMARINE),
                Ship.createShip(Ship.ShipType.SUBMARINE),
                Ship.createShip(Ship.ShipType.SUBMARINE),
                Ship.createShip(Ship.ShipType.PATROL_BOAT),
                Ship.createShip(Ship.ShipType.PATROL_BOAT),
                Ship.createShip(Ship.ShipType.PATROL_BOAT),
                Ship.createShip(Ship.ShipType.PATROL_BOAT)
        };
        return ships;
    }

    public GameDriver(Controller controller) {
        this.controller = controller;
        board = new Board();
    }

    public Board getBoard() {
        return board;
    }

    public boolean startGame(GradeLevel gradeLevel) {
        if (gradeLevel == GradeLevel.GODKAND) {
            if (!board.newGame(createShipsForGradeG()))
                return false;
        } else if (gradeLevel == GradeLevel.VAL_GODKAND)
            if (!board.newGame(createShipsForGradeVG()))
                return false;

        missedShots = 0;
        controller.getMainFrame().getBoardPanel().boardChanged();
        controller.getMainFrame().getShipsStatusPanel().update(board.getShips());
        controller.updateMissedPanel("Missed shots : 0");
        controller.getMainFrame().getFXPane().showMessage("Good luck!");
        return true;
    }

    public void squareClicked(BoardPanel.SquareLabel sq) {

        // set the square and check if it was a hit or miss
        if (!board.setSquare(sq.getRow(), sq.getCol()))
            return;

        Board.Square boardSq = board.getSquare(sq.getRow(), sq.getCol());

        // these views are updated through this controller, instead of using listeners
        int numDestroyedShips = 0;
        if (boardSq.isOccupied()) {
            controller.getMainFrame().getFXPane().doHitAnimation(true);
            if (boardSq.getShip().isSunk())
                controller.getMainFrame().shakeWindow();
            for (Ship ship : board.getShips()) {
                if (ship.isSunk())
                    numDestroyedShips++;

            }

            // end game
            if (numDestroyedShips == board.getShips().size()) {
                System.out.println("All ships destroyed!");
                controller.getMainFrame().getFXPane().showMessage("Victory!");
                controller.setState(Controller.State.NORMAL);
            }
            controller.updateShipsStatusPanel(boardSq.getShip().getCopy());
        } else {
            String s = String.format("Missed shots : %d. ", ++missedShots);
            controller.updateMissedPanel(s);
            controller.getMainFrame().getFXPane().doHitAnimation(false);
        }

    }
}
