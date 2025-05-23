package model.scoring;

import model.config.GameConfig;

/**
 * Handles score calculation and callback updates.
 */
public class ScoreManager {
    private int totalScore = 0;
    private Runnable updateCallback;

    public void addPoints(int candyCount) {
        totalScore += candyCount * GameConfig.SCORE_PER_CANDY;
        if (updateCallback != null) updateCallback.run();
    }

    public void setUpdateCallback(Runnable updateCallback) {
        this.updateCallback = updateCallback;
    }

    public int getTotalScore() {
        return totalScore;
    }
}