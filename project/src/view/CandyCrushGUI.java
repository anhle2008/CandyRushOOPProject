package view;

import controller.CandyCrushController;
import model.board.CandyCell;
import model.config.GameConfig;
import model.core.*;
import model.type.CandyType;
import util.CandyUtils;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;

/**
 * GUI for Candy Crush Game.
 * Handles grid layout, user input, and score display.
 */
public class CandyCrushGUI extends JFrame implements GameStateListener {
    private final int gridSize = GameConfig.GRID_SIZE;
    private final CandyCell[][] cells = new CandyCell[gridSize][gridSize];
    private final CandyButton[][] buttons = new CandyButton[gridSize][gridSize];

    private final JLabel scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
    private final JLabel modeLabel = new JLabel("", SwingConstants.CENTER);

    private final CandyCrushGame game;
    private final CandyCrushController controller;
    private final GameMode mode;
    private final GameDialogManager dialogManager;

    public CandyCrushGUI(GameMode mode) {
        GUI gui = new GUI(this, "Candy Crush Mini", GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT, new BorderLayout());
        gui.setFontForComponents(new JComponent[]{scoreLabel, modeLabel});
        dialogManager = new GameDialogManager(this, buttons);

        this.mode = mode;

        JPanel topPanel = initializeTopPanel();
        JPanel gridPanel = initializeGrid();

        add(topPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);

        game = new CandyCrushGame(cells, buttons, gridSize, mode);
        game.addGameStateListener(this);

        controller = new CandyCrushController(game, this);

        game.processMatches(false); // Preprocess any initial matches
        onScoreUpdate(game.getTotalScore(), game.getMaximumScore()); // Initial score
        updateModeLabel(); // Setting label according to mode
        setVisible(true);
    }

    /**
     * Visually highlights or unhighlights a button.
     */
    public void highlightButton(CandyButton btn, boolean highlight) {
        if (highlight) {
            // Use a compound border with padding to make it stand out more
            Border outer = BorderFactory.createLineBorder(Color.ORANGE, 4);
            Border inner = BorderFactory.createEmptyBorder(2, 2, 2, 2);
            btn.setBorder(BorderFactory.createCompoundBorder(outer, inner));;
        } else {
            btn.setBorder(BorderFactory.createEmptyBorder());
        }
    }

    // -------------- GameStateListener methods --------------
    @Override
    public void onTimeUpdate(int newTimeSeconds) {
        modeLabel.setText("Time left: " + newTimeSeconds + "s");
    }

    @Override
    public void onMovesUpdate(int remainingMoves) {
        modeLabel.setText("Moves left: " + remainingMoves);
    }

    @Override
    public void onScoreUpdate(int newScore, int maxScore) {
        String message = "Score: " + newScore;

        if (maxScore != 0) {
            message += "/" + maxScore;
        }

        scoreLabel.setText(message);
    }
    
    @Override
    public void onGameOver() {
        if (mode == GameMode.TIME_LIMITED) {
            dialogManager.showGameOverDialog("Time's Up!");
        } else if (mode == GameMode.MOVE_LIMITED) {
            dialogManager.showGameOverDialog("No more moves left!");
        }
        dialogManager.showPlayAgainDialog(() -> restartGame());
    }

    @Override
    public void onWinning() {
        dialogManager.showWinningDialog();
        dialogManager.showPlayAgainDialog(() -> restartGame());
    }

    /**
     * Initialize and returns the panel containing the candy grid.
     */
    private JPanel initializeGrid() {
        JPanel panel = new JPanel(new GridLayout(gridSize, gridSize));

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                CandyCell cell = createCandy(row, col);
                cells[row][col] = cell;

                CandyButton btn = createButton(cell);
                buttons[row][col] = btn;

                panel.add(btn);
            }
        }

        return panel;
    }

    /**
     * Initialize top panel for score and mode-related label
     */
    private JPanel initializeTopPanel() {
        JPanel panel = new JPanel(new GridLayout(1 ,2));
        panel.add(scoreLabel, BorderLayout.NORTH);
        panel.add(modeLabel, BorderLayout.CENTER);

        return panel;
    }
    
    /**
     * Helper method to create candy.
     */
    private CandyCell createCandy(int row, int col) {
        CandyType candy = CandyUtils.getRandomCandy();
        return new CandyCell(row, col, candy);
    }

    /**
     * Helper method to create candy button
     */
    private CandyButton createButton(CandyCell cell) {
        CandyButton button = new CandyButton(cell);
        button.addActionListener(e -> handleClick(button, mode));
        return button;
    }

    /**
     * Handles a user's click on a candy button.
     */
    private void handleClick(CandyButton clicked, GameMode mode) {
        controller.handleCandyClick(clicked, mode);
    }

    /**
     * Update label based on mode.
     */
    private void updateModeLabel() {
        switch (mode) {
            case TIME_LIMITED:
                modeLabel.setText("Time left: " + game.getRemainingTimeSeconds() + "s");
                break;
            case MOVE_LIMITED:
                modeLabel.setText("Moves left: " + game.getRemainingMoves());
                break;
            case ENDLESS: // Normal mode
                break;
        }
    }

    /**
     * Helper method to restart game. Currently just reinitialize the same game mode.
     */
    private void restartGame() {
        GUI.restart(this, () -> {
            game.stopGame(); // Stop previous timer when in "Beat the clock!" mode
            new CandyCrushGUI(mode);
        });
    }
}
