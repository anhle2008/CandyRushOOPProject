package model.config;

import java.awt.Font;

public class GameConfig {
    // Gameplay settings
    public static final int GRID_SIZE = 6;
    public static final int SCORE_PER_CANDY = 20;
    public static final int MAX_TIME_SECONDS = 60;
    public static final int MAX_MOVES = 20;
    public static final int SCORE_GOAL = 2500;

    // Animation settings
    public static final int SWAP_DELAY_MS = 120;
    public static final int CRUSH_BLINK_DELAY_MS = 180;
    public static final int POST_CRUSH_DELAY_MS = 100;

    // GUI settings
    public static final int WINDOW_WIDTH = 500;
    public static final int WINDOW_HEIGHT = 550;
    public static final int MENU_WIDTH = 300;
    public static final int MENU_HEIGHT = 200;
    public static final int ICON_SIZE = 72;
    public static final String FONT_NAME = "Arial";
    public static final int FONT_STYLE = Font.BOLD;
    public static final int FONT_SIZE = 20;
}