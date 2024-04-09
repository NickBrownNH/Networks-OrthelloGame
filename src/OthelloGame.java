import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OthelloGame {
    private static final char PLAYER = 'O';
    private static final char COMPUTER = 'X';
    private static final char EMPTY = '.';
    private char[][] board;
    private int size = 4;

    public OthelloGame() {
        board = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = EMPTY;
            }
        }
        board[size / 2 - 1][size / 2 - 1] = COMPUTER;
        board[size / 2][size / 2] = COMPUTER;
        board[size / 2 - 1][size / 2] = PLAYER;
        board[size / 2][size / 2 - 1] = PLAYER;
    }

    public void displayBoard() {
        int cellNumber = 1;
        System.out.println("  +- - - - - - - - - - - - - - - - -+");
        for (int i = 0; i < size; i++) {
            System.out.print("  | ");
            for (int j = 0; j < size; j++) {
                if (board[i][j] == EMPTY) {
                    System.out.printf(" %2d ", cellNumber);
                } else {
                    System.out.print(" " + board[i][j] + "  ");
                }
                cellNumber++;
            }
                System.out.println("|");
            }
         System.out.println("  +- - - - - - - - - - - - - - - - -+");
        }

    public List<int[]> getValidMoves(char player) {
        List<int[]> validMoves = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == EMPTY) {
                    if (isValidMove(player, i, j)) {
                        validMoves.add(new int[]{i, j});
                    }
                }
            }
        }
        return validMoves;
    }

    private boolean isValidMove(char player, int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size || board[row][col] != EMPTY) return false;

        char opponent = (player == PLAYER) ? COMPUTER : PLAYER;
        int[] directions = {-1, 0, 1};

        for (int dx : directions) {
            for (int dy : directions) {
                if (dx == 0 && dy == 0) continue;

                int x = row + dx, y = col + dy;
                boolean hasOpponentBetween = false;
                while (x >= 0 && x < size && y >= 0 && y < size && board[x][y] == opponent) {
                    x += dx;
                    y += dy;
                    hasOpponentBetween = true;
                }
                if (hasOpponentBetween && x >= 0 && x < size && y >= 0 && y < size && board[x][y] == player) {
                    return true;
                }
            }
        }
        return false;
    }

    private void flipTiles(char player, int row, int col) {
        if (!isValidMove(player, row, col)) return;

        board[row][col] = player;
        char opponent = (player == PLAYER) ? COMPUTER : PLAYER;
        int[] directions = {-1, 0, 1};

        for (int dx : directions) {
            for (int dy : directions) {
                if (dx == 0 && dy == 0) continue;

                List<int[]> tilesToFlip = new ArrayList<>();
                int x = row + dx, y = col + dy;
                while (x >= 0 && x < size && y >= 0 && y < size && board[x][y] == opponent) {
                    tilesToFlip.add(new int[]{x, y});
                    x += dx;
                    y += dy;
                }
                if (x >= 0 && x < size && y >= 0 && y < size && board[x][y] == player) {
                    for (int[] tile : tilesToFlip) {
                        board[tile[0]][tile[1]] = player;
                    }
                }
            }
        }
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            displayBoard();
            List<int[]> validMoves = getValidMoves(PLAYER);
            if (validMoves.isEmpty()) break;
            int moveNumber = 0;
            while (true) {
                System.out.print("Select your move (1-" + (size * size) + "): ");
                moveNumber = scanner.nextInt();
                int row = (moveNumber - 1) / size;
                int col = (moveNumber - 1) % size;

                if (isValidMove(PLAYER, row, col)) {
                    flipTiles(PLAYER, row, col);
                    break;
                } else {
                    System.out.println("Invalid move, please try again.");
                }
            }

            System.out.println("Player's move:");
            displayBoard();

            List<int[]> computerValidMoves = getValidMoves(COMPUTER);
            if (computerValidMoves.isEmpty()) break;

            int[] bestMove = null;
            int maxFlips = -1;
            for (int[] move : computerValidMoves) {
                int flips = countFlips(COMPUTER, move[0], move[1]);
                if (flips > maxFlips) {
                    bestMove = move;
                    maxFlips = flips;
                }
            }

            if (bestMove != null) {
                flipTiles(COMPUTER, bestMove[0], bestMove[1]);
            }
            System.out.println("Computer's move:");
        }

        scanner.close();
    }

    private int countFlips(char player, int row, int col) {
        int flips = 0;
        if (!isValidMove(player, row, col)) return flips;

        char opponent = (player == PLAYER) ? COMPUTER : PLAYER;
        int[] directions = {-1, 0, 1};

        for (int dx : directions) {
            for (int dy : directions) {
                if (dx == 0 && dy == 0) continue;

                List<int[]> tilesToFlip = new ArrayList<>();
                int x = row + dx, y = col + dy;
                while (x >= 0 && x < size && y >= 0 && y < size && board[x][y] == opponent) {
                    tilesToFlip.add(new int[]{x, y});
                    x += dx;
                    y += dy;
                }
                if (x >= 0 && x < size && y >= 0 && y < size && board[x][y] == player) {
                    flips += tilesToFlip.size();
                }
            }
        }

        return flips;
    }

    public static void main(String[] args) {
        OthelloGame game = new OthelloGame();
        game.play();
    }
}

