package view;

import model.config.GameConfig;

import javax.swing.*;
import java.awt.*;

/**
 * Manages GUI element (window initialization, component's font)
 */
public class GUI {
    public GUI(JFrame parent, String title, int windowWidth, int windowHeight, LayoutManager layout) {
        parent.setTitle(title);
        parent.setSize(windowWidth, windowHeight);
        parent.setLayout(layout);
        parent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        parent.setLocationRelativeTo(null); // Center window to the screen
    }

    public void setFontForComponents(JComponent[] components) {
        Font font = new Font(GameConfig.FONT_NAME, GameConfig.FONT_STYLE, GameConfig.FONT_SIZE);
        for (JComponent component: components) {
            component.setFont(font);
        }
    }

    /**
     * Restart the GUI and run action.
     */
    public static void restart(JFrame parent, Runnable action) {
        SwingUtilities.invokeLater(() -> {
            parent.dispose(); // Close current window
            action.run();
        });
    }
}