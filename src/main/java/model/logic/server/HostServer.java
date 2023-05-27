package model.logic.server;

import model.logic.client.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;


public class HostServer extends MyServer {

    private final Map<Integer, Socket> clients;
    ServerSocket server;
    private boolean gameIsRunning;

    public HostServer(int port, ClientHandler clientHandler) {
        super(port, clientHandler);
        this.clients = new HashMap<>();
        this.server = null;
    }

    @Override
    protected void runServer() throws Exception {
        try {
            this.server = new ServerSocket(this.port);
            server.setSoTimeout(1000);
            connectClients();
            playGame();
        } catch (SocketException ignored) {}
    }

    public void connectClients() {
        while (!gameIsRunning && clients.size() < 4) {
            try {
                Socket aClient = server.accept();
                clients.put(clients.size() + 1, aClient);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * TODO - Finish implementation of time/turn management.
     */
    public void playGame(){
        if(!gameIsRunning){
            startGame();
        }
        for (Socket aClient: clients.values()) {
            new Thread(() -> {
                try {
                    clientHandler.handleClient(aClient.getInputStream(), aClient.getOutputStream());
                    broadcastUpdate();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
//        while (!stop){
//            for (Socket aClient: clients.values()) {
//                try{
//                    Thread.sleep(45000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                long startTime = System.currentTimeMillis();
//            }
//        }
        //45sec -> Set False
        //Socket -> 45sec (playTurn)
        //Set True
        //new Thread -> clientHandler.handleClient(Socket.getIn(),Socket.getOut());
        //Set False
        //"Submit"
        //"0"
        //"Challenge"
        //Loop
        //Handle clients..
    }

    //TODO - Implement method
    private void broadcastUpdate() {

    }

    @Override
    public void close() {
        this.stop = true;
        this.clientHandler.close();
        for(Socket aClient : clients.values()) {
            try {
                aClient.close();
            } catch (IOException ignored) {}
        }
    }

    public boolean isGameRunning() {
        return gameIsRunning;
    }

    public void startGame() {
        this.gameIsRunning = true;
    }

    public void stopGame() {
        this.gameIsRunning = false;
    }
}