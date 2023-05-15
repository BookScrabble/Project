package model.Logic;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class HostServer extends MyServer {
    private String hostname;
    private int port;
    private Socket mainServer;
    private PrintWriter printWriter;
    private Scanner scanner;
    private ArrayList<Socket> clients;

    /**
     * Constructor for MyServer class. Initializes the server with specified port number and client handler.
     * @param port          the port number to use for the server
     * @param clientHandler the client handler to use for handling client connections
     */
    public HostServer(String username ,int port, ClientHandler clientHandler) {
        super(port, clientHandler);
        this.clients = new ArrayList<>();
        this.hostname = username;
        connect();
    }

    /**
     * Connects to the main server and creates input and output streams.
     * @throws IOException if an I/O error occurs when creating the socket or the streams
     * TODO - Connect method receives the ip and port, for now locally given just for testing.
     */
    public void connect() {
        try {
            mainServer = new Socket("localhost", 25565);
            printWriter=new PrintWriter(mainServer.getOutputStream());
            scanner=new Scanner(mainServer.getInputStream());
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
                while(mainServer.isConnected()) {
                    msgFromGroupChat=scanner.nextLine();
                    System.out.println(msgFromGroupChat);
                }
            }
        }).start();
    }

    public void addClient(Socket client){
        this.clients.add(client);
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
}
