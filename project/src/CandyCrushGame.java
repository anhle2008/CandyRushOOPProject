import java.awt.*;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class CandyCrushGame {
    private final int SIZE;
    private final CandyButton[][] buttons;
    private int totalScore = 0;
    private final int scorePerCandy = 10;

    private Runnable scoreUpdateCallback;

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

    /**
     * Process candies match, without animation. This is useful for preprocessing matches when the game start.
     */
    public void processMatches() {
        boolean foundMatch;

        do {
            foundMatch = crushMatches();
            dropCandies();
            refillBoard();
        } while (foundMatch);
    }

    /**
     * Processing candies match, but with animation.
     */
    public void processMatchesAnimated() {
        boolean[][] toCrush = getMatchMatrix();

        if (hasMatch()) {
            animateCrush(toCrush, () -> {
                // Update score
                int crushedCount = countCrushedCandies(toCrush);
                totalScore += crushedCount * scorePerCandy;

                // Notify GUI to update score
                if (scoreUpdateCallback != null) {
                    SwingUtilities.invokeLater(scoreUpdateCallback);
                }

                crushMatches();

                Timer delay = new Timer(CandyUtils.getAnimationDelay(), e -> {
                    ((Timer) e.getSource()).stop();
                    dropCandies();
                    refillBoard();

                    // Continue if more matches occur
                    processMatchesAnimated();
                });

                delay.setRepeats(false);
                delay.start();                
            });
        }
    }

    private boolean isEmpty(CandyButton btn) {
        return btn.getText().equals(" ");
    }

    private boolean crushMatches() {
        boolean[][] toCrush = getMatchMatrix();
        boolean crushed = false;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (toCrush[i][j]) {
                    buttons[i][j].setText(" ");
                    buttons[i][j].setBackground(Color.WHITE);

                    crushed = true;
                }
            }
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

    // Animate the matches of candies by blinking
    private void animateCrush(boolean[][] toCrush, Runnable onComplete) {
        final int[] blinkStep = {0};
        final int maxBlinkStep = 4;
        
        Timer timer = new Timer(CandyUtils.getAnimationDelay(), null);

        timer.addActionListener(e -> {
            if (blinkStep[0] < maxBlinkStep) {
                for (int i = 0; i < SIZE; i++) {
                    for (int j = 0; j < SIZE; j++) {
                        if (toCrush[i][j]) {
                            Color currentCandyColor = buttons[i][j].getBackground();
                            buttons[i][j].setBackground(
                                currentCandyColor.equals(Color.WHITE)
                                    ? CandyUtils.getColorForCandy(buttons[i][j].getText().charAt(0))
                                    : Color.WHITE
                            );
                        }
                    }
                }

                blinkStep[0]++;
            } else {
                timer.stop();
                onComplete.run(); // Call the logic to crush candies
            }
        });

        timer.start();
    }

    private int countCrushedCandies(boolean[][] toCrush) {
        int count = 0;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (toCrush[i][j]) {
                    count++;
                }
            }
        }

        return count;
    }

    public void setScoreUpdateCallback(Runnable callback) {
        this.scoreUpdateCallback = callback;
    }
}