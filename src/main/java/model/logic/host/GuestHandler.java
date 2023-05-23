package model.logic.host;

import model.logic.client.ClientHandler;

import java.io.*;

public class GuestHandler implements ClientHandler {
    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        GameManager gm = GameManager.get();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inFromClient));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outToClient));
            String line = br.readLine(); /*TODO - If blocking call is needed change to scanner*/
            String[] clientRequest = line.split(",");
            switch (clientRequest[0]) {
                case "submit" ->
                    //"submit,word,row,col,vertical"
                    //"submit,HELLO,3,5,true"
                        gm.submit(clientRequest[1] + "," + clientRequest[2] + "," + clientRequest[3] + "," + clientRequest[4]);
                case "swap" ->
                    //"swap,
                        gm.swapTiles();
                case "resign" -> gm.resign();
                case "challenge" ->
                    //"challenge,word"
                    //"challenge,HELLO"
                        gm.challenge("C," + gm.getGameData().getDictionaries() + "," + clientRequest[1]);
                case "skip" -> gm.skipTurn();
                case "sort" -> gm.sort();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Client connected successfully to Host!");
    }

    @Override
    public void close() {

    }
}
