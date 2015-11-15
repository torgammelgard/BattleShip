package view;

import controller.BoardListener;
import controller.Controller;
import helpers.ShowLevel;
import model.Board;
import model.IBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A panel for showing the game board.
 *
 * @author Tor Gammelgard
 * @version 2015-10-15
 * @see BoardListener
 */
public class BoardPanel extends JPanel implements BoardListener {

    public static final int SQUARE_WIDTH = 70;
    public static int rows, columns;

    private Random random = new Random();
    private IBoard board;
    private Controller controller;
    private List<SquareLabel> squareLabelList;

    public BoardPanel(IBoard board, Controller controller) {

        setBackground(MainFrame.BACKGROUND_COLOR);
        this.board = board;
        this.controller = controller;

        rows = Board.ROWS;
        columns = Board.COLUMNS;

        setLayout(new GridLayout(rows, columns));

        squareLabelList = new ArrayList<>(rows * columns);

        SquareLabel s;
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++) {
                s = new SquareLabel(i, j);
                s.addMouseListener(controller);
                s.addMouseMotionListener(controller);
                s.setOpaque(true);
                squareLabelList.add(s);
                add(s);
            }
        board.addListener(this);
        setPreferredSize(new Dimension(rows * SQUARE_WIDTH, columns * SQUARE_WIDTH));
    }

    @Override
    public void boardChanged() {
        for (SquareLabel squareLabel : squareLabelList)
            squareLabel.update();
    }

    @Override
    public void squareChanged(int row, int col) {
        squareLabelList.get(row * rows + col).update();
    }

    /**
     * A customized label for displaying a square on the board.
     */
    public class SquareLabel extends JLabel {

        private BufferedImage waterBackground;
        private BufferedImage shipBackground;
        private BufferedImage waterMissBackground;
        private BufferedImage shipHitBackground;

        private int row, col;

        public SquareLabel(int row, int col) {
            this.row = row;
            this.col = col;
            waterBackground = createWaterBackground();
            shipBackground = createShipBackground();
            waterMissBackground = createWaterMissBackground();
            shipHitBackground = createShipHitBackground();
            setPreferredSize(new Dimension(SQUARE_WIDTH, SQUARE_WIDTH));
            reset();
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        /**
         * Resets the background image.
         */
        public void reset() {
            setIcon(new ImageIcon(waterBackground));
        }

        /**
         * Updates the current
         */
        public void update() {
            Board.Square sq = board.getSquare(row, col);

            if (controller.getShowLevel().equals(ShowLevel.SHOW)) {
                if (sq.isOccupied() && sq.isHit()) {
                    setIcon(new ImageIcon(shipHitBackground));
                } else if (sq.isOccupied()) {
                    setIcon(new ImageIcon(shipBackground));
                } else if (sq.isHit()) {
                    setIcon(new ImageIcon(waterMissBackground));
                } else {
                    setIcon(new ImageIcon(waterBackground));
                }
            } else {
                if (sq.isOccupied() && sq.isHit()) {
                    setIcon(new ImageIcon(shipHitBackground));
                } else if (!sq.isOccupied() && sq.isHit()) {
                    setIcon(new ImageIcon(waterMissBackground));
                } else {
                    setIcon(new ImageIcon(waterBackground));
                }
            }
        }


        private BufferedImage createWaterBackground() {
            ImageIcon waterbgImgIcon;
            waterbgImgIcon = new ImageIcon("src/res/waterbg.jpg", "waterbg");
            waterBackground = new BufferedImage(SQUARE_WIDTH * rows, SQUARE_WIDTH * columns, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = waterBackground.createGraphics();
            waterbgImgIcon.paintIcon(null, g2, 0, 0);
            g2.dispose();

            return waterBackground.getSubimage(SQUARE_WIDTH * col, SQUARE_WIDTH * row, SQUARE_WIDTH, SQUARE_WIDTH);
        }

        private BufferedImage createShipBackground() {
            BufferedImage buff = new BufferedImage(waterBackground.getWidth(), waterBackground.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = buff.createGraphics();
            g2.drawImage(waterBackground, 0, 0, null);
            g2.setPaint(Color.BLACK);
            g2.fillArc(buff.getWidth() / 5, buff.getHeight() / 5, buff.getWidth() * 3 / 5, buff.getHeight() * 3 / 5, 0, 360);
            g2.dispose();
            return buff;
        }

        private BufferedImage createWaterMissBackground() {
            BufferedImage buff = new BufferedImage(waterBackground.getWidth(), waterBackground.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = buff.createGraphics();
            g2.drawImage(waterBackground, 0, 0, null);

            Color color;
            Random random = new Random();

            color = new Color(100, 100, 100, 80);
            g2.setPaint(color);
            g2.fillRect(0, 0, buff.getWidth(), buff.getHeight());
            for (int i = 0; i < 10; i++) {
                int gr = random.nextInt(100);
                color = new Color(gr, gr, 100, 125);
                g2.setPaint(color);
                g2.rotate(random.nextDouble(), buff.getWidth() / 2, buff.getHeight() / 2);
                g2.fillArc(buff.getWidth() / 2, buff.getHeight() / 2, random.nextInt(35), random.nextInt(35), 0, 360);
                g2.rotate(random.nextDouble(), buff.getWidth() / 2, buff.getHeight() / 2);
                g2.fillRect(buff.getWidth() / 2, buff.getHeight() / 2, random.nextInt(35), random.nextInt(35));
            }
            g2.dispose();
            return buff;
        }

        private BufferedImage createShipHitBackground() {
            BufferedImage buff = new BufferedImage(waterBackground.getWidth(), waterBackground.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = buff.createGraphics();
            g2.drawImage(shipBackground, 0, 0, null);

            Color color;
            color = new Color(random.nextInt(101) + 155, 0, 0, 125);

            g2.setPaint(color);
            g2.fillRect(0, 0, buff.getWidth(), buff.getHeight());
            for (int i = 0; i < 25; i++) {
                g2.setPaint(new Color(255, random.nextInt(200), 0, 150));
                g2.rotate(random.nextDouble(), buff.getWidth() / 2, buff.getHeight() / 2);
                g2.fillArc(buff.getWidth() / 2, buff.getHeight() / 2, random.nextInt(35), random.nextInt(35), 0, 360);
            }
            g2.dispose();
            return buff;
        }
    }
}