import java.net.*;
import java.io.*;
import java.util.Scanner;

public class OthelloClient {
    public static void main(String[] args) {
        String host = "localhost"; // Host sets the IP address that the client is being sent to
        int port = 3333; // Port sets the port the client will be using

        try (Socket socket = new Socket(host, port); // Tries to set up a new socket using the IP and port given
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Tries to set up a BufferedReader object using ...
                                                                                                    //      the InputStream
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // Tries to set up a printWriter object to communicate with the ...
                                                                                        //      server
             Scanner scanner = new Scanner(System.in)) { // Tries to set up a Scanner object to retrieve what the user is typing

            String fromServer;

            while ((fromServer = in.readLine()) != null) { // Takes an input from the server and runs this code while the input isn't null

                if (fromServer.equals("END OF BOARD")) { // Checks if the server has sent the End of Board line and if it has, it knows when the board ...
                                                        //      is stop being printed and when the user can proceed with their turn

                    System.out.println("Enter your move (number space): ");  // Asks the user to enter which space they would like to move to

                    String userMove = scanner.nextLine(); // Takes the user's answer

                    out.println(userMove); // Sends the user's move to the server to be processed

                } else if (fromServer.startsWith("Game over")) { // Checks if the game is over or not

                    System.out.println(fromServer); // Sends over the game over message to the user, displaying who won

                    break; // Breaks out of the loop since the game has ended for the user

                } else { // If anything else gets sent to the user, print out that message

                    System.out.println(fromServer);

                }
            }
        } catch (UnknownHostException e) { // Throws an exception for if a host is not found

            System.err.println("Host not found: " + e.getMessage());

        } catch (IOException e) { // Throws an IOException

            System.err.println("I/O Error: " + e.getMessage());

        }
    }
}
