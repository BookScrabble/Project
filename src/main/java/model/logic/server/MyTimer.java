package model.logic.server;

import java.io.*;
import java.util.Timer;
/**
 * The MyTimer class is a serializable wrapper for a Timer object.
 * It allows the Timer object to be serialized and deserialized.
 */
public class MyTimer implements Serializable {

    private transient Timer timer;

    /**
     * Constructs a new MyTimer object with the specified Timer.
     * @param timer The Timer object to wrap.
     */
    public MyTimer(Timer timer) {
        this.timer = timer;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.timer = new Timer();
    }

    /**
     * Retrieves the Timer object wrapped by this MyTimer.
     * @return The Timer object.
     */
    public Timer getTimer() {
        return this.timer;
    }
}
