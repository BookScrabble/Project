package model.logic.server;

import java.io.*;
import java.util.Timer;

public class MyTimer implements Serializable {

    private transient Timer timer;

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
    public Timer getTimer() {
        return this.timer;
    }
}
