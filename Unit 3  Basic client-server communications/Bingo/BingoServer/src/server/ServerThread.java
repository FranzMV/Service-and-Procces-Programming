package server;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <h1>Server Thread</h1>
 * Thread to manage every player in the server side.
 * @author Francisco David Manzanedo Valle.
 * @version 1.0
 */
public class ServerThread extends Thread{

    /**
     * Max number of number in the bing
     * */
    private static final int MAX_NUMBERS = 20;
    /**
     *Creates a stream socket and connects it to the client (with specified port number at the specified IP address).
     */
    private final Socket service;

    /**
     * Constructor to instantiate the service received by BingoServer class.
     * @param service A stream socket to connects it to the client
     */
    public ServerThread(Socket service){ this.service = service; }

    @Override
    public void run() {

        ObjectInputStream objectSocketIn = null;
        ObjectOutputStream objectSocketOut = null;

            try {

                objectSocketOut = new ObjectOutputStream(service.getOutputStream());
                objectSocketIn = new ObjectInputStream(service.getInputStream());

                //Send the ticket to the client
                objectSocketOut.writeObject(getTicket());
                objectSocketOut.flush();

                int count = 0;
                do {
                    //Read the boolean sent by the client
                    BingoServer.winner = objectSocketIn.readBoolean();
                    //Waits 2 seconds in each iteration
                    Thread.sleep(2000);
                    System.out.println("Sent "+(count+1)+": "+BingoServer.winner);
                    if (!BingoServer.winner) {
                        //Send numbers to the client one by one that are stored in BingoServer.allNumbers
                        objectSocketOut.writeInt(BingoServer.allNumbers.get(count));
                        objectSocketOut.flush();
                    } else {
                        objectSocketOut.writeInt(-1);
                        objectSocketOut.flush();
                    }
                    count++;
                } while (!BingoServer.winner && count < BingoServer.MAX_NUMBER);
                objectSocketOut.writeInt(-1);
                objectSocketOut.flush();

            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            } finally { closeResources(service, objectSocketOut, objectSocketIn); }
    }



    /**
     *Generate an Integer List with 20 numbers shuffled and select five numbers (one ticket) sorted in ascending order.
     * @return an Integer List with 5 numbers sorted in ascending order.
     */
    private static List<Integer> getTicket(){
        List<Integer> numbers = IntStream.rangeClosed(1,MAX_NUMBERS)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(numbers);

        return numbers.stream().limit(5).sorted().collect(Collectors.toList());
    }


    /**
     * Close the resources used in the connection.
     * @param service Socket corresponding to the client connection.
     * @param objectSocketOut ObjectOutputStream corresponding to the stream send to the client.
     * @param objectSocketIn  ObjectInputStream corresponding to the client stream input.
     */
    private void closeResources(Socket service, ObjectOutputStream objectSocketOut, ObjectInputStream objectSocketIn){
        try{
            if(objectSocketOut != null)
                objectSocketOut.close();
        }catch (IOException ex){
            System.out.println("Error closing client output stream.");
            ex.printStackTrace();
        }

        try{
            if(objectSocketIn != null)
                objectSocketIn.close();
        }catch (IOException ex){
            System.out.println("Error closing client input stream.");
            ex.printStackTrace();
        }

        try{
            if(service != null)
                service.close();
        }catch (IOException ex){
            System.out.println("Error closing client service.");
            ex.printStackTrace();
        }
    }
}
