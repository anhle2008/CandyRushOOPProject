package model.logic;

import model.board.CandyCell;
import model.type.CandyType;

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
        for (int row = 0; row < size; row++) {
            for (int col = 0; col <= size - 3; col++) {
                CandyType c1 = board[row][col].getCandyType();
                CandyType c2 = board[row][col + 1].getCandyType();
                CandyType c3 = board[row][col + 2].getCandyType();

                if (c1 != null && c1.equals(c2) && c2.equals(c3)) {
                    matches[row][col] = matches[row][col + 1] = matches[row][col + 2] = true;
                }
            }
        }

        // Vertical
        for (int col = 0; col < size; col++) {
            for (int row = 0; row <= size - 3; row++) {
                CandyType c1 = board[row][col].getCandyType();
                CandyType c2 = board[row + 1][col].getCandyType();
                CandyType c3 = board[row + 2][col].getCandyType();

                if (c1 != null && c1.equals(c2) && c2.equals(c3)) {
                    matches[row][col] = matches[row + 1][col] = matches[row + 2][col] = true;
                }
            }
        }

        return matches;
    }

    /**
     * Check candies matches in the board.
     */
    public static boolean hasMatch(boolean[][] matchMatrix) {
        for (boolean[] row: matchMatrix) {
            for (boolean cell: row) {
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
        for (boolean[] row: matchMatrix) {
            for (boolean cell: row) {
                if (cell) count++;
            }
        }
        return count;
    }
}