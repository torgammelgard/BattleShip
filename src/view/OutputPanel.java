package view;

import javax.swing.*;
import java.awt.*;


/**
 * A panel for displaying information about missed shots.
 *
 * @author Tor Gammelgard
 * @version 2015-10-18
 * @see MainFrame
 */
public class OutputPanel extends JTextField {

    public OutputPanel() {
        setEditable(false);
        setFont(new Font("SansSerif", Font.BOLD, 20));
        setForeground(MainFrame.FOREGROUND_COLOR);
        setBackground(MainFrame.BACKGROUND_COLOR);
        setPreferredSize(new Dimension(400, 50));
    }
}
