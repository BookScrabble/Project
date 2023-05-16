package model.Logic;

import java.io.*;

public class GuestHandler implements ClientHandler{
    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inFromClient));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outToClient));
        System.out.println("Client connected successfully to Host!");
    }

    @Override
    public void close() {

    }
}
