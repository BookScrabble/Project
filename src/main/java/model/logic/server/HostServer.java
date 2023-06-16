package model.logic.server;

import model.logic.client.Client;
import model.logic.client.ClientHandler;
import model.logic.host.GameManager;
import model.logic.host.GuestHandler;
import model.logic.host.MySocket;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.*;


public class HostServer extends MyServer implements Serializable {

    private final Map<Integer, MySocket> clients;
    MyServerSocket server;
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
                Socket currentPlayer = clients.get(GameManager.get().getCurrentPlayerID()).getPlayerSocket();
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
            this.server = new MyServerSocket(new ServerSocket(this.port));
            connectClients();
            playGame();
        } catch (SocketException ignored) {}
    }

    public void connectClients() {
        while (!gameIsRunning) {
            try {
                Socket aClient = server.getServerSocket().accept();
                Scanner inPingCheck = new Scanner(aClient.getInputStream());
                String ping = inPingCheck.next();
                if(ping.equals("ping")){
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(aClient.getOutputStream());
                    objectOutputStream.writeObject(GameManager.get());
                    aClient.close();
                    MySocket realClient = new MySocket(server.getServerSocket().accept());
                    Scanner inFromClient = new Scanner(realClient.getPlayerSocket().getInputStream());
                    String playerName = inFromClient.next();
                    clients.put(clients.size() + 1, realClient);
                    GameManager.get().addPlayer(playerName);
                    System.out.println("Player " + playerName + " Connected Successfully!");
                }
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
        for(MySocket aClient : clients.values()) {
            try {
                PrintWriter outToClient = new PrintWriter(aClient.getPlayerSocket().getOutputStream(), true);
                outToClient.println(messageForPlayers);
            } catch (IOException ignored) {}
        }
    }

    @Override
    public void close() {
        Client.serverIsRunning = false;
        for(MySocket aClient : clients.values()) {
            try {
                aClient.getPlayerSocket().close();
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