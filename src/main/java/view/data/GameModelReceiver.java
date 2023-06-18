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
    public GameModelReceiver(String ip, int port){
        try {
            server = new MySocket(new Socket(ip, port));
            inFromServer = new BufferedInputStream(server.getPlayerSocket().getInputStream());
            updatedModel = null;
            System.out.println("server status in GameModelReceiver -> " + server.getPlayerSocket().isClosed());
            listenForModelUpdates();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUpdatedModel(GameManager newModel){
        this.updatedModel = newModel;
        setChanged();
        notifyObservers();
        System.out.println("notifyFromGameModelReceiver To ViewSharedData");
    }

    public MySocket getServer(){
        return server;
    }

    public GameManager getUpdatedModel() {
        return updatedModel;
    }

    private void listenForModelUpdates(){
        new Thread(() -> {
            while (!server.getPlayerSocket().isClosed()) {
                try {
                    GameManager newModel = (GameManager) new ObjectInputStream(inFromServer).readObject();
                    System.out.println("Received model client size -> " + newModel.getGameData().getAllPlayers().size());
                    setUpdatedModel(newModel);
                } catch (IOException | ClassNotFoundException ignored) {}
            }
        }).start();
    }
}
