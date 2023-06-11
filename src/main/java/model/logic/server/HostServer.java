package model.logic.server;

import model.logic.client.Client;
import model.logic.client.ClientHandler;
import model.logic.host.GameManager;
import model.logic.host.GuestHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.*;


public class HostServer extends MyServer {

    private final Map<Integer, Socket> clients;
    ServerSocket server;
    private boolean gameIsRunning;
    private Timer turnTimer;

    public HostServer(int port, ClientHandler clientHandler) {
        super(port, clientHandler);
        this.clients = new HashMap<>();
        this.server = null;
    }

    public class ManageTurnTask extends TimerTask{
        @Override
        public void run() {
            new Thread(() -> {
                GameManager.get().getTurnManager().nextTurn();
                System.out.println("Current player -> " + GameManager.get().getCurrentPlayerID());
                Socket currentPlayer = clients.get(GameManager.get().getCurrentPlayerID());
                try {
                    clientHandler.handleClient(currentPlayer.getInputStream(), currentPlayer.getOutputStream());
                    this.cancel();
                    turnTimer.scheduleAtFixedRate(new ManageTurnTask(), 1000, 5000);
                } catch (IOException ignored) {}
            }).start();
        }
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
        if(gameIsRunning){
            turnTimer = new Timer();
            turnTimer.scheduleAtFixedRate(new ManageTurnTask(), 1000, 5000); //TODO - Update delay and period here and in ManageTurnTask!
        }
    }

    //TODO - Implement method
    private void broadcastUpdate(String messageForPlayers) {
        for(Socket aClient : clients.values()) {
            try {
                PrintWriter outToClient = new PrintWriter(aClient.getOutputStream(), true);
                outToClient.println(messageForPlayers);
            } catch (IOException ignored) {}
        }
    }

    @Override
    public void close() {
        Client.serverIsRunning = false;
        for(Socket aClient : clients.values()) {
            try {
                aClient.close();
            } catch (IOException ignored) {}
        }
        clients.clear();
        super.close();
    }

    public boolean isGameRunning() {
        return gameIsRunning;
    }

    public void startGame() {
        this.gameIsRunning = true;
    }

    public void stopGame() {
        this.gameIsRunning = false;
        if(turnTimer != null) turnTimer.cancel();
        GameManager.get().skipTurn();
        this.close();
    }
}