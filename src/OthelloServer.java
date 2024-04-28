import java.net.*;
import java.io.*;
import java.util.concurrent.*;

class OthelloHandler implements Runnable {
    private Socket clientSocket;
    private OthelloGame game;

    public OthelloHandler(Socket socket) {
        this.clientSocket = socket;
        this.game = new OthelloGame();
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            out.println(game.displayBoardAsString());
            out.println("END OF BOARD");
            out.flush();

            String inputLine;
            while ((inputLine = in.readLine()) != null && !game.isGameOver()) {
                int move = Integer.parseInt(inputLine);
                String response = game.processMove(move);  // Assume this returns a string now

                if (response.startsWith("Invalid move")) {
                    out.println(response);
                } else {
                    game.computerMove();  // Let the computer make a move if player's move was valid
                }

                out.println(game.displayBoardAsString());
                out.println("END OF BOARD");

                if (game.isGameOver()) {
                    String winnerMessage = "Game over. Winner: " + game.getWinnerText();
                    out.println(winnerMessage);
                    break;
                }
            }
            out.flush();
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
}

    public class OthelloServer {
    public static void main(String[] args) {
        int port = 3333;
        ExecutorService pool = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                pool.submit(new OthelloHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }
}


