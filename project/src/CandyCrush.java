import java.util.*;

public class CandyCrush {
    static final int SIZE = 5;
    static final char[] CANDIES = {'A', 'B', 'C', 'D', 'E'};
    static char[][] board = new char[SIZE][SIZE];
    static Random random = new Random();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        initBoard();
        printBoard();

        while (true) {
            System.out.println("Enter swap (row1 col1 row2 col2) or -1 to quit:");
            int r1 = sc.nextInt();
            if (r1 == -1) break;
            int c1 = sc.nextInt();
            int r2 = sc.nextInt();
            int c2 = sc.nextInt();

            if (isValidSwap(r1, c1, r2, c2)) {
                swap(r1, c1, r2, c2);
                crushMatches();
                dropCandies();
                refillBoard();
                printBoard();
            } else {
                System.out.println("Invalid move!");
            }
        }
        sc.close();
    }

    static void initBoard() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                board[i][j] = randomCandy();
    }

    static char randomCandy() {
        return CANDIES[random.nextInt(CANDIES.length)];
    }

    static void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++)
                System.out.print(board[i][j] + " ");
            System.out.println();
        }
    }

    static boolean isValidSwap(int r1, int c1, int r2, int c2) {
        return Math.abs(r1 - r2) + Math.abs(c1 - c2) == 1;
    }

    static void swap(int r1, int c1, int r2, int c2) {
        char temp = board[r1][c1];
        board[r1][c1] = board[r2][c2];
        board[r2][c2] = temp;
    }

    static void crushMatches() {
        boolean[][] toCrush = new boolean[SIZE][SIZE];

        // Check rows
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE - 2; j++) {
                char c = board[i][j];
                if (c != ' ' && c == board[i][j + 1] && c == board[i][j + 2]) {
                    toCrush[i][j] = toCrush[i][j + 1] = toCrush[i][j + 2] = true;
                }
            }
        }

        // Check columns
        for (int j = 0; j < SIZE; j++) {
            for (int i = 0; i < SIZE - 2; i++) {
                char c = board[i][j];
                if (c != ' ' && c == board[i + 1][j] && c == board[i + 2][j]) {
                    toCrush[i][j] = toCrush[i + 1][j] = toCrush[i + 2][j] = true;
                }
            }
        }

        // Crush
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (toCrush[i][j])
                    board[i][j] = ' ';
    }

    static void dropCandies() {
        for (int j = 0; j < SIZE; j++) {
            int emptyRow = SIZE - 1;
            for (int i = SIZE - 1; i >= 0; i--) {
                if (board[i][j] != ' ') {
                    board[emptyRow][j] = board[i][j];
                    if (emptyRow != i) board[i][j] = ' ';
                    emptyRow--;
                }
            }
        }
    }

    static void refillBoard() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (board[i][j] == ' ')
                    board[i][j] = randomCandy();
    }
}
