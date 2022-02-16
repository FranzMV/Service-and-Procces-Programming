package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <h1>Bingo Server</h1>
 * Main class of the server side.
 * @author Francisco David Manzanedo Valle.
 * @version 1.0
 */
public class BingoServer {

    /**
     * Max numbers of players.
     */
    public static final int MAX_CONNECTIONS = 3;
    /**
     * Max number of numbers in the bingo.
     */
    public static final int MAX_NUMBER = 20;
    /**
     * Connection Port.
     */
    public static final int PORT = 6000;
    /**
     * List of integers to store all the numbers in the game.
     */
    public static List<Integer> allNumbers;
    /**
     * Boolean to check if the game has finished.
     */
    public static Boolean winner;


    /**
     * Main method of the class to connect the players.
     * @param args  Console args.
     */

    public static void main(String[] args) {

        // Gets all the numbers (1, 20)
        allNumbers = getNumbers();

        //To store each client connection to the server
        ArrayList<Socket> sockets = new ArrayList<>();

        try (ServerSocket server = new ServerSocket(PORT)) {
            int clientsConnected = 0;
            System.out.println("Waiting 3 players for playing");
            while (clientsConnected < MAX_CONNECTIONS) {
                Socket service = server.accept();
                clientsConnected++;
                sockets.add(service);
                System.out.println("Connection established");
                System.out.println("Number of players: "+clientsConnected);
            }
            for (int i = 0; i < MAX_CONNECTIONS; i++) {
                ServerThread serverThread = new ServerThread(sockets.get(i));
                serverThread.start();

            }
        } catch (IOException e) {
            System.out.println("An error occurred connecting to the client.");
            e.printStackTrace();
        }
    }

    /**
     * Gets a List of integers with 20 numbers shuffled.
     * @return A List of integers with 20 numbers shuffled.
     */
    private static List<Integer> getNumbers(){
        List<Integer> numbers = IntStream.rangeClosed(1, MAX_NUMBER)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(numbers);

        return numbers;
    }
}
