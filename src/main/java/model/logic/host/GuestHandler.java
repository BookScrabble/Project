package model.logic.host;

import model.logic.client.ClientHandler;

import java.io.*;

public class GuestHandler implements ClientHandler,Serializable {

    boolean stillPlaying;

    /**
     * The handleClient function is responsible for handling the client's requests.
     * It receives a request from the client, and then it sends back an appropriate response to the client.
     * The function also handles all the game logic that occurs during a player's turn, such as checking if their move is legal or not.
     * @param in in Read the client's request
     * @param out out Send messages to the client
     */
    @Override
    public void handleClient(InputStream in, OutputStream out) {
        boolean stillPlaying = true;
        GameManager gm = GameManager.get();
        gm.host.broadcastUpdate("bindButtons");
        int currentTurn = gm.getCurrentPlayerID();
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(in));
        PrintWriter outToClient = new PrintWriter(new OutputStreamWriter(out), true);
        while (currentTurn == gm.getCurrentPlayerID() && stillPlaying) {
            try {
                if(inFromClient.ready()){
                    String line = inFromClient.readLine();
                    System.out.println("Received message from client -> " + line);
                    String[] clientRequest = line.split(",");
                    switch (clientRequest[0]) {
                        case "submit" -> {
                            String result = gm.submit(clientRequest[1] + "," + clientRequest[2] + "," + clientRequest[3] + "," + clientRequest[4]);
                            switch (result) {
                                case "-2" -> outToClient.println("You didn't place any tiles on board");
                                case "-1" -> outToClient.println("boardNotLegal");
                                case "0" -> outToClient.println("dictionaryNotLegal");
                                case "Player Is Not Found!" -> {
                                    stillPlaying = false;
                                    gm.removePlayer(gm.getCurrentPlayerID());
                                }
                                default -> {
                                    outToClient.println("turnEnded");
                                    stillPlaying = false;
                                }
                            }
                        }

                        case "challenge" -> {
                            String result = gm.challenge(clientRequest[1]);
                            switch (result) {
                                case "true" -> {
                                    outToClient.println("challengeSucceeded");
                                    gm.submit(clientRequest[1] + "," + clientRequest[2] + "," + clientRequest[3] + "," + clientRequest[4]);
                                    stillPlaying = false;
                                }
                                case "false" -> {
                                    outToClient.println("challengeFailed");
                                    gm.getGameData().getPlayer(currentTurn).setScore(gm.getGameData().getPlayer(currentTurn).getScore() - 8);
                                    stillPlaying = false;
                                }
                            }
                        }

                        case "swapTiles" -> {
                            gm.swapTiles();
                            stillPlaying = false;
                        }

                        case "skipTurn" -> {
                            gm.skipTurn();
                            stillPlaying = false;
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }



    /**
     * The close function is used to stop the thread from running.
     * It sets stillPlaying to false, which causes the while loop in run()
     * to terminate. This allows for a clean exit of the program without
     * any errors or exceptions being thrown.
     */
    @Override
    public void close() {
        stillPlaying = false;
    }

}
