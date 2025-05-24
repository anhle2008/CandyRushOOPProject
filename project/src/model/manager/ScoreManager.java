package model.manager;

import model.config.GameConfig;
import model.core.GameMode;

/**
 * Handles score calculation and callback updates.
 */
public class ScoreManager {
    private int totalScore = 0;

    public void addPoints(int candyCount) {
        totalScore += candyCount * GameConfig.SCORE_PER_CANDY;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getMaximumScore(GameMode mode) {
        if (mode != GameMode.ENDLESS) return GameConfig.SCORE_GOAL;

        return 0; // Endless mode does not have one
    }
}