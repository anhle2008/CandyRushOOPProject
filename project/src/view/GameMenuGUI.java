package view;

import model.config.GameConfig;
import model.core.GameMode;

import javax.swing.*;
import java.awt.*;

public class GameMenuGUI extends JFrame {
    public GameMenuGUI() {
        GUI gui = new GUI(this, "Choose a mode", GameConfig.MENU_WIDTH, GameConfig.MENU_HEIGHT, new GridLayout(3, 1));

        JButton endlessBtn = new JButton("Endless");
        JButton timeBtn = new JButton("Beat the clock");
        JButton moveBtn = new JButton("Limited move");

        gui.setFontForComponents(new JComponent[]{endlessBtn, timeBtn, moveBtn});

        endlessBtn.addActionListener(e -> launchGame(GameMode.ENDLESS));
        timeBtn.addActionListener(e -> launchGame(GameMode.TIME_LIMITED));
        moveBtn.addActionListener(e -> launchGame(GameMode.MOVE_LIMITED));

        add(endlessBtn);
        add(timeBtn);
        add(moveBtn);

        setVisible(true);
    }

    private void launchGame(GameMode mode) {
        dispose(); // Close menu
        new CandyCrushGUI(mode); // Pass mode to game
    }
}