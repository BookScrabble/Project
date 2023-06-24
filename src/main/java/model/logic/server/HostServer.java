package model.logic.server;

import model.logic.client.Client;
import model.logic.client.ClientHandler;
import model.logic.host.GameManager;
import model.logic.host.MySocket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.*;


public class HostServer extends MyServer implements Serializable {

    private final Map<Integer, MySocket> clients;
    private final Map<Integer, MySocket> clientsModelReceiver;
    MyServerSocket server;
    private boolean gameIsRunning;
    private MyTimer turnTimer;

    public HostServer(int port, ClientHandler clientHandler) {
        super(port, clientHandler);
        this.clients = new HashMap<>();
        this.clientsModelReceiver = new HashMap<>();
        this.server = null;
    }

    public Map<Integer, MySocket> getClientsModelReceiver() {
        return clientsModelReceiver;
    }

    public class ManageTurnTask extends TimerTask{
        @Override
        public void run() {
            new Thread(() -> {
                GameManager.get().getTurnManager().nextTurn();
                sendUpdatedModel();
                System.out.println("Current player turn -> " + GameManager.get().getCurrentPlayerID());
                Socket currentPlayer = clients.get(GameManager.get().getCurrentPlayerID()).getPlayerSocket();
                try {
                    clientHandler.handleClient(currentPlayer.getInputStream(), currentPlayer.getOutputStream());
//                    this.cancel();
//                    turnTimer.getTimer().scheduleAtFixedRate(new ManageTurnTask(), 5000, 15000);
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
                MySocket client = new MySocket(server.getServerSocket().accept());
                Scanner inFromClient = new Scanner(client.getPlayerSocket().getInputStream());
                String playerName = inFromClient.next();
                if(playerName.equals("start")) {
                    GameManager.get().startGame();
                    broadcastUpdate("loadBoard");
                    return;
                }
                if(clients.size() < 4){
                    PrintWriter outToClient = new PrintWriter(client.getPlayerSocket().getOutputStream(),true);
                    outToClient.println(clients.size()+1);
                    clients.put(clients.size()+1, client);
                    GameManager.get().addPlayer(playerName);
                    System.out.println("Player " + playerName + " Connected Successfully!");
                    MySocket clientModelReceiver = new MySocket(server.getServerSocket().accept());
                    clientsModelReceiver.put(clients.size(), clientModelReceiver);
                    sendUpdatedModel();
                }
            }catch (SocketTimeoutException ignored) {}
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendUpdatedModel(){
        new Thread(() -> {
            for (MySocket gameModelReceiver : clientsModelReceiver.values()){
                try {
                    ObjectOutputStream outToModelReceiver = new ObjectOutputStream(gameModelReceiver.getPlayerSocket().getOutputStream());
                    outToModelReceiver.writeObject(GameManager.get());
                } catch (IOException ignored) {
                    System.out.println("GameModelReceiver not found -> IOException");
                    try {
                        throw(ignored);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
        sendUpdateViewRequest();
    }

    public void sendUpdateViewRequest(){
        //TODO - Update all client sockets that change was made and their view needs a refresh.
    }

    public void playGame(){
        if(!gameIsRunning){
            startGame();
        }
        if(gameIsRunning){
            turnTimer = new MyTimer(new Timer());
            turnTimer.getTimer().scheduleAtFixedRate(new ManageTurnTask(), 5000, 15000);
        }
    }

    //TODO - Implement method/Change to observer updates(GameModelReceiver MITM)
    public void broadcastUpdate(String messageForPlayers) {
        for(MySocket aClient : clients.values()) {
            try {
                PrintWriter outToClient = new PrintWriter(aClient.getPlayerSocket().getOutputStream(), true);
                outToClient.println(messageForPlayers);
            } catch (IOException ignored) {}
        }
    }

    public void removePlayer(int playerId){
        MySocket removedPlayer = this.clients.remove(playerId);
        if(removedPlayer != null) {
            try {
                removedPlayer.getPlayerSocket().close();
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
        sendUpdatedModel();
        this.gameIsRunning = true;
    }

    public void stopGame() {
        this.gameIsRunning = false;
        if(turnTimer != null) turnTimer.getTimer().cancel();
        GameManager.get().skipTurn();
        this.close();
    }
}