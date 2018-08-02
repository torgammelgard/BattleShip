package view;

import model.Ship;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A panel for displaying information about the ships in the game.
 *
 * @author Tor Gammelgard
 * @version 2015-10-26
 */
public class ShipsStatusPanel extends JPanel {

    public static final int WIDTH = 300;
    public static final int HEIGHT = 200;

    private static final int SCALEFACTOR = 4;
    private BufferedImage carrier = createBuffImg(new ImageIcon("src/main/java/res/carrier.png", "carrier"));
    private BufferedImage battleship = createBuffImg(new ImageIcon("src/main/java/res/battleship.png", "battleship"));
    private BufferedImage submarine = createBuffImg(new ImageIcon("src/main/java/res/submarine.png", "submarine"));
    private BufferedImage patrolboat = createBuffImg(new ImageIcon("src/main/java/res/patrolboat.png", "patrolboat"));

    private BufferedImage carrierFire = createBuffImg(new ImageIcon("src/main/java/res/carrierFire.png", "carrierfire"));
    private BufferedImage battleshipFire = createBuffImg(new ImageIcon("src/main/java/res/battleshipFire.png", "battleshipfire"));
    private BufferedImage submarineFire = createBuffImg(new ImageIcon("src/main/java/res/submarineFire.png", "submarinefire"));
    private BufferedImage patrolboatFire = createBuffImg(new ImageIcon("src/main/java/res/patrolboatFire.png", "patrolboatfire"));

    private ArrayList<Ship> ships;
    private HashMap<Integer, ShipLabel> shipLabels;

    public ShipsStatusPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setMinimumSize(new Dimension(WIDTH, HEIGHT));
        this.ships = new ArrayList<>();
        this.shipLabels = new HashMap<>();
        update(ships);
    }

    /**
     * Removes all ships from the panel and clears the ship map.
     * Adds all new ships.
     *
     * @param ships an array of ships to be added
     */
    public void update(ArrayList<Ship> ships) {

        removeAll();
        this.ships.clear();
        this.shipLabels.clear();

        this.ships = ships;
        int rows = ships.size() / 2;
        setLayout(new GridLayout(rows, 2));

        ShipLabel shipLabel = new ShipLabel();

        for (Ship ship : ships) {
            if (ship.getShipType().equals(Ship.ShipType.CARRIER))
                shipLabel = new ShipLabel(carrier);
            if (ship.getShipType().equals(Ship.ShipType.BATTLESHIP))
                shipLabel = new ShipLabel(battleship);
            if (ship.getShipType().equals(Ship.ShipType.SUBMARINE))
                shipLabel = new ShipLabel(submarine);
            if (ship.getShipType().equals(Ship.ShipType.PATROL_BOAT))
                shipLabel = new ShipLabel(patrolboat);

            add(shipLabel);
            shipLabels.put(ship.getId(), shipLabel);
        }
        revalidate();
        repaint();
    }

    /**
     * Updates the <code>ShipLabel</code> corresponding to the hit ship.
     *
     * @param hitShip a ship that was hit
     */
    public void update(Ship hitShip) {
        if (hitShip.getHits() == hitShip.getLength()) {
            if (hitShip.getShipType().equals(Ship.ShipType.CARRIER))
                shipLabels.get(hitShip.getId()).setImage(carrierFire);
            if (hitShip.getShipType().equals(Ship.ShipType.BATTLESHIP))
                shipLabels.get(hitShip.getId()).setImage(battleshipFire);
            if (hitShip.getShipType().equals(Ship.ShipType.SUBMARINE))
                shipLabels.get(hitShip.getId()).setImage(submarineFire);
            if (hitShip.getShipType().equals(Ship.ShipType.PATROL_BOAT))
                shipLabels.get(hitShip.getId()).setImage(patrolboatFire);
        }
        shipLabels.get(hitShip.getId()).setHitsInfo(hitShip.getHits() + "/" + hitShip.getLength());
    }

    /**
     * Creates a buffered image to be used in <code>ShipLabel</code>.
     *
     * @param imageIcon a holder for the image to be used
     * @return a scaled image of the passed in image
     */
    private BufferedImage createBuffImg(ImageIcon imageIcon) {
        Image image = imageIcon.getImage();

        BufferedImage bi = new BufferedImage(image.getWidth(null) / SCALEFACTOR, image.getHeight(null) / SCALEFACTOR, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bi.createGraphics();
        g2.drawImage(image, AffineTransform.getScaleInstance(1.0 / SCALEFACTOR, 1.0 / SCALEFACTOR), null);
        g2.dispose();

        return bi;
    }

    /**
     * A customized label for displaying the status of a ship.
     */
    private class ShipLabel extends JLabel {

        private String hitsInfo = "default";

        private ShipLabel() {
            super();
        }

        private ShipLabel(BufferedImage shipBuffImg) {
            super(new ImageIcon(shipBuffImg));
            setVerticalTextPosition(BOTTOM);
            setHorizontalTextPosition(CENTER);
            setFont(new Font("Serif", Font.BOLD, 24));

            // writes the default text with invisible ink
            // to set the labels size from the start.
            setForeground(new Color(0, 0, 0, 0));
            setText(hitsInfo);
        }

        /**
         * Sets the text of the label.
         *
         * @param hitsInfo information about the hits
         */
        public void setHitsInfo(String hitsInfo) {
            this.hitsInfo = hitsInfo;
            String[] s_arr = hitsInfo.split("/");
            if (s_arr[0].equals(s_arr[1]))
                setForeground(Color.RED);
            else
                setForeground(Color.BLACK);
            setText(hitsInfo);
        }

        /**
         * @param shipBuffImg image to be set as icon
         */
        private void setImage(BufferedImage shipBuffImg) {
            setIcon(new ImageIcon(shipBuffImg));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
    }
}
