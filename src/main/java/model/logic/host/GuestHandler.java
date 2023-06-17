package model.logic.host;

import model.logic.client.ClientHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GuestHandler implements ClientHandler,Serializable {

    @Override
    public void handleClient(InputStream in, OutputStream out) {
        boolean stillPlaying = true;
        GameManager gm = GameManager.get();
        int currentTurn = gm.getCurrentPlayerID();
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(in));
        PrintWriter outToClient = new PrintWriter(new OutputStreamWriter(out), true);
        outToClient.println("playTurn");
        while (currentTurn == gm.getCurrentPlayerID() && stillPlaying) {
            try {
                if(inFromClient.ready()){
                    String line = inFromClient.readLine();
                    String[] clientRequest = line.split(",");
                    switch (clientRequest[0]) {
                        case "submit" -> {
                            //"submit,word,row,col,vertical"
                            //"submit,HELLO,3,5,true"
                            String result = gm.submit(clientRequest[1] + "," + clientRequest[2] + "," + clientRequest[3] + "," + clientRequest[4]);
                            switch (result) {
                                case "-2" -> outToClient.println("You didn't place any tiles on board");
                                case "-1" -> {
                                    outToClient.println("Word cannot be placed in board");
                                    stillPlaying = false;
                                }
                                case "0" -> outToClient.println("Word not found in dictionary, Challenge or try again");
                                case "Player Is Not Found!" -> {
                                    stillPlaying = false;
                                    gm.removePlayer(gm.getCurrentPlayerID());
                                }
                                default -> {
                                    outToClient.println("Word accepted, turn done");
                                    stillPlaying = false;
                                }
                            }
                        }

                        case "challenge" -> {
                            //"challenge,word,row,col,vertical"
                            //"challenge,HELLO,3,5,true"
                            String result = gm.challenge(clientRequest[1]);
                            switch (result) {
                                case "true" -> {
                                    outToClient.println("Challenge accepted.");
                                    gm.submit(clientRequest[1] + "," + clientRequest[2] + "," + clientRequest[3] + "," + clientRequest[4]);
                                    stillPlaying = false;
                                }
                                case "false" -> outToClient.println("Challenge failed.");
                            }
                            //Update all if succeeded(View)
                            //Update client for score gained(View)
                            //Update all if failed
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
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //gm.setCurrentPlayerID((gm.currentPlayerID%4)+1);
        try {
            inFromClient.close();
            outToClient.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void close() {

    }

}
