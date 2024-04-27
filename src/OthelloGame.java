import java.util.ArrayList;
import java.util.List;

public class OthelloGame {
    public static final char PLAYER = 'O';
    public static final char COMPUTER = 'X';
    private static final char EMPTY = '.';
    private char[][] board;
    private int size = 4;  // Typically Othello is played on an 8x8 board

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

    public String displayBoardAsString() {
        StringBuilder sb = new StringBuilder();
        int cellNumber = 1;
        sb.append("  +");
        for (int i = 0; i < size; i++) {
            sb.append("---");
        }
        sb.append("+\n");
        for (int i = 0; i < size; i++) {
            sb.append((i+1) + " |");
            for (int j = 0; j < size; j++) {
                if (board[i][j] == EMPTY) {
                    sb.append(String.format("%2d ", cellNumber));
                } else {
                    sb.append(" " + board[i][j] + " ");
                }
                cellNumber++;
            }
            sb.append("|\n");
        }
        sb.append("  +");
        for (int i = 0; i < size; i++) {
            sb.append("---");
        }
        sb.append("+\n");
        System.out.println(sb.toString());
        return sb.toString();
    }

    public List<int[]> getValidMoves(char player) {
        List<int[]> validMoves = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == EMPTY && isValidMove(player, i, j)) {
                    validMoves.add(new int[]{i, j});
                }
            }
        }
        return validMoves;
    }

    private boolean isValidMove(char player, int row, int col) {
        if (board[row][col] != EMPTY) return false;

        char opponent = (player == PLAYER) ? COMPUTER : PLAYER;
        int[] directions = {-1, 0, 1}; // to check all directions around a tile

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

    public void flipTiles(char player, int row, int col) {
        if (!isValidMove(player, row, col)) return;

        board[row][col] = player;
        char opponent = (player == PLAYER) ? COMPUTER : PLAYER;
        int[] directions = {-1, 0, 1};

        for (int dx : directions) {
            for (int dy : directions) {
                if (dx == 0 && dy == 0) continue;

                int x = row + dx, y = col + dy;
                List<int[]> tilesToFlip = new ArrayList<>();
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

    public boolean isGameOver() {
        return getValidMoves(PLAYER).isEmpty() && getValidMoves(COMPUTER).isEmpty();
    }

    public char getWinner() {
        int playerCount = 0, computerCount = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == PLAYER) playerCount++;
                else if (board[i][j] == COMPUTER) computerCount++;
            }
        }
        return playerCount > computerCount ? PLAYER : (playerCount < computerCount ? COMPUTER : EMPTY);
    }

    // This method processes a player's move and returns the result
    public String processMove(int moveNumber) {
        int row = (moveNumber - 1) / size;
        int col = (moveNumber - 1) % size;

        if (isValidMove(PLAYER, row, col)) {
            flipTiles(PLAYER, row, col);
            return "Player move processed. " + displayBoardAsString();
        } else {
            return "Invalid move, please try again.";
        }
    }

    private int countFlips(char player, int row, int col) {
        if (!isValidMove(player, row, col)) {
            return 0; // No tiles flipped if the move is invalid
        }

        char opponent = (player == PLAYER) ? COMPUTER : PLAYER;
        int[] directions = {-1, 0, 1};
        int totalFlips = 0;

        // Check all directions
        for (int dx : directions) {
            for (int dy : directions) {
                if (dx == 0 && dy == 0) continue;

                int x = row + dx, y = col + dy;
                List<int[]> tilesToFlip = new ArrayList<>();
                while (x >= 0 && x < size && y >= 0 && y < size && board[x][y] == opponent) {
                    tilesToFlip.add(new int[]{x, y});
                    x += dx;
                    y += dy;
                }
                if (x >= 0 && x < size && y >= 0 && y < size && board[x][y] == player) {
                    totalFlips += tilesToFlip.size();
                }
            }
        }
        return totalFlips;
    }



    public void computerMove() {
        List<int[]> validMoves = getValidMoves(COMPUTER);
        if (!validMoves.isEmpty()) {
            int[] bestMove = null;
            int maxFlips = 0; // Keep track of the maximum number of tiles flipped by a move

            // Evaluate each move
            for (int[] move : validMoves) {
                int flips = countFlips(COMPUTER, move[0], move[1]);
                if (flips > maxFlips) {
                    maxFlips = flips;
                    bestMove = move;
                }
            }

            if (bestMove != null) {
                flipTiles(COMPUTER, bestMove[0], bestMove[1]);
            }
        }
    }


    public String getWinnerText() {
        char winner = getWinner();
        if (winner == PLAYER) {
            return "Player";
        } else if (winner == COMPUTER) {
            return "Computer";
        } else {
            return "Draw";
        }
    }




}
