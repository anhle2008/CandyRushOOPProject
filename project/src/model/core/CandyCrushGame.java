package model.core;

import util.CandyUtils;
import view.CandyButton;
import model.scoring.ScoreManager;
import model.board.*;
import model.logic.*;

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

    public CandyCrushGame(CandyCell[][] cellGrid, CandyButton[][] buttonGrid, int size, GameMode mode) {
        this.gameBoard = new GameBoard(cellGrid, size);
        this.scoreManager = new ScoreManager();
        this.animationManager = new AnimationManager(cellGrid, buttonGrid);
        this.mode = mode;

        this.buttonGrid = buttonGrid;

        this.remainingMoves = 5; // default value
        this.remainingTimeSeconds = 10; 
    }

    public int getTotalScore() {
        return scoreManager.getTotalScore();
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
            notifyScoreUpdate();

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
            l.onScoreUpdate(scoreManager.getTotalScore());
        }
    }

    private void notifyGameOver() {
        for (GameStateListener l: listeners) {
            l.onGameOver();
        }
    }

    public void addPoints(int candyCount) {
        scoreManager.addPoints(candyCount);
        notifyScoreUpdate();
    }

    public void startCountDown() {
        gameTimer = new Timer(1000, e -> {
            remainingTimeSeconds--;
            notifyTimeUpdate();
            if (remainingTimeSeconds <= 0) {
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
            if (remainingMoves <= 0) {
                notifyGameOver();
            }
        }
    }
}