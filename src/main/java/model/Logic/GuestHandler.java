package model.Logic;

import java.io.*;

public class GuestHandler implements ClientHandler{
    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        /*
        Expectation of handleClient for guests:
        Host will call this method for every guest when he wants to give this guest his turn.
        1)Send client a message that runs a method in client class which opens
            the communication between the client and the host(Not necessary a thread)
        2)open a thread in a helper method inside GuestHandler which listens for an update from the host.
            Message will indicate the guest is out of time or the player has finished his turn successfully,
            Meaning he placed a word.
        3)Send a message to the client which stops his turn thread, Either putting it in wait/sleep
            or completely closes it.
         */
        BufferedReader br = new BufferedReader(new InputStreamReader(inFromClient));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outToClient));
        System.out.println("Client connected successfully to Host!");
    }

    @Override
    public void close() {

    }
}
