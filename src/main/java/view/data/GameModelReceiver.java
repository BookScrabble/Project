package view.data;

import model.logic.host.GameManager;
import model.logic.host.MySocket;

import java.io.*;
import java.net.Socket;
import java.util.Observable;

public class GameModelReceiver extends Observable implements Serializable {
    MySocket server;
    private final BufferedInputStream inFromServer;
    GameManager updatedModel;
    /**
     * The GameModelReceiver function is a constructor that creates a new socket connection to the server.
     * It also starts listening for updates from the server in its own thread.
     * @param ip ip Connect to the server
     * @param port port Connect to the server
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
     * The setUpdatedModel function is used to update the model of the game.
     * @param newModel newModel Update the model
     */
    public void setUpdatedModel(GameManager newModel){
        this.updatedModel = newModel;
        setChanged();
        notifyObservers();
    }

    /**
     * The getServer function returns the server socket.
     * @return The server variable
     */
    public MySocket getServer(){
        return server;
    }

    /**
     * The getUpdatedModel function returns the updated model.
     * @return The updated model
     */
    public GameManager getUpdatedModel() {
        return updatedModel;
    }

    /**
     * The listenForModelUpdates function is a function that runs on its own thread.
     * It listens for updates from the server and sets the updated model to be used by the client.
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
