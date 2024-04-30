//Nick presents OthelloGame class
import java.util.ArrayList;
import java.util.List;

public class OthelloGame {
    public static final char PLAYER = 'O'; // Dedicated player character
    public static final char COMPUTER = 'X'; // Dedicated computer character
    private static final char EMPTY = '.'; // Initial blank board character
    private char[][] board; // Initializing 2d character array
    private int size = 4;  // Typically Othello is played on a 8x8 board, but we're using 4x4 for ease of testing

    public OthelloGame() { // Setting up the board and filling all the pieces with the EMPTY spaces
        board = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = EMPTY;
            }
        }
        board[size / 2 - 1][size / 2 - 1] = COMPUTER; //Sets the initial computer pieces on the board
        board[size / 2][size / 2] = COMPUTER;
        board[size / 2 - 1][size / 2] = PLAYER; //Sets the initial player pieces on the board
        board[size / 2][size / 2 - 1] = PLAYER;
    }

    public String displayBoardAsString() { // Function used to display board
        StringBuilder sb = new StringBuilder(); // We use string builder to make use of its append function
                                                // We ended up choosing a StringBuilder object after having trouble sending the server properly
        int cellNumber = 1; // Setting the first cell number as 1
        sb.append("  +"); // Starting the board at the top left corner with a +
        for (int i = 0; i < size; i++) { // This fills out the rest of the top with dashes
            sb.append("---");
        }
        sb.append("+\n"); // At the end of the board we add a + and start the next line
        for (int i = 0; i < size; i++) { // Here we start to fill the entirety of the rest of the board
            sb.append(" |"); // This indicates the left edge of the board
            for (int j = 0; j < size; j++) { // This loop just checks for every empty piece on the board, set it to the cell number and increment
                                            //      that number by 1
                if (board[i][j] == EMPTY) {
                    sb.append(String.format("%2d ", cellNumber)); // Here we actually append the string while formatting it at the same time
                                                                    //  again we ran into issues and this seemed to fix it
                } else {
                    sb.append(" " + board[i][j] + " "); // Adding proper spacing
                }
                cellNumber++; // Incrementing cellNumber
            }
            sb.append("|\n"); // Adding the edge and next line
        }
        sb.append("  +");
        for (int i = 0; i < size; i++) {
            sb.append("---");
        }
        sb.append("+\n");
        System.out.println(sb.toString());
        return sb.toString(); // Sending the result to a string to be ready to send to the server
    }

    public List<int[]> getValidMoves(char player) {
        List<int[]> validMoves = new ArrayList<>(); // Here we create a new ArrayList to keep track of all the available spaces the player can
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == EMPTY && isValidMove(player, i, j)) { // checks if is a valid move
                    validMoves.add(new int[]{i, j}); //Adds the position to the valid moves array
                }
            }
        }
        return validMoves;
    }

    private boolean isValidMove(char player, int row, int col) { // Checks if the player can actually make the move
        if (board[row][col] != EMPTY) return false; // Returns false if the position has already been played

        char opponent;
        if (player == PLAYER) { // Sets who is the opponent
            opponent = COMPUTER;
        } else {
            opponent = PLAYER;
        }

        int[] dir = {-1, 0, 1}; // to check all dir around a tile

        for (int dx : dir) { // This just iterates through each direction and sets it to the dx
            for (int dy : dir) { // This does the same but for the dy
                if (dx == 0 && dy == 0) continue; //Checks if the selected block is the center piece, and if it is, just skip over

                int x = row + dx, y = col + dy; // sets x and y locations
                boolean hasOpponentBetween = false; // Initially sets has Opponent Between as false
                while (x >= 0 && x < size && y >= 0 && y < size && board[x][y] == opponent) { // This actually checks if there is an opponent in between
                                                                                            // If there is set there is an opponent and iterate the x and y
                    x += dx;
                    y += dy;
                    hasOpponentBetween = true;
                }

                if (hasOpponentBetween && x >= 0 && x < size && y >= 0 && y < size && board[x][y] == player) { //
                    return true;
                }
            }
        }
        return false;
    }

    public void flipTiles(char player, int row, int col) {
        if (!isValidMove(player, row, col)) return;

        board[row][col] = player;
        char opponent;
        if (player == PLAYER) { // Sets who is the opponent
            opponent = COMPUTER;
        } else {
            opponent = PLAYER;
        }
        
        int[] dir = {-1, 0, 1};

        for (int dx : dir) {
            for (int dy : dir) {
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
        boolean noPlayerMoves = getValidMoves(PLAYER).isEmpty();
        boolean noComputerMoves = getValidMoves(COMPUTER).isEmpty();
        System.out.println("Checking game over: Player moves left? " + !noPlayerMoves + ", Computer moves left? " + !noComputerMoves);
        return noPlayerMoves && noComputerMoves;
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

        char opponent;
        if (player == PLAYER) { // Sets who is the opponent
            opponent = COMPUTER;
        } else {
            opponent = PLAYER;
        }
        
        int[] dir = {-1, 0, 1};
        int totalFlips = 0;

        // Check all dir
        for (int dx : dir) {
            for (int dy : dir) {
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
