package model;

import util.CandyUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Game logic class for Candy Crush.
 * Handles matching, swapping, scoring, animations, and board updates.
 */
public class CandyCrushGame {
    private final int size;
    private final CandyButton[][] board;
    private int totalScore = 0;

    private Runnable scoreUpdateCallback;

    public CandyCrushGame(CandyButton[][] board, int size) {
        this.board = board;
        this.size = size;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setScoreUpdateCallback(Runnable callback) {
        this.scoreUpdateCallback = callback;
    }

    /**
     * Swaps the appearance and type of two candy buttons.
     */
    public void swap(CandyButton a, CandyButton b) {
        String tempText = a.getText();
        Color tempColor = a.getBackground();

        a.setText(b.getText());
        a.setBackground(b.getBackground());

        b.setText(tempText);
        b.setBackground(tempColor);
    }

    /**
     * Determines if two candies are adjacent on the board.
     */
    public boolean areAdjacent(CandyButton a, CandyButton b) {
        int dx = Math.abs(a.getRow() - b.getRow());
        int dy = Math.abs(a.getColumn() - b.getColumn());
        return dx + dy == 1;
    }

    /**
     * Starts an animation for swapping candies, followed by a callback.
     */
    public void animateSwap(CandyButton a, CandyButton b, Runnable onComplete) {
        Color originalA = a.getBackground();
        Color originalB = b.getBackground();

        a.setBackground(Color.YELLOW);
        b.setBackground(Color.YELLOW);

        Timer timer = new Timer(CandyUtils.getAnimationDelay(), e -> {
            a.setBackground(originalA);
            b.setBackground(originalB);
            onComplete.run();
            ((Timer) e.getSource()).stop();
        });

        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Processes all matches in the grid. Can be animated or immediate.
     */
    public void processMatches(boolean withAnimation) {
        if (withAnimation) {
            processMatchesWithAnimation();
        } else {
            processMatchesWithoutAnimation();
        }
    }

    private void processMatchesWithoutAnimation() {
        boolean foundMatch;
        do {
            foundMatch = crushMatches();
            dropCandies();
            refillBoard();
        } while (foundMatch);
    }

    private void processMatchesWithAnimation() {
        boolean[][] toCrush = getMatchMatrix();
        if (!hasMatch(toCrush)) return;

        animateCrush(toCrush, () -> {
            int crushed = countCrushed(toCrush);
            totalScore += crushed * GameConfig.SCORE_PER_CANDY;

            if (scoreUpdateCallback != null) {
                SwingUtilities.invokeLater(scoreUpdateCallback);
            }

            crushMatches(); // Crush again after animation

            Timer delay = new Timer(CandyUtils.getAnimationDelay(), e -> {
                ((Timer) e.getSource()).stop();
                dropCandies();
                refillBoard();
                processMatchesWithAnimation(); // Recursively check for more matches
            });

            delay.setRepeats(false);
            delay.start();
        });
    }

    /**
     * Checks for matches anywhere on the board.
     */
    public boolean hasMatch() {
        return hasMatch(getMatchMatrix());
    }

    private boolean hasMatch(boolean[][] matrix) {
        for (boolean[] row : matrix) {
            for (boolean cell : row) {
                if (cell) return true;
            }
        }
        return false;
    }

    /**
     * Clears matched candies from the board.
     */
    private boolean crushMatches() {
        boolean[][] toCrush = getMatchMatrix();
        boolean crushed = false;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (toCrush[i][j]) {
                    board[i][j].clearCandy();
                    crushed = true;
                }
            }
        }

        return crushed;
    }

    /**
     * Drops candies into empty slots from above.
     */
    private void dropCandies() {
        for (int column = 0; column < size; column++) {
            int emptyRow = size - 1;

            for (int row = size - 1; row >= 0; row--) {
                if (!board[row][column].isEmpty()) {
                    board[emptyRow][column].setText(board[row][column].getText());
                    board[emptyRow][column].setBackground(board[row][column].getBackground());

                    if (emptyRow != row) {
                        board[row][column].clearCandy();
                    }

                    emptyRow--;
                }
            }
        }
    }

    /**
     * Refills the board with random candies where empty.
     */
    private void refillBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j].isEmpty()) {
                    CandyType candy = CandyUtils.getRandomCandy();
                    board[i][j].setCandy(candy);
                }
            }
        }
    }

    /**
     * Detects all horizontal and vertical matches (3 or more).
     */
    private boolean[][] getMatchMatrix() {
        boolean[][] matches = new boolean[size][size];

        // Horizontal
        for (int i = 0; i < size; i++) {
            for (int j = 0; j <= size - 3; j++) {
                String c1 = board[i][j].getText();
                String c2 = board[i][j + 1].getText();
                String c3 = board[i][j + 2].getText();

                if (!c1.equals(" ") && c1.equals(c2) && c2.equals(c3)) {
                    matches[i][j] = matches[i][j + 1] = matches[i][j + 2] = true;
                }
            }
        }

        // Vertical
        for (int j = 0; j < size; j++) {
            for (int i = 0; i <= size - 3; i++) {
                String c1 = board[i][j].getText();
                String c2 = board[i + 1][j].getText();
                String c3 = board[i + 2][j].getText();

                if (!c1.equals(" ") && c1.equals(c2) && c2.equals(c3)) {
                    matches[i][j] = matches[i + 1][j] = matches[i + 2][j] = true;
                }
            }
        }

        return matches;
    }

    /**
     * Blinks matched candies for visual effect.
     */
    private void animateCrush(boolean[][] toCrush, Runnable onComplete) {
        final int[] blinkStep = {0};
        final int maxSteps = 4;

        Timer timer = new Timer(CandyUtils.getAnimationDelay(), null);

        timer.addActionListener(e -> {
            if (blinkStep[0] < maxSteps) {
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if (toCrush[i][j]) {
                            CandyButton btn = board[i][j];
                            btn.setBackground(btn.getBackground().equals(Color.WHITE)
                                    ? CandyType.fromChar(btn.getText().charAt(0)).getColor()
                                    : Color.WHITE);
                        }
                    }
                }
                blinkStep[0]++;
            } else {
                timer.stop();
                onComplete.run();
            }
        });

        timer.start();
    }

    /**
     * Counts how many candies will be crushed.
     */
    private int countCrushed(boolean[][] toCrush) {
        int count = 0;
        for (boolean[] row : toCrush) {
            for (boolean cell : row) {
                if (cell) count++;
            }
        }
        return count;
    }
}