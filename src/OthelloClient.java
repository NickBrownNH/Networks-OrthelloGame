import java.net.*;
import java.io.*;
import java.util.*;

public class OthelloClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 3333);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);

        String fromServer;
        while ((fromServer = in.readLine()) != null) {
            System.out.println("Server: " + fromServer);
            if (fromServer.contains("Game over")) {
                break;
            }
            System.out.print("Enter your move: ");
            String fromUser = scanner.nextLine();
            if (fromUser != null) {
                out.println(fromUser);
            }
        }

        scanner.close();
        in.close();
        out.close();
        socket.close();
    }
}
