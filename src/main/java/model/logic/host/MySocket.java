package model.logic.host;

import java.io.*;
import java.net.Socket;

public class MySocket implements Serializable {
    private transient Socket playerSocket;

    /**
     * The MySocket function is a constructor that takes in a Socket object and assigns it to the playerSocket variable.
     * @param playerSocket playerSocket Create a socket for the player
     */
    public MySocket(Socket playerSocket) {
        this.playerSocket = playerSocket;
    }

    /**
     * The writeObject function is used to write the object's state to a stream.
     * This function is called by ObjectOutputStream when an object needs to be written out.
     * The default implementation of this method in ObjectOutputStream calls the writeObjectOverride() method of class ObjectOutputStream, which in turn calls the defaultWriteObject() method of this class.
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
        this.playerSocket = new Socket();
    }

    /**
     * The getPlayerSocket function returns the playerSocket variable.
     * @return The player socket variable
     */
    public Socket getPlayerSocket() {
        return playerSocket;
    }
}
