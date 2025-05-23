package view;

import model.*;
import util.CandyUtils;
import controller.CandyCrushController;

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

    public CandyCrushGUI(GameMode mode) {
        this.mode = mode;

        configureWindow();
        JPanel topPanel = initializeTopPanel();
        JPanel gridPanel = initializeGrid();

        add(topPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);

        game = new CandyCrushGame(cells, buttons, gridSize, mode);
        game.addGameStateListener(this);

        controller = new CandyCrushController(game, this);

        game.processMatches(false); // Preprocess any initial matches
        startGame();
        updateModeLabel(); // Setting label according to mode
        setVisible(true);
    }

    /**
     * Initializes the main game window settings.
     */
    private void configureWindow() {
        setTitle("Candy Crush Mini");
        setSize(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        modeLabel.setFont(new Font("Arial", Font.BOLD, 20));
    }

    /**
     * Initialize and returns the panel containing the candy grid.
     */
    private JPanel initializeGrid() {
        JPanel panel = new JPanel(new GridLayout(gridSize, gridSize));

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                CandyType candy = CandyUtils.getRandomCandy();
                CandyCell cell = new CandyCell(row, col, candy);
                cells[row][col] = cell;

                CandyButton btn = new CandyButton(cell);
                buttons[row][col] = btn;
                btn.addActionListener(e -> handleClick(btn, mode));

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
     * Handles a user's click on a candy button.
     */
    private void handleClick(CandyButton clicked, GameMode mode) {
        controller.handleCandyClick(clicked, mode);
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

    private void startGame() {
        if (mode == GameMode.TIME_LIMITED) {
            game.startCountDown();
        }
    }

    @Override
    public void onTimeUpdate(int newTimeSeconds) {
        modeLabel.setText("Time left: " + newTimeSeconds + "s");
    }

    @Override
    public void onMovesUpdate(int remainingMoves) {
        modeLabel.setText("Moves left: " + remainingMoves);
    }

    @Override
    public void onScoreUpdate(int newScore) {
        scoreLabel.setText("Score: " + newScore);
    }
    
    @Override
    public void onGameOver() {
        if (mode == GameMode.TIME_LIMITED) {
            showGameOverDialog("Time's Up!");
        } else if (mode == GameMode.MOVE_LIMITED) {
            showGameOverDialog("No more moves left!");
        }
    }

    public void showGameOverDialog(String message) {
        // Disable all buttons
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                buttons[i][j].setEnabled(false);
            }
        }

        // Show popup dialog
        JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }
}
