package model.logic.server;

import java.io.*;
import java.util.TimerTask;

public class MyTimerTask implements Serializable {
    private transient TimerTask timerTask;

    /**
     * The MyTimerTask function is a constructor for the MyTimerTask class.
     * It takes in a TimerTask object and assigns it to the timerTask variable.
     * @param timerTask timerTask Create a new timerTask object
     */
    public MyTimerTask(TimerTask timerTask) {
        this.timerTask = timerTask;
    }

    /**
     * The writeObject function is used to write the object's state to a stream.
     * This function is called by ObjectOutputStream when an object needs to be written out.
     * The default implementation of this method in ObjectOutputStream calls the methods writeObjectOverride and writeFields, which you can override in your class as needed.
     * @param out out Write the object to a file
     */
    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    /**
     * The readObject function is used to read the object from a stream.
     * It is called when an object is deserialized.
     * The default implementation of this function in ObjectInputStream does nothing, but it can be overridden by subclasses
     * to customize the behavior of class-specific data written by writeObject and read by multiple calls to readObject.
     * @param in in Read the object from a file
     */
    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.timerTask = new TimerTask() {
            /**
             * The run function is the main function of the thread.
             * It will run until it is interrupted or stopped.
             */
            @Override
            public void run() {

            }
        };
    }
    /**
     * The getTimerTask function returns the timerTask variable.
     * @return The timerTask object
     */
    public TimerTask getTimerTask() {
        return this.timerTask;
    }
}
