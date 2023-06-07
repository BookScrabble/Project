package model.logic.server;

import model.logic.client.ClientHandler;
import model.logic.host.GameManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


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
        while (!gameIsRunning) {
            try {
                Socket aClient = server.accept();
                Scanner inFromClient = new Scanner(aClient.getInputStream());
                String playerName = inFromClient.next();
                clients.put(clients.size() + 1, aClient);
                GameManager.get().addPlayer(playerName);
                System.out.println("Player " + playerName + " Connected Successfully!");
            }catch (SocketTimeoutException ignored) {}
            catch (IOException e) {
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