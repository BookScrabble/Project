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

/**
 * The HostServer class represents a server that hosts a game session.
 * It extends the MyServer class and provides additional functionality for managing the game and interacting with clients.
 */
public class HostServer extends MyServer implements Serializable {

    private final Map<Integer, MySocket> clients;
    private final Map<Integer, MySocket> clientsModelReceiver;
    MyServerSocket server;
    private boolean gameIsRunning;
    private MyTimer turnTimer;
    private MyTimerTask timerTask;

    /**
     * Constructs a new HostServer with the specified port and client handler.
     * @param port The port number to listen on.
     * @param clientHandler The client handler to use for handling client connections.
     */
    public HostServer(int port, ClientHandler clientHandler) {
        super(port, clientHandler);
        this.clients = new HashMap<>();
        this.clientsModelReceiver = new HashMap<>();
        this.server = null;
    }

    /**
     * A TimerTask implementation for managing the turns in the game.
     */
    public class ManageTurnTask extends TimerTask{
        @Override
        public void run() {
            new Thread(() -> {
                sendUpdatedModel();
                System.out.println("Current player turn -> " + GameManager.get().getCurrentPlayerID());
                Socket currentPlayer = clients.get(GameManager.get().getCurrentPlayerID()).getPlayerSocket();
                try {
                    clientHandler.handleClient(currentPlayer.getInputStream(), currentPlayer.getOutputStream());
                    this.cancel();
                    resetTimerTask();
                } catch (IOException ignored) {}
            }).start();
        }
    }

    /**
     * Starts the server and handles client connections.
     * @throws Exception if an error occurs while running the server.
     */
    @Override
    protected void runServer() throws Exception {
        try {
            this.server = new MyServerSocket(new ServerSocket(this.port));
            connectClients();
            playGame();
        } catch (SocketException ignored) {}
    }

    /**
     * Connects clients to the server until the game is running.
     */
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

    /**
     * Sends the updated game model to all connected clients.
     */
    public void sendUpdatedModel(){
        new Thread(() -> {
            for (MySocket gameModelReceiver : clientsModelReceiver.values()){
                try {
                    ObjectOutputStream outToModelReceiver = new ObjectOutputStream(gameModelReceiver.getPlayerSocket().getOutputStream());
                    outToModelReceiver.writeObject(GameManager.get());
                } catch (IOException ignored) {
                    System.out.println("GameModelReceiver not found -> IOException");
                }
            }
        }).start();
    }

    /**
     * Resets the timer task.
     */
    public void resetTimerTask(){
        timerTask = null;
    }

    /**
     * Plays the game by managing turns and timers.
     */
    public void playGame(){
        if(!gameIsRunning){
            startGame();
        }
        while(gameIsRunning){
            if(timerTask == null || turnTimer == null){
                timerTask = new MyTimerTask(new ManageTurnTask());
                turnTimer = new MyTimer(new Timer());
                GameManager.get().getTurnManager().nextTurn();
                turnTimer.getTimer().schedule(timerTask.getTimerTask(), 1000, 60000);
            }
            else{
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Broadcasts an update message to all connected clients.
     * @param messageForPlayers The message to broadcast to the players.
     */
    //TODO - Implement method/Change to observer updates(GameModelReceiver MITM)
    public void broadcastUpdate(String messageForPlayers) {
        for(MySocket aClient : clients.values()) {
            try {
                PrintWriter outToClient = new PrintWriter(aClient.getPlayerSocket().getOutputStream(), true);
                outToClient.println(messageForPlayers);
            } catch (IOException ignored) {}
        }
    }

    /**
     * Removes a player from the server.
     * @param playerId The ID of the player to remove.
     */
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

        broadcastUpdate("serverIsClosing");

        for(MySocket aClient: clientsModelReceiver.values()){
            try{
                aClient.getPlayerSocket().close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        clients.clear();
        clientsModelReceiver.clear();
        super.close();
    }

    /**
     * Checks if the game is currently running.
     * @return true if the game is running, false otherwise.
     */
    public boolean isGameRunning() {
        return gameIsRunning;
    }

    /**
     * Starts the game and sends the updated model to the clients.
     */
    public void startGame() {
        sendUpdatedModel();
        this.gameIsRunning = true;
    }

    /**
     * Stops the game and cancels the turn timer.
     */
    public void stopGame() {
        this.gameIsRunning = false;
        if(turnTimer != null) turnTimer.getTimer().cancel();
        resetTimerTask();
        this.close();
    }
}