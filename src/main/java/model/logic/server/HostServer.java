package model.logic.server;

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
    private volatile boolean gameIsRunning;
    private MyTimer turnTimer;
    private MyTimerTask timerTask;

    /**
     * The HostServer function is a constructor for the HostServer class.
     * It takes in two parameters: an integer representing the port number, and a ClientHandler object.
     * The function initializes two HashMaps, one to store clients and their corresponding client handlers,
     * and another to store clients' model receivers.
     * @param port port Specify the port number that the server will be listening on
     * @param clientHandler clientHandler Create a new instance of the clientHandler class
     */
    public HostServer(int port, ClientHandler clientHandler) {
        super(port, clientHandler);
        this.clients = new HashMap<>();
        this.clientsModelReceiver = new HashMap<>();
        this.server = null;
    }

    /**
     * Manage each guest turn, update models and if player is found initiate clientHandler.
     */
    public class ManageTurnTask extends TimerTask{
        /**
         * The run function is called when the timer task is executed.
         * It creates a new thread that sends the updated model to all clients,
         * then it gets the current player's socket and calls handleClient on it.
         */
        @Override
        public void run() {
            new Thread(() -> {
                sendUpdatedModel();
                System.out.println("Current player turn -> " + GameManager.get().getCurrentPlayerID());
                Socket currentPlayer;
                try{
                   currentPlayer = clients.get(GameManager.get().getCurrentPlayerID()).getPlayerSocket();
                } catch(NullPointerException e){
                    this.cancel();
                    resetTimerTask();
                    return;
                }
                try {
                    clientHandler.handleClient(currentPlayer.getInputStream(), currentPlayer.getOutputStream());
                    this.cancel();
                    resetTimerTask();
                } catch (IOException ignored) {}
            }).start();
        }
    }

    /**
     * The runServer function is the main function of the server. It creates a new MyServerSocket object,
     * which is a subclass of ServerSocket that allows for multiple clients to connect at once. The runServer
     * function then calls two other functions: connectClients and playGame. The first connects all clients to
     * the server, while the second runs through each round of gameplay until one player wins or there's a tie.
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
     * The connectClients function is responsible for connecting clients to the server.
     * It does this by accepting connections from clients and adding them to a list of connected players.
     * The function also adds the player's name to a list of players in GameManager, which will be used later on when creating the game board.
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
     * The sendUpdatedModel function is used to send the updated model to all the clients.
     * It does this by creating a new thread that iterates through each client and sends them
     * an object output stream containing the GameManager's current gameModel. This function is called
     * whenever there are changes made to the gameModel, such as when a player moves or when a card is played.
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
     * The resetTimerTask function is used to reset the timerTask variable.
     * This function is called when a new game begins, and it resets the timerTask variable so that it can be used again.
     */
    public void resetTimerTask(){
        timerTask = null;
    }

    /**
     * The playGame function is the main function of the game. It starts by checking if a game is running, and if not, it starts one.
     * Then it enters a while loop that runs as long as there's an active game running. The while loop contains two try-catch blocks:
     * One for accepting new players to join the server (if they're allowed), and one for receiving messages from clients (such as when they disconnect).
     * If a player disconnects, their ID will be sent to this class through MySocket's getPlayerSocket().getInputStream(), which will then be read by Scanner scanner = new Scan
     */
    public void playGame(){
        if(!gameIsRunning){
            startGame();
        }
        while(gameIsRunning){
            try{
                MySocket hostAttempt = new MySocket(server.getServerSocket().accept());
                Scanner scanner = new Scanner(hostAttempt.getPlayerSocket().getInputStream());
                String hostMessage;
                if(scanner.hasNext()){
                    hostMessage = scanner.next();
                    if(hostMessage.equals("serverIsClosing")){
                        broadcastUpdate("serverIsClosing");
                        resetTimerTask();
                        GameManager.get().stopGame();
                    }
                    else{
                        String[] splitted = hostMessage.split(",");
                        if(splitted[0].equals("playerDisconnected")) {
                            GameManager.get().removePlayer(Integer.parseInt(splitted[1]));
                            sendUpdatedModel();
                            broadcastUpdate("playerDisconnected");
                        }
                    }
                }
                hostAttempt.getPlayerSocket().close();
            } catch (IOException ignored) {}
            if(timerTask == null || turnTimer == null){
                timerTask = new MyTimerTask(new ManageTurnTask());
                turnTimer = new MyTimer(new Timer());
                GameManager.get().getTurnManager().nextTurn();
                turnTimer.getTimer().schedule(timerTask.getTimerTask(), 1000, 60000);
            }
        }
    }

    /**
     * The broadcastUpdate function is used to send a message to all players in the game.
     * @param messageForPlayers messageForPlayers Send a message to all the clients
     */
    public void broadcastUpdate(String messageForPlayers) {
        for(MySocket aClient : clients.values()) {
            try {
                PrintWriter outToClient = new PrintWriter(aClient.getPlayerSocket().getOutputStream(), true);
                outToClient.println(messageForPlayers);
            } catch (IOException ignored) {}
        }
    }

    /**
     * The removePlayer function removes a player from the game.
     * @param playerId playerId Find the player in the clients map
     */
    public void removePlayer(int playerId){
        MySocket removedPlayer = this.clients.remove(playerId);
        if(removedPlayer != null) {
            try {
                removedPlayer.getPlayerSocket().close();
            } catch (IOException ignored) {}
        }
    }

    /**
     * The close function closes the server socket and all the client sockets.
     * It also clears both clients hashmaps.
     */
    @Override
    public void close() {
        broadcastUpdate("serverIsClosing");
        for(MySocket aClient: clientsModelReceiver.values()){
            try{
                aClient.getPlayerSocket().close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for(MySocket aClient: clients.values()){
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
     * The isGameRunning function returns a boolean value that indicates whether the game is running or not.
     * @return A boolean value
     */
    public boolean isGameRunning() {
        return gameIsRunning;
    }

    /**
     * The startGame function is called when the game is ready to start.
     * It sets the gameIsRunning variable to true, and then sends an updated model
     * to all the players in this Game's playerList.
     */
    public void startGame() {
        sendUpdatedModel();
        this.gameIsRunning = true;
    }

    /**
     * The stopGame function is called when the game ends. It sets the gameIsRunning boolean to false,
     * cancels any turnTimer that may be running, resets the timerTask and closes all of its sockets.
     */
    public void stopGame() {
        this.gameIsRunning = false;
        if(turnTimer != null) turnTimer.getTimer().cancel();
        resetTimerTask();
        this.close();
    }
}