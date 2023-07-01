package model.logic.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.SocketException;

public class MyServerSocket implements Serializable {
    private transient ServerSocket serverSocket;

    /**
     * The MyServerSocket function creates a new MyServerSocket object.
     * @param serverSocket serverSocket Create a new server socket
     */
    public MyServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        try {
            this.serverSocket.setSoTimeout(3000);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The writeObject function is used to write the object's state to a stream.
     * This function is called by ObjectOutputStream when an object needs to be written out.
     * The default implementation of this method in ObjectOutputStream calls the methods of DataOutput,
     * which writes primitive data types and Strings in a machine-independent format.
     * @param out out Write the object to a file
     */
    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    /**
     * The readObject function is used to read the object from a stream.
     * @param in in Read the object from the stream
     */
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.serverSocket = new ServerSocket();
    }
    /**
     * The getServerSocket function returns the serverSocket variable.
     * @return The serverSocket variable
     */
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

}
