package model.logic.host;

import java.io.*;
import java.net.Socket;

public class MySocket implements Serializable {
    private transient Socket playerSocket;

    public MySocket(Socket playerSocket) {
        this.playerSocket = playerSocket;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.playerSocket = new Socket();
    }

    public Socket getPlayerSocket() {
        return playerSocket;
    }
}
