package model.logic.server;

import java.io.*;
import java.util.Timer;

public class MyTimer implements Serializable {

    private transient Timer timer;

    /**    
     * The MyTimer function is a constructor that takes in a Timer object and sets the timer variable to it.
     * @param timer timer Pass in the timer object that is created in the main method
     */
    public MyTimer(Timer timer) {
        this.timer = timer;
    }

    /**
     * The writeObject function is used to write the object's state to a stream.
     * This function is called by ObjectOutputStream when an object needs to be written out.
     * The default implementation of this method in ObjectOutputStream calls the writeObjectOverride()
     * method of class ObjectOutputStream, which in turn calls the defaultWriteObject() method of this class.
     * @param out out Write the object to a file
     */
    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    /**    
     * The readObject function is used to read the object from a stream.
     * @param in in Read the object from a file
     */
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.timer = new Timer();
    }

    /**
     * The getTimer function returns the timer object.
     * @return The timer object
     */
    public Timer getTimer() {
        return this.timer;
    }
}
