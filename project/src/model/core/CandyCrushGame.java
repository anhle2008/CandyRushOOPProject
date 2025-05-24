package model.core;

import model.board.*;
import model.config.GameConfig;
import model.logic.*;
import model.manager.*;
import view.CandyButton;

import java.util.*;
import javax.swing.Timer;

/**
 * Game logic class for Candy Crush.
 * Handles matching, swapping, scoring, animations, and board updates.
 */
public class CandyCrushGame {
    private final GameBoard gameBoard;
    private final ScoreManager scoreManager;
    private final AnimationManager animationManager;
    private final GameMode mode;
    private final CandyButton[][] buttonGrid;
    private final List<GameStateListener> listeners = new ArrayList<>();

    private int remainingMoves;
    private int remainingTimeSeconds;
    private Timer gameTimer;
    private boolean hasWon = false;

    public CandyCrushGame(CandyCell[][] cellGrid, CandyButton[][] buttonGrid, int size, GameMode mode) {
        this.gameBoard = new GameBoard(cellGrid, size);
        this.scoreManager = new ScoreManager();
        this.animationManager = new AnimationManager(cellGrid, buttonGrid);
        this.mode = mode;

        this.buttonGrid = buttonGrid;

        this.remainingMoves = GameConfig.MAX_MOVES;
        this.remainingTimeSeconds = GameConfig.MAX_TIME_SECONDS;

        if (mode == GameMode.TIME_LIMITED) {
            startGame();
        }
    }

    public int getTotalScore() {
        return scoreManager.getTotalScore();
    }

    public int getMaximumScore() {
        return scoreManager.getMaximumScore(mode);
    }

    public int getRemainingTimeSeconds() {
        return remainingTimeSeconds;
    }

    public int getRemainingMoves() {
        return remainingMoves;
    }

    public void addGameStateListener(GameStateListener listener) {
        listeners.add(listener);
    }

    public void removeGameStateListener(GameStateListener listener) {
        listeners.remove(listener);
    }

    /**
     * Helper method to start game. Currently only for starting "Beat the clock!" timer.
     */
    public void startGame() {
        hasWon = false; // Also reset this flag on restarting game
        if (mode == GameMode.TIME_LIMITED) {
            startCountDown();
        }
    }

    /**
     * Helper method to stop game. Currently ensure countdown timer is stopped.
     */
    public void stopGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
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

    public void addPoints(int candyCount) {
        scoreManager.addPoints(candyCount);
        notifyScoreUpdate();

        if (isReachedGoal() && mode == GameMode.MOVE_LIMITED) {
            notifyWinning();
        }
    }

    public void startCountDown() {
        gameTimer = new Timer(1000, e -> {
            remainingTimeSeconds--;
            notifyTimeUpdate();
            if (isReachedGoal()) {
                notifyWinning();
            } else if (remainingTimeSeconds <= 0) {
                gameTimer.stop();
                notifyGameOver();
            }
        });
        gameTimer.start();
    }

    public void decrementMove() {
        if (mode == GameMode.MOVE_LIMITED) {
            remainingMoves--;
            notifyMovesUpdate();
            if (isReachedGoal()) {
                notifyWinning();
            } else if (remainingMoves <= 0) {
                notifyGameOver();
            }
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
            addPoints(candyCount);

            Timer timer = new Timer(GameConfig.POST_CRUSH_DELAY_MS, null);
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
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                buttonGrid[row][col].updateIcon();
            }
        }
    }

    private boolean isReachedGoal() {
        return scoreManager.getTotalScore() >= GameConfig.SCORE_GOAL;
    }

    private void notifyTimeUpdate() {
        for (GameStateListener l: listeners) {
            l.onTimeUpdate(remainingTimeSeconds);
        }
    }

    private void notifyMovesUpdate() {
        for (GameStateListener l: listeners) {
            l.onMovesUpdate(remainingMoves);
        }
    }

    private void notifyScoreUpdate() {
        for (GameStateListener l: listeners) {
            l.onScoreUpdate(scoreManager.getTotalScore(), scoreManager.getMaximumScore(mode));
        }
    }

    private void notifyGameOver() {
        for (GameStateListener l: listeners) {
            l.onGameOver();
        }
    }

    private void notifyWinning() {
        if (!hasWon) {
            hasWon = true;
            for (GameStateListener l: listeners) {
                l.onWinning();
            }
        }
    }
}