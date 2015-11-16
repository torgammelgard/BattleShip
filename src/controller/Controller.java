package controller;

import helpers.GradeLevel;
import helpers.ShowLevel;
import model.Ship;
import view.BoardPanel;
import view.MainFrame;
import view.OutputPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This controller handles user events for example mouse and key events.
 *
 * @author Tor Gammelgard
 * @version 2015-10-15
 */
public class Controller implements MouseMotionListener, MouseListener, ActionListener, ItemListener {

    /**
     * State - the current state of the program (package modifier since it should be reached from GameDriver also)
     */
    enum State {
        NORMAL, WAITING_FOR_PLAYER_TO_MOVE
    }

    private State state;

    // other controller part
    private GameDriver gameDriver;

    // view
    private MainFrame mainFrame;
    private OutputPanel outputPanel;

    public Controller(MainFrame mainFrame) {
        this.gameDriver = new GameDriver(this);
        this.mainFrame = mainFrame;
        this.outputPanel = mainFrame.getOutputPanel();
        this.state = State.NORMAL;
        setState(State.NORMAL);
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public GameDriver getGameDriver() {
        return gameDriver;
    }

    public ShowLevel getShowLevel() {
        return mainFrame.isShowSelected() ? ShowLevel.SHOW : ShowLevel.HIDE;
    }

    void setState(State state) {
        this.state = state;
    }

    State getState() {
        return state;
    }

    public void updateMissedPanel(String s) {
        outputPanel.setText(s);
    }

    public void updateShipsStatusPanel(Ship ship) {
        getMainFrame().getShipsStatusPanel().update(ship);
    }

    // START - Implementation of MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        BoardPanel.SquareLabel sq;
        if (e.getSource().getClass().equals(BoardPanel.SquareLabel.class))
            sq = (BoardPanel.SquareLabel) e.getSource();
        else
            return;

        if (state.equals(State.WAITING_FOR_PLAYER_TO_MOVE)) {
            gameDriver.squareClicked(sq);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

        if (e.getSource().getClass().equals(BoardPanel.SquareLabel.class)) {
            // set cursor
            getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

            // set cross hair animation
            getMainFrame().getFXPane().setShowCrossHair(true);
        } else {
            getMainFrame().setCursor(Cursor.getDefaultCursor());

            getMainFrame().getFXPane().setShowCrossHair(false);
        }

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    // END - Implementation of MouseListener

    // START - Implementation of MouseMotionListener
    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (state.equals(State.WAITING_FOR_PLAYER_TO_MOVE)) {
            Point convertPoint = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), getMainFrame().getFXPane());
            getMainFrame().getFXPane().setMousePos(new Point(convertPoint));
        } else {
            getMainFrame().getFXPane().setShowCrossHair(false);
        }
    }
    // END - Implementation of MouseMotionListener


    /**
     * Starts a new game
     */
    private void startrestart() {
        if (!mainFrame.isGradeSelected()) {
            if (gameDriver.startGame(GradeLevel.GODKAND))
                setState(State.WAITING_FOR_PLAYER_TO_MOVE);
        } else {
            if (gameDriver.startGame(GradeLevel.VAL_GODKAND))
                setState(State.WAITING_FOR_PLAYER_TO_MOVE);
        }
    }

    /**
     * ActionListener implementation
     *
     * @param e an ActionEvent
     *
     * @see MainFrame
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(MainFrame.START_RESTART)) {
            startrestart();
        }
    }

    /**
     * ItemListener implementation
     * Used for JComboBox
     *
     * @param e - an ItemEvent
     *
     * @see MainFrame
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            // check if "show/hide" was changed
            if (((MainFrame.MyCheckBox) e.getSource()).getText().equals("Show/Hide")) {
                mainFrame.getBoardPanel().boardChanged();
            }
            // check if "grade G" is selected
            else if (((MainFrame.MyCheckBox) e.getSource()).getText().equals("VG")) {
                startrestart();
            }
        }
        if (e.getStateChange() == ItemEvent.DESELECTED) {
            if (((MainFrame.MyCheckBox) e.getSource()).getText().equals("Show/Hide")) {
                mainFrame.getBoardPanel().boardChanged();
            } else if (((MainFrame.MyCheckBox) e.getSource()).getText().equals("VG")) {
                startrestart();
            }
        }
    }
}
