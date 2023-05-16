package model.Logic;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class MyServer {
    protected final int port;
    private final ClientHandler clientHandler;
    private volatile boolean stop;

    /**
     Constructor for MyServer class. Initializes the server with specified port number and client handler.
     @param port the port number to use for the server
     @param clientHandler the client handler to use for handling client connections
     */
    public MyServer(int port, ClientHandler clientHandler) {
        this.port = port;
        this.clientHandler = clientHandler;
        this.stop = false;
    }

    /**
     * Starts the server in a new thread.
     * @throws RuntimeException if an error occurs while starting the server.
     */
    public void start() {
        new Thread(()-> {
            try {
                runServer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    /**
     * Closes the server and stops accepting new connections. Also, closes the client handler.
     */
    public void close() {
        this.stop = true;
        this.clientHandler.close();
    }

    /**
     Starts the server and listens for incoming client connections.
     Once a connection is established, the client handler is called to handle the client's input/output streams.
     The method runs in a loop until the server is stopped.
     @throws Exception if there is an error while creating or closing the server socket
     */
    protected void runServer(){
        try {
            ServerSocket server = null;
            try {
                server = new ServerSocket(this.port);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            server.setSoTimeout(1000);
            while (!stop) {
                Socket aClient = null;
                try {
                    aClient = server.accept(); // blocking call
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    clientHandler.handleClient(aClient.getInputStream(), aClient.getOutputStream());
                    aClient.getInputStream().close();
                    aClient.getOutputStream().close();
                    aClient.close();
                } catch (IOException ignored) {}
            }
            try {
                server.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (SocketException ignored) {}
    }

    private void hostTest(){
        System.out.println("Client connected to a host not just mainServer");
    }
}
