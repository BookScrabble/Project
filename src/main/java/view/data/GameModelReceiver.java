package view.data;

import model.logic.host.GameManager;
import model.logic.host.MySocket;

import java.io.*;
import java.net.Socket;
import java.util.Observable;
/**
 * The GameModelReceiver class is responsible for receiving and updating the game model from the server.
 * It listens for model updates from the server and notifies observers when a new model is received.
 */
public class GameModelReceiver extends Observable implements Serializable {
    MySocket server;
    private final BufferedInputStream inFromServer;
    GameManager updatedModel;

    /**
     * Constructs a new GameModelReceiver object with the specified IP address and port number.
     * It establishes a connection to the server and initializes the input stream.
     * @param ip The IP address of the server.
     * @param port The port number of the server.
     */
    public GameModelReceiver(String ip, int port){
        try {
            server = new MySocket(new Socket(ip, port));
            inFromServer = new BufferedInputStream(server.getPlayerSocket().getInputStream());
            updatedModel = null;
            listenForModelUpdates();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the updated game model and notifies observers of the change.
     * @param newModel The updated game model.
     */
    public void setUpdatedModel(GameManager newModel){
        this.updatedModel = newModel;
        setChanged();
        notifyObservers();
    }

    /**
     * Retrieves the server socket associated with the GameModelReceiver.
     * @return The MySocket object representing the server socket.
     */
    public MySocket getServer(){
        return server;
    }

    /**
     * Retrieves the updated game model.
     * @return The GameManager object representing the updated game model.
     */
    public GameManager getUpdatedModel() {
        return updatedModel;
    }

    /**
     * Listens for model updates from the server in a separate thread.
     * When a new model is received, it updates the game model and notifies observers.
     */
    private void listenForModelUpdates(){
        new Thread(() -> {
            while (!server.getPlayerSocket().isClosed()) {
                try {
                    GameManager newModel = (GameManager) new ObjectInputStream(inFromServer).readObject();
                    setUpdatedModel(newModel);
                } catch (IOException | ClassNotFoundException ignored) {}
            }
        }).start();
    }
}
