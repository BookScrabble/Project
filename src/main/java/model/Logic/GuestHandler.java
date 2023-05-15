package model.Logic;

import java.io.*;

public class GuestHandler implements ClientHandler{
    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inFromClient));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outToClient));
            String line = br.readLine();
            System.out.println("Client connected successfully to Host!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {

    }
}
