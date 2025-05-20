import javax.swing.*;
import java.awt.*;

public class CandyCrushGUI extends JFrame {
    private final int SIZE = 6;
    private final CandyButton[][] buttons = new CandyButton[SIZE][SIZE];
    private final JLabel scoreLabel = new JLabel("Score: 0", SwingConstants.CENTER);
    private final CandyCrushGame game;

    private final int mainGameSize = 500;
    private final int scoreHeight = 50;

    private int clickCount = 0;
    private CandyButton firstButton, secondButton;

    public CandyCrushGUI() {
        // Initialize window
        setTitle("Candy Crush Mini");
        setSize(mainGameSize, mainGameSize + scoreHeight);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize score label
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(scoreLabel, BorderLayout.NORTH);

        // Initialize candies grid
        JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE));
        initBoard(gridPanel);
        add(gridPanel, BorderLayout.CENTER);

        // Initialize main game
        game = new CandyCrushGame(buttons, SIZE);
        game.processMatches(); // Preprocessing matches

        // Show window
        setVisible(true);
    }

    private void initBoard(JPanel panel) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                char candy = CandyUtils.randomCandy();
                Color color = CandyUtils.getColorForCandy(candy);
                CandyButton btn = new CandyButton(i, j, candy, color);

                btn.addActionListener(e -> handleClick(btn));
                buttons[i][j] = btn;
                panel.add(btn);
            }
        }
    }

    private void handleClick(CandyButton btn) {
        if (clickCount == 0) {
            firstButton = btn;
            firstButton.setBackground(Color.YELLOW);
            clickCount++;
        } else {
            secondButton = btn;
            firstButton.setBackground(CandyUtils.getColorForCandy(firstButton.getText().charAt(0)));

            if (firstButton != secondButton && game.areAdjacent(firstButton, secondButton)) {
                game.swap(firstButton, secondButton);

                if (game.hasMatch()) {
                    game.processMatchesAnimated();

                    // Score update callback
                    game.setScoreUpdateCallback(() -> scoreLabel.setText("Score: " + game.getTotalScore()));
                } else {
                    game.swap(firstButton, secondButton); // Revert candies swap
                }
            }

            clickCount = 0;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CandyCrushGUI::new);
    }
}
