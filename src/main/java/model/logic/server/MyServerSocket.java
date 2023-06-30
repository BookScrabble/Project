package model.logic.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.SocketException;
/**
 * The MyServerSocket class is a serializable wrapper for a ServerSocket object.
 * It allows the ServerSocket object to be serialized and deserialized.
 */
public class MyServerSocket implements Serializable {
    private transient ServerSocket serverSocket;

    /**
     * Constructs a new MyServerSocket object with the specified ServerSocket.
     * @param serverSocket The ServerSocket object to wrap.
     */
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

    /**
     * Retrieves the ServerSocket object wrapped by this MyServerSocket.
     * @return The ServerSocket object.
     */
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

}
