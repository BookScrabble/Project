package model.logic.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket server;
    private Scanner inFromServer;
    private PrintWriter outToServer;

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
                        case "playTurn" -> playTurn();
                        case "wordNotFoundInDictionary" -> wordNotFoundInDictionary();
                        case "boardPlacementIllegal" -> boardPlacementIllegal();
                        case "updateGameState" -> updateGameState();
                        default -> System.out.println(msgFromServer); //TODO - Implemented for testing.
                    }
                }
            }
            closeEverything();
        }).start();
    }


    /**
     * TODO - for now test implementation.
     */
    private void playTurn() {
        System.out.println("Turn started");
        outToServer.println("submit,BELIEVE,7,7,vertical");
    }

    private void wordNotFoundInDictionary() {
        System.out.println("Place a different word or challenge");
    }

    private void boardPlacementIllegal() {
    }

    private void updateGameState() {
        System.out.println("Refreshing view...");
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
