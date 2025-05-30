package model.board;

import model.type.CandyType;
import util.CandyUtils;

/**
 * Manage the grid of candies (their state, dropping, refilling, swapping).
 */
public class GameBoard {
    private final CandyCell[][] board;
    private final int size;

    public GameBoard(CandyCell[][] board, int size) {
        this.board = board;
        this.size = size;
    }

    public CandyCell[][] getBoard() {
        return board;
    }

    /**
     * Swaps the appearance and type of two candy buttons.
     */
    public void swap(CandyCell a, CandyCell b) {
        CandyType temp = a.getCandyType();
        a.setCandyType(b.getCandyType());
        b.setCandyType(temp);
    }
    
    /**
     * Clears matched candies from the board.
     */
    public void crush(boolean[][] matches) {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (matches[row][col]) {
                    board[row][col].clearCandy();
                }
            }
        }
    }

    /**
     * Drops candies into empty slots from above.
     */
    public void dropCandies() {
        for (int column = 0; column < size; column++) {
            int emptyRow = size - 1;

            for (int row = size - 1; row >= 0; row--) {
                if (!board[row][column].isEmpty()) {
                    CandyType candy = board[row][column].getCandyType();
                    board[emptyRow][column].setCandyType(candy);

                    if (emptyRow != row) {
                        board[row][column].clearCandy();
                    }

                    emptyRow--;
                }
            }
        }
    }

    /**
     * Refills the board with random candies where empty.
     */
    public void refillBoard() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col].isEmpty()) {
                    CandyType candy = CandyUtils.getRandomCandy();
                    board[row][col].setCandyType(candy);
                }
            }
        }
    }
}