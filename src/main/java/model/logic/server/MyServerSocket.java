package model.logic.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.SocketException;

public class MyServerSocket implements Serializable {
    private transient ServerSocket serverSocket;

    public MyServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        try {
            this.serverSocket.setSoTimeout(3000);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.serverSocket = new ServerSocket();
    }
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

}
