//Jon presents from line 1 - 54
import java.net.*;
import java.io.*;
import java.util.concurrent.*;

class OthelloHandler implements Runnable {
    private Socket clientSocket; // Instantiates a Socket as clientSocket
    private OthelloGame game; // Instantiates an OthelloGame object as game

    public OthelloHandler(Socket socket) { // This is the OthelloHandler constructor which creates a new socket and game for every new instance created
        this.clientSocket = socket; // Sets the client socket as the new socket
        this.game = new OthelloGame(); // sets the othello game as a new othello game
    }

    @Override
    public void run() { // This is like the main method when using a runnable class
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); // Tries to instantiate a BufferedReader
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) { // Tries to instantiate a PrintWriter

            out.println(game.displayBoardAsString()); // Sends client the initial game board
            out.println("END OF BOARD"); // Used to indicate the end of the board for the client reader

            String inputLine;
            while ((inputLine = in.readLine()) != null && !game.isGameOver()) {
                int move = Integer.parseInt(inputLine); // Sets the move what was given for the inputLine
                String response = game.processMove(move);  // Processes a move that will either update the game board or print out an error

                if (response.startsWith("Invalid move")) {
                    out.println(response); // Lets the User know they played an invalid more and to try again
                } else {
                    game.computerMove();  // Let the computer make a move if player's move was valid
                }

                out.println(game.displayBoardAsString()); //Prints out new game board with user's move and computers move now updated
                out.println("END OF BOARD"); //Used to indicate the end of the board for the client reader

                if (game.isGameOver()) { // Checks if the game is over
                    String winnerMessage = "Game over. Winner: " + game.getWinnerText(); // Makes the winner string and decides who won by running the ...
                                                                                        //      getWinnerText function from the game object

                    out.println(winnerMessage); // Sends out this message to the client
                    break; // This breaks out of the game loop
                }
            }
            out.flush(); // This flushes out the PrintWriter object out to ensure no problems when sending over multiple messages
        } catch (IOException e) { // Catches an IOException if one occurs
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try { // Tries to close the client socket
                clientSocket.close();
            } catch (IOException e) { // Catches an IOException if one occurs
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
}

//Dylan presents from here on down (The Server and Client Classes)
public class OthelloServer { // Instantiating the server class
    public static void main(String[] args) {
        int port = 3333; // Setting the port to 3333
        ExecutorService pool = Executors.newCachedThreadPool(); // This is what manages a group of runnable instances

        try (ServerSocket serverSocket = new ServerSocket(port)) { // Tries to open a new serverSocket at the port set previously

            System.out.println("Server is running on port " + port);

            while (true) { // Continuous loop to add new clientSockets handled by the OthelloHandler to the pool

                Socket clientSocket = serverSocket.accept(); // Sets the client socket as the incoming socket from the serverSocket
                pool.submit(new OthelloHandler(clientSocket)); // Adds the client to the pool which allows multiple clients to be ran at the same time

            }
        } catch (IOException e) { // Catches an IOException if one occurs

            System.err.println("Server exception: " + e.getMessage());

        } finally { // When finished, closes the pool

            pool.shutdown();

        }
    }
}