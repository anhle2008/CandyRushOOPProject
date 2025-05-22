package model;

import util.CandyUtils;
import view.CandyButton;

import javax.swing.*;

/**
 * Game logic class for Candy Crush.
 * Handles matching, swapping, scoring, animations, and board updates.
 */
public class CandyCrushGame {
    private final GameBoard gameBoard;
    private final ScoreManager scoreManager;
    private final AnimationManager animationManager;

    private final CandyButton[][] buttonGrid;

    public CandyCrushGame(CandyCell[][] cellGrid, CandyButton[][] buttonGrid, int size) {
        this.gameBoard = new GameBoard(cellGrid, size);
        this.scoreManager = new ScoreManager();
        this.animationManager = new AnimationManager(cellGrid, buttonGrid);
        this.buttonGrid = buttonGrid;
    }

    public void setScoreUpdateCallback(Runnable callback) {
        scoreManager.setUpdateCallback(callback);
    }

    public int getTotalScore() {
        return scoreManager.getTotalScore();
    }

    /**
     * Determines if two candies are adjacent on the board.
     */
    public boolean areAdjacent(CandyCell a, CandyCell b) {
        int dx = Math.abs(a.getRow() - b.getRow());
        int dy = Math.abs(a.getColumn() - b.getColumn());
        return dx + dy == 1;
    }

    public void animateSwap(CandyButton a, CandyButton b, Runnable onComplete) {
        animationManager.animateSwap(a, b, onComplete);
    }

    public void swap(CandyCell a, CandyCell b) {
        gameBoard.swap(a, b);
    }

    public boolean hasMatch() {
        boolean[][] matchMatrix = MatchFinder.findMatches(gameBoard.getBoard());
        return MatchFinder.hasMatch(matchMatrix);
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

    /**
     * Processing match without animation, useful for preprocessing the board.
     */
    private void processMatchesWithoutAnimation() {
        boolean foundMatch;
        do {
            boolean[][] matches = MatchFinder.findMatches(gameBoard.getBoard());
            foundMatch = MatchFinder.hasMatch(matches);

            if (foundMatch) {
                gameBoard.crush(matches);
                gameBoard.dropCandies();
                gameBoard.refillBoard();
            }
        } while (foundMatch);

        // Reflect model change
        updateAllButtonIcons();
    }

    /**
     * Processing match with animation, useful for user-facing interaction.
     */
    private void processMatchesWithAnimation() {
        boolean[][] matches = MatchFinder.findMatches(gameBoard.getBoard());

        if (!MatchFinder.hasMatch(matches)) return;

        animationManager.animateCrush(matches, () -> {
            int candyCount = MatchFinder.countMatches(matches);
            gameBoard.crush(matches);
            scoreManager.addPoints(candyCount);

            Timer timer = new Timer(CandyUtils.getPostCrushDelay(), null);
            timer.addActionListener(e -> {
                timer.stop();
                gameBoard.dropCandies();
                gameBoard.refillBoard();
                updateAllButtonIcons(); // Reflect model change
                processMatchesWithAnimation(); // Recursively check for more matches
            });
            timer.setRepeats(false);
            timer.start();
        });
    }

    private void updateAllButtonIcons() {
        int size = buttonGrid.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttonGrid[i][j].updateIcon();
            }
        }
     }
}