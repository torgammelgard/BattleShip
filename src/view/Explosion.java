package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

/**
 * An explosion animation
 *
 * @author Tor Gammelgard
 * @version 2015-11-09
 * @see FXPane
 */
public class Explosion extends JComponent implements ActionListener {

    private JComponent parent;
    private int counter = 0;
    private Timer timer;
    private ArrayList<Particle> particles;

    public Explosion(JComponent parent, int x, int y, int w, int h, boolean hit) {
        this.parent = parent;
        setBounds(x, y, w, h);
        Point midPoint = new Point(w / 2, h / 2);
        particles = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            particles.add(new Particle(midPoint, hit));
            add(particles.get(particles.size() - 1));
        }
        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (Particle particle : particles) {
            particle.update();
        }
        if (counter++ > 100) {
            parent.remove(this);
            particles.clear();
            timer.stop();
            parent.repaint();
        }
    }

    /**
     * A particle is a sub component of an <code>Explosion</code>.
     */
    class Particle extends JComponent {
        Color color;
        double x, y;
        double vx, vy;
        double theta;

        Particle(Point p, boolean hit) {
            Random random = new Random();
            vx = random.nextDouble() - 0.5;
            vy = random.nextDouble() - 0.5;
            int w = random.nextInt(11) + 10;
            int h = random.nextInt(11) + 10;
            this.x = p.x;
            this.y = p.y;
            theta = (random.nextDouble() - 0.5) / 10;
            setBounds((int) x, (int) y, w, h);
            if (hit)
                color = new Color(random.nextInt(101) + 155, 0, 0);
            else {
                int colorComp = random.nextInt(120);
                color = new Color(colorComp, colorComp, colorComp);
            }
        }

        /**
         * Updates the position and lowers the alpha component.
         */
        void update() {
            x += vx;
            y += vy;
            theta += 0.05;
            int a = color.getAlpha() - 2;
            if (a < 0)
                a = 0;
            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), a);
            setLocation((int) x, (int) y);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(color);
            g2.rotate(theta, getWidth() / 2, getHeight() / 2);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}