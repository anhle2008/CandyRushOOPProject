package model;

public interface GameStateListener {
    void onTimeUpdate(int newTimeSeconds);
    void onMovesUpdate(int remainingMoves);
    void onScoreUpdate(int newScore);
    void onGameOver();
}