import java.net.*;
import java.io.*;

public class OthelloServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(3333);
        System.out.println("Server started. Waiting for connection...");
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected.");

        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        OthelloGame game = new OthelloGame();
        out.println(game.displayBoardAsString());

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            try {
                int move = Integer.parseInt(inputLine);
                String response = game.processMove(move);
                out.println(response);
            } catch (NumberFormatException e) {
                out.println("Please enter a valid number.");
            }
        }

        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }
}
