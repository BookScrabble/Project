package model.Logic;

import java.io.*;
import java.net.Socket;

public class HostServer extends MyServer {
    private String hostname;
    private int port;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * Constructor for MyServer class. Initializes the server with specified port number and client handler.
     *
     * @param port          the port number to use for the server
     * @param clientHandler the client handler to use for handling client connections
     */
    public HostServer(String username ,int port, ClientHandler clientHandler) {
        super(port, clientHandler);
        this.hostname = username;
    }

    /**
     * Connects to the server and creates input and output streams.
     * @throws IOException if an I/O error occurs when creating the socket or the streams
     */
    public void connect() {
        try {
            socket = new Socket(hostname, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * listen for incoming messages from the server
     * @param message the message to send
     */
    public void listen() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                while(socket.isConnected()) {
                    try {
                        msgFromGroupChat=in.readLine();
                        System.out.println(msgFromGroupChat);
                    } catch (IOException ignored) {}
                }
            }
        }).start();
    }

    /**
     * Sends a message to the server.
     * @param query
     */
    public void sendQuery(String ...query) {
    	for(String q : query) {
    		out.println(q);
    	}
    }

}
