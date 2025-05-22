package model;

/**
 * Handles finding of all matches (3 or more in a row or column).
 */
public class MatchFinder {
    /**
     * Detects all horizontal and vertical matches (3 or more).
     */
    public static boolean[][] findMatches(CandyCell[][] board) {
        int size = board.length;
        boolean[][] matches = new boolean[size][size];

        // Horizontal
        for (int i = 0; i < size; i++) {
            for (int j = 0; j <= size - 3; j++) {
                CandyType c1 = board[i][j].getCandyType();
                CandyType c2 = board[i][j + 1].getCandyType();
                CandyType c3 = board[i][j + 2].getCandyType();

                if (c1 != null && c1.equals(c2) && c2.equals(c3)) {
                    matches[i][j] = matches[i][j + 1] = matches[i][j + 2] = true;
                }
            }
        }

        // Vertical
        for (int j = 0; j < size; j++) {
            for (int i = 0; i <= size - 3; i++) {
                CandyType c1 = board[i][j].getCandyType();
                CandyType c2 = board[i + 1][j].getCandyType();
                CandyType c3 = board[i + 2][j].getCandyType();

                if (c1 != null && c1.equals(c2) && c2.equals(c3)) {
                    matches[i][j] = matches[i + 1][j] = matches[i + 2][j] = true;
                }
            }
        }

        return matches;
    }

    /**
     * Check candies matches in the board.
     */
    public static boolean hasMatch(boolean[][] matchMatrix) {
        for (boolean[] row : matchMatrix) {
            for (boolean cell : row) {
                if (cell) return true;
            }
        }
        return false;
    }

    /**
     * Counts how many candies are crushed.
     */
    public static int countMatches(boolean[][] matchMatrix) {
        int count = 0;
        for (boolean[] row : matchMatrix) {
            for (boolean cell : row) {
                if (cell) count++;
            }
        }
        return count;
    }
}