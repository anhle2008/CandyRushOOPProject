package view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GameDialogManager {
    private final JFrame parent;
    private final CandyButton[][] buttons;
    
    public GameDialogManager(JFrame parent, CandyButton[][] buttons) {
        this.parent = parent;
        this.buttons = buttons;
    }

    public void showGameOverDialog(String message) {
        disableAllButtons();
        JOptionPane.showMessageDialog(parent, message, "Game Over!", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showWinningDialog() {
        disableAllButtons();
        JOptionPane.showMessageDialog(parent, "You win!", "Congratulation!", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showPlayAgainDialog(Runnable onYes) {
        disableAllButtons();

        int choice = JOptionPane.showConfirmDialog(parent, "Do you want to play again?", "Play again?", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            onYes.run();
        } else {
            System.exit(0);
        }
    }

    /**
     * Disable all buttons, preventing user interaction.
     */
    private void disableAllButtons() {
        int gridSize = buttons.length;
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                buttons[row][col].setEnabled(false);
            }
        }
    }
}