package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * A component for displaying some overlay effects.
 * Used as the glassPane in MainFrame.
 *
 * @author Tor Gammelgard
 * @version 2015-11-02
 * @see MainFrame
 */
public class FXPane extends JComponent {

    // Private fields

    private String message;
    private Timer showTextTimer;
    private Timer xHairTimer;
    private JLabel outputLabel;
    private Component parent;
    private double height, width;
    private Font font;
    private float fontSize;
    private boolean showCrossHair;
    private Point mousePos;
    private double scale = 0.0;
    private double scaleInc = 0.01;
    private double theta = 0.0;
    private double ringScale = 0.0;
    private BufferedImage backupBImg;

    public FXPane(Component parent) {
        this.parent = parent;

        showCrossHair = false;
        font = new Font("Serif", Font.BOLD, 150);

        showTextTimer = new Timer(20, e -> {
            font = font.deriveFont(fontSize);
            outputLabel.setFont(font);
            Color fColor = outputLabel.getForeground();
            fontSize--;
            if (outputLabel.getForeground().getAlpha() - 4 >= 0) {
                outputLabel.setForeground(new Color(0, 0, 0, fColor.getAlpha() - 4));
            } else {
                showTextTimer.stop();
            }
            updateLabel();
        }) {
            @Override
            public void start() {
                reset();
                outputLabel.setVisible(true);
                super.start();
            }

            @Override
            public void stop() {
                super.stop();
                outputLabel.setVisible(false);
            }
        };

        xHairTimer = new Timer(16, e -> {
            scale = scale + scaleInc;
            if (scale < 0 || scale > 1)
                scaleInc *= -1;
            ringScale = ringScale + 0.01;
            if (ringScale > 1)
                ringScale = 0.0;

            theta = theta + 0.04;
            if (theta > 2 * Math.PI)
                theta = 0.0;
            repaint();

        });

        outputLabel = new JLabel(message);
        add(outputLabel);
    }

    /**
     * Resets the output label.
     */
    private void reset() {
        this.fontSize = 150;
        outputLabel.setFont(this.font);
        width = parent.getWidth();
        height = parent.getHeight();
        outputLabel.setHorizontalAlignment(SwingConstants.CENTER);
        outputLabel.setForeground(Color.BLACK);
        updateLabel();
    }

    /**
     * Updates the bounds of the output label.
     */
    private void updateLabel() {
        outputLabel.setBounds((int) ((parent.getWidth() - width) / 2), (int) ((parent.getHeight() - height) / 2), (int) width, (int) height);
    }

    /**
     * Starts an animation which outputs the message in the middle of the screen.
     *
     * @param message Text message to be shown.
     */
    public void showMessage(String message) {
        this.message = message;
        outputLabel.setText(message);
        showTextTimer.start();
    }

    /**
     * @param mousePos current mouse position.
     */
    public void setMousePos(Point mousePos) {
        this.mousePos = mousePos;
        repaint();
    }

    /**
     * Sets the <code>showCrossHair</code> if it hasn't been set already and starts the animation
     * (i.e. <code>xHairTimer</code>).
     *
     * @param showCrossHair the new <code>showCrossHair</code> of this component
     */
    public void setShowCrossHair(boolean showCrossHair) {
        if (showCrossHair != this.showCrossHair) {
            this.showCrossHair = showCrossHair;
            repaint();
            if (showCrossHair) {
                if (!xHairTimer.isRunning()) {
                    xHairTimer.start();
                }
            } else if (xHairTimer.isRunning()) {
                xHairTimer.stop();
            }
        }
    }

    /**
     * Adds an Explosion object around the mouse position.
     *
     * @param hit true if a ship was hit
     */
    public void doHitAnimation(boolean hit) {
        if (mousePos != null) {
            Explosion explosion;
            explosion = new Explosion(this, mousePos.x - 100, mousePos.y - 100, 200, 200, hit);
            add(explosion);
        }
    }

    /**
     * Animates an aim cursor, resembling a radar, by rotation, AlphaComposite and saving to a backup buffered image.
     *
     * @param g2 graphics
     */
    private void updateBigAim(Graphics2D g2) {
        if (mousePos == null)
            return;

        int xhairRadius = 60;

        BufferedImage tempBuff = new BufferedImage(2 * xhairRadius, 2 * xhairRadius, BufferedImage.TYPE_INT_ARGB);

        Graphics2D tempGraphics = tempBuff.createGraphics();
        tempGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.98f));

        // gray background disc with circles
        g2.setPaint(new Color(50, 50, 50, 50));
        g2.fillArc(mousePos.x - xhairRadius, mousePos.y - xhairRadius, 2 * xhairRadius, 2 * xhairRadius, 0, 360);
        g2.setPaint(Color.DARK_GRAY);
        g2.setStroke(new BasicStroke(1));
        g2.drawArc((int) (mousePos.x - ringScale * xhairRadius / 2), (int) (mousePos.y - ringScale * xhairRadius / 2), (int) (ringScale * xhairRadius), (int) (ringScale * xhairRadius), 0, 360);
        g2.drawArc((int) (mousePos.x - ringScale * xhairRadius), (int) (mousePos.y - ringScale * xhairRadius), (int) (ringScale * 2 * xhairRadius), (int) (ringScale * 2 * xhairRadius), 0, 360);

        AffineTransform at = new AffineTransform();
        at.rotate(0.001, xhairRadius, xhairRadius);

        tempGraphics.drawImage(backupBImg, at, null);

        tempGraphics.setStroke(new BasicStroke(10));
        tempGraphics.setColor(new Color(25, 25, 25, 15));
        tempGraphics.rotate(-theta, xhairRadius, xhairRadius);
        tempGraphics.fillRect(xhairRadius - 5, (int) (scale * 50) + xhairRadius, 10, xhairRadius - 50);

        // green rotating thing
        tempGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        tempGraphics.rotate(-theta + .2, xhairRadius, xhairRadius);
        tempGraphics.setPaint(new Color(120, 221, 17));
        tempGraphics.fillRect(xhairRadius - 5, xhairRadius, 10, xhairRadius);

        g2.drawImage(tempBuff, mousePos.x - xhairRadius, mousePos.y - xhairRadius, null);

        backupBImg = tempBuff;
        tempGraphics.dispose();
    }

    /**
     * Sets some rendering hints and updates the big aim cursor animation
     *
     * @param g graphics
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (showCrossHair) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
            g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
            updateBigAim(g2);
        }
    }

}
