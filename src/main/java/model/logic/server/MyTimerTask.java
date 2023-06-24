package model.logic.server;

import java.io.*;
import java.util.TimerTask;

public class MyTimerTask implements Serializable {

    private transient TimerTask timerTask;

    public MyTimerTask(TimerTask timerTask) {
        this.timerTask = timerTask;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.timerTask = new TimerTask() {
            @Override
            public void run() {

            }
        };
    }
    public TimerTask getTimerTask() {
        return this.timerTask;
    }
}
