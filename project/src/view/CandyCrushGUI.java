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
public class CandyCrushGUI extends JFrame {
    private final int gridSize = GameConfig.GRID_SIZE;
    private final CandyCell[][] cells = new CandyCell[gridSize][gridSize];
    private final CandyButton[][] buttons = new CandyButton[gridSize][gridSize];

    private final JLabel scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);

    private final CandyCrushGame game;
    private final CandyCrushController controller;

    public CandyCrushGUI() {
        configureWindow();
        JPanel gridPanel = initializeGrid();

        add(scoreLabel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);

        game = new CandyCrushGame(cells, buttons, gridSize);
        game.setScoreUpdateCallback(() -> scoreLabel.setText("Score: " + game.getTotalScore()));

        controller = new CandyCrushController(game, this);

        game.processMatches(false); // Preprocess any initial matches
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
                btn.addActionListener(e -> handleClick(btn));

                panel.add(btn);
            }
        }

        return panel;
    }

    /**
     * Handles a user's click on a candy button.
     */
    private void handleClick(CandyButton clicked) {
        controller.handleCandyClick(clicked);
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
}
