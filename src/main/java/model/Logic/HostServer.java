package model.Logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;

public class HostServer extends MyServer {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;


    /**
     * Constructor for MyServer class. Initializes the server with specified port number and client handler.
     *
     * @param port          the port number to use for the server
     * @param clientHandler the client handler to use for handling client connections
     */
    public HostServer(int port, ClientHandler clientHandler) {
        super(port, clientHandler);
    }


    // when does the game end?
}
