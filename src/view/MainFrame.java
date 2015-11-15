package view;

import controller.Controller;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * Main frame and top container of the application.
 *
 * @author Tor Gammelgard
 * @version 2015-10-15
 */

public class MainFrame extends JFrame {

    public static final Color BACKGROUND_COLOR = new Color(97, 131, 98);
    public static final Color FOREGROUND_COLOR = new Color(8, 9, 67);

    public static final String START_RESTART = "startRestart";

    private BoardPanel boardPanel;
    private OutputPanel outputPanel;
    private JButton startRestartButton;
    private MyCheckBox showCheckBox;
    private MyCheckBox gradeCheckBox;
    private ShipsStatusPanel shipsStatusPanel;
    private FXPane FXPane;

    public MainFrame() throws HeadlessException {
        final SplashScreen splashScreen = SplashScreen.getSplashScreen();
        if (splashScreen == null) {
            System.out.println("No splash");
            return;
        }
        Graphics2D g2 = splashScreen.createGraphics();
        if (g2 == null) {
            System.out.println("no graphics");
            return;
        }

        outputPanel = new OutputPanel();
        outputPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        Controller controller = new Controller(this);
        boardPanel = new BoardPanel(controller.getGameDriver().getBoard(), controller);
        boardPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        startRestartButton = new RoundRedButton();
        startRestartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        showCheckBox = new MyCheckBox("Show/Hide");
        showCheckBox.addItemListener(controller);
        gradeCheckBox = new MyCheckBox("VG");
        gradeCheckBox.addItemListener(controller);
        shipsStatusPanel = new ShipsStatusPanel();
        shipsStatusPanel.setBackground(Color.DARK_GRAY);
        shipsStatusPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        FXPane = new FXPane(this);
        setGlassPane(FXPane);
        getGlassPane().setVisible(true);

        // right panel
        JPanel right_panel = new JPanel();
        right_panel.setBackground(BACKGROUND_COLOR);
        right_panel.setLayout(new BoxLayout(right_panel, BoxLayout.PAGE_AXIS));
        right_panel.add(shipsStatusPanel);
        right_panel.add(Box.createVerticalStrut(15));

        JPanel p = new JPanel();
        p.add(showCheckBox);
        p.setPreferredSize(new Dimension(300, 75));
        p.setMinimumSize(new Dimension(300, 75));
        p.setMaximumSize(new Dimension(300, 75));
        p.setBackground(BACKGROUND_COLOR);

        right_panel.add(p);

        p.add(gradeCheckBox);

        right_panel.add(Box.createVerticalStrut(15));
        right_panel.add(startRestartButton);
        right_panel.add(Box.createVerticalStrut(40));
        right_panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        add(boardPanel, BorderLayout.CENTER);
        add(outputPanel, BorderLayout.SOUTH);
        add(right_panel, BorderLayout.EAST);
        setFocusable(true);                                         // or keylistener doesn't respond

        // add listeners
        startRestartButton.setActionCommand(START_RESTART);
        startRestartButton.addActionListener(controller);
        addMouseListener(controller);
        addKeyListener(controller);

        setTitle("Battleship");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        splashScreen.close();

        setVisible(true);
    }

    /**
     * @return a reference to the <code>OutputPanel</code>
     */
    public OutputPanel getOutputPanel() {
        return outputPanel;
    }

    /**
     * @return a reference to the <code>ShipStatusPanel</code>
     */
    public ShipsStatusPanel getShipsStatusPanel() {
        return shipsStatusPanel;
    }

    /**
     * @return a reference to the <code>BoardPanel</code>
     */
    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    /**
     * @return a reference to the <code>FXPane</code>
     */
    public FXPane getFXPane() {
        return FXPane;
    }

    /**
     * @return true if grade checkbox is selected
     */
    public boolean isGradeSelected() {
        return gradeCheckBox.isSelected();
    }

    /**
     * @return true if the show checkbox is selected
     */
    public boolean isShowSelected() {
        return showCheckBox.isSelected();
    }

    /**
     * Shakes the window a short period of time.
     */
    public void shakeWindow() {
        Timer timer = new Timer(0, (e) -> {
            int c = 0;
            Point l = getLocation();
            while (c++ < 8) {
                if (c % 2 == 0)
                    setLocation(l.x - 2, l.y - 2);
                else
                    setLocation(l.x + 2, l.y + 2);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            setLocation(l);

        });
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * A custom checkbox
     */
    public class MyCheckBox extends JCheckBox {
        MyCheckBox(String s) {
            setFont(new Font("Serif", Font.BOLD, 24));
            setText(s);
            setFocusable(false);
            setIcon(new ImageIcon("src/res/checkbox.png"));
            setSelectedIcon(new ImageIcon("src/res/checkboxselected.png"));
            setContentAreaFilled(false);
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        }
    }

    /**
     * A custom button
     */
    public class RoundRedButton extends JButton {
        RoundRedButton() {
            setIcon(new ImageIcon("src/res/restartbutton.png"));
            setSelectedIcon(new ImageIcon("src/res/"));
            setPressedIcon(new ImageIcon("src/res/restartbuttonpressed.png"));
            setContentAreaFilled(false);
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        }

    }
}
