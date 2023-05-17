package model.Logic;

import model.Data.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HostServer extends MyServer {
    private String hostname;
    private Socket calculationServer;
    private PrintWriter printWriter;
    private Scanner scanner;
    private Map<Socket, Player> clients;
    private boolean gameIsRunning = false;

    /**
     * Constructor for MyServer class. Initializes the server with specified port number and client handler.
     * @param port          the port number to use for the server
     * @param clientHandler the client handler to use for handling client connections
     */
    public HostServer(String username ,int port, ClientHandler clientHandler) {
        super(port, clientHandler);
        this.clients = new HashMap<>();
        this.hostname = username;
        connect();
    }

    @Override
    protected void runServer(){
        try {
            ServerSocket server = new ServerSocket(this.port);
            connectClients(server);
            launchGame();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void connectClients(ServerSocket server){
        while(!gameIsRunning && clients.size() < 4){
            Socket aClient = null;
            try {
                aClient = server.accept(); // blocking call
                addClient(aClient, "default");
                System.out.println("A new guest has connected!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void launchGame(){
        if(gameIsRunning){
            Socket currentClient = (Socket) clients.keySet().toArray()[0];
            try {
                PrintWriter pw = new PrintWriter(currentClient.getOutputStream(), true);
                Scanner scanner = new Scanner(currentClient.getInputStream());
                pw.println("1");
                if(scanner.hasNextLine()){
                    String msg = scanner.nextLine();
                    pw.println("done");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Connects to the main server and creates input and output streams.
     * @throws IOException if an I/O error occurs when creating the socket or the streams
     * TODO - Connect method receives the ip and port, for now locally given just for testing.
     */
    public void connect() {
        try {
            calculationServer = new Socket("localhost", 25565);
            printWriter=new PrintWriter(calculationServer.getOutputStream());
            scanner=new Scanner(calculationServer.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * listen for incoming messages from the server
     * Not ready need to fix, Currently written like chatApp...
     * @param
     */
    public void listen() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                while(calculationServer.isConnected()) {
                    msgFromGroupChat=scanner.nextLine();
                    System.out.println(msgFromGroupChat);
                }
            }
        }).start();
    }

    public void addClient(Socket client, String name){
        this.clients.put(client, new Player(name));
        sendMSG(client);
    }

    /**
     * Sends a message to the server.
     * @param query
     */
    public void sendQuery(String ...query) {
    	for(String q : query) {
    		printWriter.println(q);
    	}
    }

    /**
     * Sends a message from the server to the client.
     * @param
     */
    public void sendMSG(Socket client){
        try {
            PrintWriter printWriter=new PrintWriter(client.getOutputStream());
            printWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean getGameIsRunning() {
    	return this.gameIsRunning;
    }

    public void startGame(){
        this.gameIsRunning = true;
    }

    public void stopGame() {
    	this.gameIsRunning = false;
    }

    public Map<Socket, Player> getClients(){
        return this.clients;
    }
}
