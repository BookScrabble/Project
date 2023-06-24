package model.logic.client;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket server;
    private Scanner inFromServer;
    private PrintWriter outToServer;
    private StringProperty messageFromHost;
    public IntegerProperty playerId;
    public StringProperty playTurn;

    public static volatile boolean serverIsRunning = true;
    /**
     * Client constructor which both initialize the client parameters but also
     * opens a thread for this client to listen on.
     * @param ip - Ip of the Host server.
     * @param port - Port 0f the Host server.
     * @param clientName - Client name for the game.
     */
    public Client(String ip, int port, String clientName){
        try {
            this.server = new Socket(ip, port);
            this.outToServer = new PrintWriter(this.server.getOutputStream(), true);
            this.inFromServer = new Scanner(this.server.getInputStream());
            outToServer.println(clientName);
            playerId = new SimpleIntegerProperty();
            playerId.setValue(Integer.parseInt(inFromServer.next()));
            this.messageFromHost = new SimpleStringProperty();
            this.playTurn = new SimpleStringProperty();
            playTurn.addListener(((observable, oldAction, newAction) -> {
                playTurn(newAction);
            }));
            listenForServerUpdates();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listenForServerUpdates() {
        new Thread(() -> {
            String msgFromServer;
            while (!server.isClosed() && serverIsRunning) {
                if(inFromServer.hasNextLine()){
                    msgFromServer = inFromServer.nextLine();
                    switch (msgFromServer) {
                        case "DictionaryNotLegal" -> wordNotFoundInDictionary();
                        case "BoardNotLegal" -> boardPlacementIllegal();
                        case "loadBoard" -> loadBoard();
                        case "bindButtons" -> bindButtons();
                        case "TurnDone" -> turnDone();
                        default -> System.out.println(msgFromServer);
                    }
                }
            }
            closeEverything();
        }).start();
    }

    public void playTurn(String action){
        System.out.println("sending action to server");
        outToServer.println(action);
    }

    public StringProperty getMessageFromHost() {
        return messageFromHost;
    }


    public void loadBoard(){
        messageFromHost.setValue("loadBoard");
    }

    public void bindButtons(){
        messageFromHost.setValue("bindButtons");
    }

    private void wordNotFoundInDictionary() {
        System.out.println("Place a different word or challenge");
    }

    private void boardPlacementIllegal() {
        System.out.println("Tiles are placed in illegal place");
    }

    private void turnDone(){
        System.out.println("Finished turn");
    }

    private void closeEverything() {
        try {
            System.out.println("Closing Client");
            this.server.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
