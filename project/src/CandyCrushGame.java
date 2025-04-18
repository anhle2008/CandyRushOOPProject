import java.awt.*;

public class CandyCrushGame {
    private final int SIZE;
    private final CandyButton[][] buttons;
    private int totalScore = 0;
    private final int scorePerCandy = 10;

    public CandyCrushGame(CandyButton[][] buttons, int size) {
        this.buttons = buttons;
        this.SIZE = size;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void swap(CandyButton a, CandyButton b) {
        String temp = a.getText();
        Color colorTemp = a.getBackground();

        a.setText(b.getText());
        a.setBackground(b.getBackground());

        b.setText(temp);
        b.setBackground(colorTemp);
    }

    public boolean areAdjacent(CandyButton a, CandyButton b) {
        int dx = Math.abs(a.getRow() - b.getRow());
        int dy = Math.abs(a.getCol() - b.getCol());

        return (dx + dy == 1);
    }

    public boolean hasMatch() {
        // Temporarily run match logic and restore board
        boolean[][] toCrush = getMatchMatrix();

        for (boolean[] row: toCrush) {
            for (boolean match: row) {
                if (match) {
                    return true;
                }
            }
        }

        return false;
    }

    public void processMatches() {
        boolean foundMatch;

        do {
            foundMatch = crushMatches();
            dropCandies();
            refillBoard();
        } while (foundMatch);
    }

    private boolean isEmpty(CandyButton btn) {
        return btn.getText().equals(" ");
    }

    private boolean crushMatches() {
        boolean[][] toCrush = getMatchMatrix();
        boolean crushed = false;
        int crushedCount = 0;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (toCrush[i][j]) {
                    buttons[i][j].setText(" ");
                    buttons[i][j].setBackground(Color.WHITE);

                    crushed = true;
                    crushedCount++;
                }
            }
        }

        if (crushed) {
            totalScore += crushedCount * scorePerCandy;
        }

        return crushed;
    }

    private void dropCandies() {
        for (int j = 0; j < SIZE; j++) {
            int emptyRow = SIZE - 1;

            for (int i = SIZE - 1; i >= 0; i--) {
                if (!isEmpty(buttons[i][j])) {
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
                if (isEmpty(buttons[i][j])) {
                    char candy = CandyUtils.randomCandy();

                    buttons[i][j].setText(String.valueOf(candy));
                    buttons[i][j].setBackground(CandyUtils.getColorForCandy(candy));
                }
            }
        }
    }

    private boolean[][] getMatchMatrix() {
        boolean[][] toCrush = new boolean[SIZE][SIZE];

        // Horizontal matches
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE - 2; j++) {
                String c1 = buttons[i][j].getText();
                String c2 = buttons[i][j + 1].getText();
                String c3 = buttons[i][j + 2].getText();

                if (!c1.equals(" ") && c1.equals(c2) && c2.equals(c3)) {
                    toCrush[i][j] = toCrush[i][j + 1] = toCrush[i][j + 2] = true;
                }
            }
        }

        // Vertical matches
        for (int j = 0; j < SIZE; j++) {
            for (int i = 0; i < SIZE - 2; i++) {
                String c1 = buttons[i][j].getText();
                String c2 = buttons[i + 1][j].getText();
                String c3 = buttons[i + 2][j].getText();

                if (!c1.equals(" ") && c1.equals(c2) && c2.equals(c3)) {
                    toCrush[i][j] = toCrush[i + 1][j] = toCrush[i + 2][j] = true;
                }
            }
        } 

        return toCrush;
    }
}