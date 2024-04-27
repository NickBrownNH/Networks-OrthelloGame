import java.net.*;
import java.io.*;
import java.util.Scanner;

public class OthelloClient {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 3333;

        try (Socket socket = new Socket(host, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            String fromServer;
            while ((fromServer = in.readLine()) != null) {
                if (fromServer.equals("END OF BOARD")) {
                    System.out.println("Enter your move (number space): ");
                    String userMove = scanner.nextLine();
                    out.println(userMove);
                } else if (fromServer.startsWith("Game over")) {
                    System.out.println(fromServer);
                    break;
                } else {
                    System.out.println(fromServer);
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Host not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
        }
    }
}
