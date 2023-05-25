package model.logic.host;

import model.logic.client.ClientHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuestHandler implements ClientHandler {

    private static final Map<GuestHandler, List<Closeable>> guestHandlerList = new HashMap<>();

    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient) {
        GameManager gm = GameManager.get();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inFromClient));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outToClient));
            String line = br.readLine(); /*TODO - If blocking call is needed change to scanner*/
            String[] clientRequest = line.split(",");
            switch (clientRequest[0]) {
                case "submit" -> {
                    //"submit,word,row,col,vertical"
                    //"submit,HELLO,3,5,true"
                    String result = gm.submit(clientRequest[1] + "," + clientRequest[2] + "," + clientRequest[3] + "," + clientRequest[4]);
                    switch(result){
                        case "-2" -> bw.write("You didn't place any tiles on board");
                        case "-1" -> bw.write("Word cannot be placed in board");
                        case "0" -> bw.write("Word not found in dictionary, Challenge or try again");
                    }
                }

                case "challenge" -> {
                    //"challenge,word,row,col,vertical"
                    //"challenge,HELLO,3,5,true"
                    String result = gm.challenge(clientRequest[1]);
                    switch(result){
                        case "true" -> bw.write("Challenge accepted.");
                        case "false" -> bw.write("Challenge failed.");
                    }
                    //Update all if succeeded(View)
                    //Update client for score gained(View)
                    //Update all if failed
                }

                case "connect" -> {
                    gm.addPlayer(clientRequest[1]);
                    if(!guestHandlerList.containsKey(this)){
                        guestHandlerList.put(this, new ArrayList<>());
                    }
                    guestHandlerList.get(this).add(inFromClient);
                    guestHandlerList.get(this).add(outToClient);
                    System.out.println("Client connected successfully");
                }

                case "test" -> {
                    System.out.println("Received input from client");
                }

                // THINGS TO IMPLEMENT IN THE FUTURE
                case "swap" ->
                    //"swap,
                        gm.swapTiles();
                case "resign" -> gm.resign();
                case "skip" -> gm.skipTurn();
                case "sort" -> {
                    gm.sort();
                    //View Update for specific client
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    @Override
    public void close() {
        guestHandlerList.forEach((guestHandler, closeables) -> {
            try {
                closeables.get(0).close();
                closeables.get(1).close();
            } catch (IOException ignored) {}
        });
    }

}
