import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MyJPanel extends JPanel implements ActionListener {

    public final static int SCREEN_WIDTH = 1200;
    public final static int SCREEN_HEIGHT = 1200;
    public final static int UNIT_SIZE = SCREEN_WIDTH / Sudoku.SUDOKU_UNITS_IN_X;

    private final Sudoku sudoku = new Sudoku(this);

    public MyJPanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setFocusable(true);
        addKeyListener(new KeyAdapter() { //מבקש פרמטר אינטרפס מסוג KeyListener

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {

//                    case KeyEvent.VK_BACK_SPACE:
//                        sudoku.getCurrentTile().delHumanVal();
//                        repaint();
//                        break;

                    case KeyEvent.VK_LEFT:
                        if (sudoku.getCurrentTile().getSudokuCoordinateX() == 0)
                            break;
                        moveCourser(Direction.LEFT);
                        repaint();
                        break;

                    case KeyEvent.VK_RIGHT:
                        if (sudoku.getCurrentTile().getSudokuCoordinateX() == Sudoku.SUDOKU_UNITS_IN_X - 1) break;
                        moveCourser(Direction.RIGHT);
                        repaint();
                        break;

                    case KeyEvent.VK_UP:
                        if (sudoku.getCurrentTile().getSudokuCoordinateY() == 0)
                            break;
                        moveCourser(Direction.UP);
                        repaint();
                        break;

                    case KeyEvent.VK_DOWN:
                        if (sudoku.getCurrentTile().getSudokuCoordinateY() == Sudoku.SUDOKU_UNITS_IN_Y - 1) break;
                        moveCourser(Direction.DOWN);
                        repaint();
                        break;

                    case KeyEvent.VK_1:
                        setHumanValueInTile(sudoku.getCurrentTile(), 1);
                        repaint();
                        break;

                    case KeyEvent.VK_2:
                        setHumanValueInTile(sudoku.getCurrentTile(), 2);
                        repaint();
                        break;

                    case KeyEvent.VK_3:
                        setHumanValueInTile(sudoku.getCurrentTile(), 3);
                        repaint();
                        break;

                    case KeyEvent.VK_4:
                        setHumanValueInTile(sudoku.getCurrentTile(), 4);
                        repaint();
                        break;

                    case KeyEvent.VK_5:
                        setHumanValueInTile(sudoku.getCurrentTile(), 5);
                        repaint();
                        break;

                    case KeyEvent.VK_6:
                        setHumanValueInTile(sudoku.getCurrentTile(), 6);
                        repaint();
                        break;

                    case KeyEvent.VK_7:
                        setHumanValueInTile(sudoku.getCurrentTile(), 7);
                        repaint();
                        break;

                    case KeyEvent.VK_8:
                        setHumanValueInTile(sudoku.getCurrentTile(), 8);
                        repaint();
                        break;

                    case KeyEvent.VK_9:
                        setHumanValueInTile(sudoku.getCurrentTile(), 9);
                        repaint();
                        break;

                }
            }

        });


        setPreGameLevel2();

        JButton solve = new JButton("solve");
        solve.setPreferredSize(new Dimension(100, 50));
        solve.addActionListener(this);
        solve.setVisible(true);
        solve.setFocusable(false);
        add(solve);

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        drawGrid(g);
        sudoku.getCurrentTile().drawTile(g);
        sudoku.drawNumbers(g);
    }

    public void setPreGameLevel2() {
        // פעולה שתחסוך לי זמן ותצייר את המשבצות בשבילי
        Box[][] boxes = sudoku.getBoxes();

        boxes[0][0].getTiles()[1][1].setHumanValue(7);
        boxes[0][0].getTiles()[2][2].setHumanValue(4);


        boxes[0][1].getTiles()[1][0].setHumanValue(8);
        boxes[0][1].getTiles()[0][1].setHumanValue(2);
        boxes[0][1].getTiles()[0][2].setHumanValue(7);
        boxes[0][1].getTiles()[2][2].setHumanValue(3);


        boxes[0][2].getTiles()[1][0].setHumanValue(5);
        boxes[0][2].getTiles()[0][1].setHumanValue(4);
        boxes[0][2].getTiles()[1][1].setHumanValue(6);
        boxes[0][2].getTiles()[0][2].setHumanValue(1);


        boxes[1][0].getTiles()[0][0].setHumanValue(3);
        boxes[1][0].getTiles()[2][0].setHumanValue(1);
        boxes[1][0].getTiles()[2][1].setHumanValue(6);


        boxes[1][1].getTiles()[1][1].setHumanValue(3);


        boxes[1][2].getTiles()[0][1].setHumanValue(2);
        boxes[1][2].getTiles()[0][2].setHumanValue(8);
        boxes[1][2].getTiles()[2][2].setHumanValue(7);


        boxes[2][0].getTiles()[2][0].setHumanValue(9);
        boxes[2][0].getTiles()[1][1].setHumanValue(3);
        boxes[2][0].getTiles()[2][1].setHumanValue(4);
        boxes[2][0].getTiles()[1][2].setHumanValue(6);

        boxes[2][1].getTiles()[0][0].setHumanValue(3);
        boxes[2][1].getTiles()[2][0].setHumanValue(6);
        boxes[2][1].getTiles()[2][1].setHumanValue(7);
        boxes[2][1].getTiles()[1][2].setHumanValue(1);


        boxes[2][2].getTiles()[0][0].setHumanValue(8);
        boxes[2][2].getTiles()[1][1].setHumanValue(7);
    }

    public void drawGrid(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        for (int i = 0; i < 3; i++) {
            g2.setColor(new Color(255, 0, 0));//קווים של ציר הX לרוחב
            g2.setStroke(new BasicStroke(4));
            g2.drawLine(i * 3 * UNIT_SIZE, 0, i * 3 * UNIT_SIZE, SCREEN_HEIGHT);

            g2.setColor(Color.black);
            g2.setStroke(new BasicStroke(2));

            g2.drawLine((i * 3 + 1) * UNIT_SIZE, 0, (i * 3 + 1) * UNIT_SIZE, SCREEN_HEIGHT);
            g2.drawLine((i * 3 + 2) * UNIT_SIZE, 0, (i * 3 + 2) * UNIT_SIZE, SCREEN_HEIGHT);
        }


        for (int i = 0; i < 3; i++) {
            g2.setColor(new Color(236, 2, 2));//קווים של ציר הX לרוחב
            g2.setStroke(new BasicStroke(4));

            g2.drawLine(0, i * 3 * UNIT_SIZE, SCREEN_WIDTH, i * 3 * UNIT_SIZE);

            g2.setColor(Color.black);
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(0, (i * 3 + 1) * UNIT_SIZE, SCREEN_WIDTH, (i * 3 + 1) * UNIT_SIZE);
            g2.drawLine(0, (i * 3 + 2) * UNIT_SIZE, SCREEN_WIDTH, (i * 3 + 2) * UNIT_SIZE);

        }

    }

    public void setHumanValueInTile(Tile tile, int value) {
        if (value < 1 || value > 9) {
            throw new RuntimeException();
        } else tile.setHumanValue(value);

    }


    public void moveCourser(Direction direction) {
        sudoku.updateCurrentTile(direction);
    }

    public void solve() {
        sudoku.solve();
    }

    public void actionPerformed(ActionEvent e) {
        solve();
        this.requestFocusInWindow();
        repaint();
    }

}