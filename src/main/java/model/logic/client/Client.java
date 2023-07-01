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
                if(newAction.equals("reset")) return;
                playTurn(newAction);
            }));
            listenForServerUpdates();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The listenForServerUpdates function is a function that listens for updates from the server.
     * It does this by creating a new thread, and then running an infinite loop in which it checks if there are any messages from the server.
     * If there are, it will check what message was sent and act accordingly.
     */
    public void listenForServerUpdates() {
        new Thread(() -> {
            String msgFromServer;
            while (!server.isClosed() && serverIsRunning) {
                if(inFromServer.hasNextLine()){
                    msgFromServer = inFromServer.nextLine();
                    switch (msgFromServer) {
                        case "dictionaryNotLegal" -> wordNotFoundInDictionary();
                        case "boardNotLegal" -> boardPlacementIllegal();
                        case "loadBoard" -> loadBoard();
                        case "bindButtons" -> bindButtons();
                        case "turnEnded" -> turnEnded();
                        case "updateView" -> updateView();
                        case "challengeSucceeded" -> challengeAccepted();
                        case "challengeFailed" -> challengeFailed();
                        case "serverIsClosing" -> closeEverything();
                        case "playerDisconnected" -> playerDisconnected();
                        default -> System.out.println(msgFromServer);
                    }
                }
            }
            closeEverything();
        }).start();
    }

    /**
     * The playerDisconnected function is called when a player disconnects from the game.
     * It sets the messageFromHost LiveData to &quot;playerDisconnected&quot; so that it can be observed by
     * GameActivity and used to display a Toast informing the user of this event.
     */
    private void playerDisconnected() {
        messageFromHost.setValue("playerDisconnected");
    }

    /**
     * The challengeFailed function is called when the user fails to solve a challenge.
     * It sets the messageFromHost LiveData object to &quot;challengeFailed&quot; so that it can be observed by the MainActivity.
     */
    private void challengeFailed() {
        messageFromHost.setValue("challengeFailed");
    }

    /**
     * The challengeAccepted function is called when the host accepts a challenge from a client.
     * It sets the messageFromHost to &quot;challengeAccepted&quot; so that it can be observed by the client.
     */
    private void challengeAccepted() {
        messageFromHost.setValue("challengeAccepted");
    }

    /**
     * The updateView function is called by the host whenever a change in the game state
     * requires that this player's GUI be updated.  The function updates all of the
     * components on this player's GUI to reflect changes in the game state.  It also
     * calls repaint() to force an immediate redrawing of all components on this player's GUI.
     */
    private void updateView() {
        messageFromHost.setValue("updateView");
    }

    /**
     * The playTurn function is used to send the action that the player wants to take
     * @param action action Send the action to the server
     */
    public void playTurn(String action){
        outToServer.println(action);
    }

    /**
     * The getMessageFromHost function is a getter function that returns the messageFromHost property.
     * @return A string property
     */
    public StringProperty getMessageFromHost() {
        messageFromHost = new SimpleStringProperty();
        return messageFromHost;
    }


    /**
     * The loadBoard function is used to load the board from a file.
     * It sets the messageFromHost value to &quot;loadBoard&quot; so that it can be read by the host and then executed.
     */
    public void loadBoard(){
        messageFromHost.setValue("loadBoard");
    }

    /**
     * The bindButtons function is called by the host to bind the buttons on the client's screen.
     * The function takes no parameters and returns nothing.
     */
    public void bindButtons(){
        messageFromHost.setValue("bindButtons");
    }

    /**
     * The wordNotFoundInDictionary function is called when the user enters a word that is not in the dictionary.
     * It sets messageFromHost to &quot;dictionaryNotLegal&quot; so that it can be displayed on screen.
     */
    private void wordNotFoundInDictionary() {
        messageFromHost.setValue("dictionaryNotLegal");
    }

    /**
     * The boardPlacementIllegal function is called when the host has determined that the board placement
     * of a player is illegal. This function sets the messageFromHost variable to &quot;boardNotLegal&quot; so that
     * it can be observed by other classes and used to display an appropriate error message.
     */
    private void boardPlacementIllegal() {
        messageFromHost.setValue("boardNotLegal");
    }

    /**
     * The turnEnded function is called when the host's turn ends.
     * It sets the messageFromHost to &quot;turnEnded&quot; so that it can be observed by the client.
     */
    private void turnEnded(){
        messageFromHost.setValue("turnEnded");
    }

    /**
     * The closeEverything function is called when the server is closing.
     * It sends a message to all clients that the server is closing, and then closes the server socket.
     */
    public void closeEverything() {
        try {
            messageFromHost.setValue("serverIsClosing");
            this.server.close();
        }
        catch (IOException ignored) {}
    }
}
