package model.logic.server;

import model.logic.client.Client;
import model.logic.client.ClientHandler;
import model.logic.host.GameManager;
import model.logic.host.MySocket;
import view.data.GameModelReceiver;

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
    private Timer turnTimer;

    public HostServer(int port, ClientHandler clientHandler) {
        super(port, clientHandler);
        this.clients = new HashMap<>();
        this.clientsModelReceiver = new HashMap<>();
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
                    turnTimer.scheduleAtFixedRate(new ManageTurnTask(), 5000, 60000);
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
//        while (!gameIsRunning) {
//            try {
//                Socket aClient = server.getServerSocket().accept();
//                Scanner inPingCheck = new Scanner(aClient.getInputStream());
//                String ping = inPingCheck.next();
//                if(ping.equals("ping")){
//                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(aClient.getOutputStream());
//                    objectOutputStream.writeObject(GameManager.get());
//                    aClient.close();
//                    MySocket realClient = new MySocket(server.getServerSocket().accept());
//                    Scanner inFromClient = new Scanner(realClient.getPlayerSocket().getInputStream());
//                    String playerName = inFromClient.next();
//                    clients.put(clients.size() + 1, realClient);
//                    GameManager.get().addPlayer(playerName);
//                    System.out.println("Player " + playerName + " Connected Successfully!");
//                    sendUpdatedModel(GameManager.get());
//                }
//            }catch (SocketTimeoutException ignored) {}
//            catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        while (!gameIsRunning) {
            try {
                MySocket client = new MySocket(server.getServerSocket().accept());
                Scanner inFromClient = new Scanner(client.getPlayerSocket().getInputStream());
                String playerName = inFromClient.next();
                clients.put(clients.size()+1, client);
                GameManager.get().addPlayer(playerName);
                System.out.println("Player " + playerName + " Connected Successfully!");
                MySocket clientModelReceiver = new MySocket(server.getServerSocket().accept());
                clientsModelReceiver.put(clients.size(), clientModelReceiver);
                sendUpdatedModel(GameManager.get());
            }catch (SocketTimeoutException ignored) {}
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendUpdatedModel(GameManager model){
        int i = 1;
        for (MySocket gameModelReceiver : clientsModelReceiver.values()){
            try {
                System.out.println("Updated client number " + i);
                i++;
                ObjectOutputStream outToModelReceiver = new ObjectOutputStream(gameModelReceiver.getPlayerSocket().getOutputStream());
                outToModelReceiver.writeObject(model);
            } catch (IOException ignored) {
                System.out.println("Not found");
            }
        }
        sendUpdateViewRequest();
    }

    public void sendUpdateViewRequest(){
        //TODO - Update all client sockets that change was made and their view needs a refresh.
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
            turnTimer.scheduleAtFixedRate(new ManageTurnTask(), 5000, 60000);
        }
    }

    //TODO - Implement method/Change to observer updates(ClientModelObserver MITM)
    private void broadcastUpdate(String messageForPlayers) {
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
        this.gameIsRunning = true;
    }

    public void stopGame() {
        this.gameIsRunning = false;
        if(turnTimer != null) turnTimer.cancel();
        GameManager.get().skipTurn();
        this.close();
    }
}