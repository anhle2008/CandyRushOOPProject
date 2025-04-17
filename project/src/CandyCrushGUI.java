import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class CandyCrushGUI extends JFrame {
    private final int SIZE = 6;
    private final JButton[][] buttons = new JButton[SIZE][SIZE];
    private final char[] candies = {'A', 'B', 'C', 'D'};
    private final Random random = new Random();

    private int clickCount = 0;
    private JButton firstButton, secondButton;

    public CandyCrushGUI() {
        setTitle("Candy Crush Mini");
        setLayout(new GridLayout(SIZE, SIZE));
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initBoard();
        processMatches(); // Crush initial matches if any
        setVisible(true);
    }

    private void initBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                JButton btn = new JButton();
                btn.setFont(new Font("Arial", Font.BOLD, 20));
                btn.setBackground(Color.WHITE);
                char candy = randomCandy();
                btn.setText(String.valueOf(candy));
                btn.setBackground(getColorForCandy(candy));

                int row = i, col = j;
                btn.addActionListener(e -> handleClick(row, col));

                buttons[i][j] = btn;
                add(btn);
            }
        }
    }

    private char randomCandy() {
        return candies[random.nextInt(candies.length)];
    }

    private void handleClick(int row, int col) {
        if (clickCount == 0) {
            firstButton = buttons[row][col];
            firstButton.setBackground(Color.YELLOW);
            clickCount++;
        } else {
            secondButton = buttons[row][col];
            firstButton.setBackground(Color.WHITE);
            if (firstButton != secondButton && areAdjacent(firstButton, secondButton)) {
                swap(firstButton, secondButton);
                if (hasMatch()) {
                    processMatches(); // Process all matches after a valid move
                } else {
                    // Undo the swap if no match
                    swap(firstButton, secondButton);
                }
            }
            clickCount = 0;
        }
    }

    private boolean areAdjacent(JButton a, JButton b) {
        Point p1 = getButtonPosition(a);
        Point p2 = getButtonPosition(b);
        int dx = Math.abs(p1.x - p2.x), dy = Math.abs(p1.y - p2.y);
        return (dx + dy == 1);
    }

    private Point getButtonPosition(JButton btn) {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (buttons[i][j] == btn)
                    return new Point(i, j);
        return null;
    }

    private void swap(JButton a, JButton b) {
        String temp = a.getText();
        a.setText(b.getText());
        b.setText(temp);
    }

    private boolean hasMatch() {
        // Temporarily run match logic and restore board
        boolean[][] toCrush = getMatchMatrix();
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (toCrush[i][j])
                    return true;
        return false;
    }

    private void processMatches() {
        boolean foundMatch;
        do {
            foundMatch = crushMatches();
            dropCandies();
            refillBoard();
        } while (foundMatch);
    }

    private boolean crushMatches() {
        boolean[][] toCrush = getMatchMatrix();
        boolean crushed = false;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (toCrush[i][j]) {
                    buttons[i][j].setText(" ");
                    buttons[i][j].setBackground(Color.WHITE);
                    crushed = true; // Mark that something was crushed
                }
            }
        }

        return crushed;
    }

    private boolean[][] getMatchMatrix() {
        boolean[][] toCrush = new boolean[SIZE][SIZE];

        // Horizontal matches
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE - 2; j++) {
                String c1 = buttons[i][j].getText();
                String c2 = buttons[i][j + 1].getText();
                String c3 = buttons[i][j + 2].getText();
                if (!c1.equals(" ") && c1.equals(c2) && c2.equals(c3)) {
                    toCrush[i][j] = toCrush[i][j + 1] = toCrush[i][j + 2] = true;
                }
            }

        // Vertical matches
        for (int j = 0; j < SIZE; j++)
            for (int i = 0; i < SIZE - 2; i++) {
                String c1 = buttons[i][j].getText();
                String c2 = buttons[i + 1][j].getText();
                String c3 = buttons[i + 2][j].getText();
                if (!c1.equals(" ") && c1.equals(c2) && c2.equals(c3)) {
                    toCrush[i][j] = toCrush[i + 1][j] = toCrush[i + 2][j] = true;
                }
            }

        return toCrush;
    }

    private void dropCandies() {
        for (int j = 0; j < SIZE; j++) {
            int emptyRow = SIZE - 1;
            for (int i = SIZE - 1; i >= 0; i--) {
                if (!buttons[i][j].getText().equals(" ")) {
                    buttons[emptyRow][j].setText(buttons[i][j].getText());
                    buttons[emptyRow][j].setBackground(buttons[i][j].getBackground());
                    if (emptyRow != i) {
                        buttons[i][j].setText(" ");
                        buttons[i][j].setBackground(Color.WHITE);
                    }
                    emptyRow--;
                }
            }
        }
    }

    private void refillBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (buttons[i][j].getText().equals(" ")) {
                    char candy = randomCandy();
                    buttons[i][j].setText(String.valueOf(candy));
                    buttons[i][j].setBackground(getColorForCandy(candy));
                }
            }
        }
    }

    private Color getColorForCandy(char candy) {
        return switch (candy) {
            case 'A' -> Color.RED;
            case 'B' -> Color.GREEN;
            case 'C' -> Color.BLUE;
            case 'D' -> Color.MAGENTA;
            default -> Color.LIGHT_GRAY;
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CandyCrushGUI::new);
    }
}
