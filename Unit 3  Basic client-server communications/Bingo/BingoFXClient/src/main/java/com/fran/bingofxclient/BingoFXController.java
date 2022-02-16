package com.fran.bingofxclient;

import com.fran.bingofxclient.utils.MessageUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * <h1> BingoFXController</h1>
 * Controller to manage the main view of the program.
 * @author Francisco David Manzanedo Valle.
 * @version 1.0
 */
public class BingoFXController implements Initializable {

    @FXML private TextField txtHost;
    @FXML private TextField txtPort;
    @FXML private Label lblNum1;
    @FXML private Label lblNum2;
    @FXML private Label lblNum3;
    @FXML private Label lblNum4;
    @FXML private Label lblNum5;
    @FXML private Label lblResult;

    /**
     *List of Integers that contains the five numbers sent by the server.
     */
    public List<Integer> ticket;
    /**
     *Boolean to check if any client win the game.
     */
    private boolean result;
    /**
     *Integer corresponding to the number sent by the server.
     */
    private int receivedNumber;
    /**
     *Integer corresponding to a counter of matches between the ticket and the numbers sent by the server.
     */
    private int countOfMatches;
    /**
     *Socket to connect to the server.
     */
    private Socket service;
    /**
     *ObjectOutputStream to send a stream of data to the server.
     */
    private ObjectOutputStream socketOut;
    /**
     *ObjectInputStream to receive a stream of data from the server.
     */
    private ObjectInputStream socketIn;

    /**
     *Style that the label gets when a number of the ticket match with a number sent by the server.
     */
    private static final String MATCH_NUMBER_STYLE =
            "    -fx-border-radius: 50%;\n" +
            "    -fx-width: 36px;\n" +
            "    -fx-height: 36px;\n" +
            "    -fx-padding: 8px;\n" +
            "    -fx-background: #00ff00;\n" +
            "    -fx-border: 2px;\n" +
            "    -fx-border-style: solid;\n" +
            "    -fx-border-color: #666;\n" +
            "    -fx-color: #666;\n" +
            "    -fx-background-color:#00ff00;\n"+
            "    -fx-background-radius: 50px;\n"+
            "    -fx-text-align: center;";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtHost.setText("localhost");
        txtPort.setText("6000");
        clearLabels();
        ticket = new ArrayList<>();
        result = false;
        countOfMatches = 0;
    }

    /**
     *Button event corresponding when the client connect to the server.
     * @param actionEvent ActionEvent
     * @throws IOException IOException
     */
    @FXML
    private void connectToServer(ActionEvent actionEvent) throws IOException{
        if(!txtHost.getText().isEmpty() || !txtPort.getText().isEmpty()){

            service = new Socket(txtHost.getText(), Integer.parseInt(txtPort.getText()));
            socketOut = new ObjectOutputStream(service.getOutputStream());
            socketIn = new ObjectInputStream(service.getInputStream());

            // Task to read the ticket sent from the server a fill the labels with the numbers received.
            Task getTicketFromServer = new Task<List<Integer>>() {
                @Override
                public List<Integer> call() {
                    try {
                        ticket = (List<Integer>) socketIn.readObject();
                        System.out.println(ticket);
                        if (!ticket.isEmpty()) setUpTicket(ticket);

                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("An error occurred receiving the ticket from the server");
                        e.printStackTrace();
                    }
                    return ticket;
                }
            };
            getTicketFromServer.run();

            // Receive the numbers from the ServerThread, count the matches and update if the number received match.
            //with any of the ticket numbers.
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        do {
                            socketOut.writeBoolean(result);
                            socketOut.flush();
                            receivedNumber = socketIn.readInt();
                            System.out.println("RECEIVED: " + receivedNumber);
                            if (receivedNumber != -1) {
                                if (ticket.contains(receivedNumber)) {
                                    int position = ticket.indexOf(receivedNumber);
                                    switch (position) {
                                        case 0: setLabelStyle(receivedNumber, lblNum1); break;
                                        case 1: setLabelStyle(receivedNumber, lblNum2); break;
                                        case 2: setLabelStyle(receivedNumber, lblNum3); break;
                                        case 3: setLabelStyle(receivedNumber, lblNum4); break;
                                        case 4: setLabelStyle(receivedNumber, lblNum5); break;
                                    }
                                    countOfMatches++;
                                    System.out.println("COUNT OF MATCHES : " + countOfMatches);
                                }
                                if (countOfMatches >= 5) result = true;
                            }
                            setResult(result, receivedNumber);

                        } while (!result || receivedNumber != -1);

                    } catch (IOException e) {
                        System.out.println("An error occurred receiving the numbers from the server.");
                        e.printStackTrace();

                    } finally { closeResources(service, socketOut, socketIn); }
                }
            }).start();


        }else MessageUtils.showError("Error", "The fields cannot be empty");
    }


    /**
     *Set's the values send by the server in the ticket in each label.
     * @param ticket List of Integers that contains the five numbers of the ticket.
     */
    private void setUpTicket(List<Integer> ticket){
        lblNum1.setText(ticket.get(0).toString());
        lblNum2.setText(ticket.get(1).toString());
        lblNum3.setText(ticket.get(2).toString());
        lblNum4.setText(ticket.get(3).toString());
        lblNum5.setText(ticket.get(4).toString());
    }

    /**
     * Check if any player has won the game.
     * @param result Boolean flat to check the winner.
     * @param receivedNumber Integer sent by the server to check if the value is -1.
     */
    private void setResult(boolean result, int receivedNumber) {

        if(!result && receivedNumber == -1){
            Platform.runLater(() -> lblResult.setText("YOU LOSE!"));
        }

        if(result) {
            Platform.runLater(() -> lblResult.setText("YOU WIN!"));
        }

    }

    /**
     * Sets the label style when a ticket number match with the number sent by the sever.
     * @param num Integer corresponding with the number sent by the server.
     * @param label A label that's match the number sent by the server.
     */
    private void setLabelStyle(int num, Label label){
        Platform.runLater(()->{
            label.setText(String.valueOf(num));
            label.setStyle(MATCH_NUMBER_STYLE);
        });
    }

    /**
     *Clear all the labels in the view.
     */
    private void clearLabels(){
        lblResult.setText("");
        lblNum1.setText("");
        lblNum2.setText("");
        lblNum3.setText("");
        lblNum4.setText("");
        lblNum5.setText("");
    }

    /**
     * Closes all the resources used to establish the connection with the server and the data streams
     * @param service Socket stream to connect to the server.
     * @param socketOut ObjectOutputStream to send data to the server.
     * @param socketIn ObjectInputStream to receive data from the server.
     */
    private void closeResources(Socket service ,ObjectOutputStream socketOut, ObjectInputStream socketIn){
        try {
            if(socketIn != null)
                socketIn.close();
        } catch (IOException e) {
            System.out.println("Error cerrando socketIN");
            e.printStackTrace();
        }
        try{
            if(socketOut != null)
                socketOut.close();
        }catch (IOException ex){
            System.out.println("Error cerrando socketOut");
            ex.printStackTrace();
        }

        try {
            if (service != null)
                service.close();
        }catch (IOException ex){
            System.out.println("Error cerrando el socket");
        }
    }
}