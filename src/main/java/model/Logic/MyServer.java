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
    private final int port;
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
    private void runServer() throws Exception {
        try {
            ServerSocket server = new ServerSocket(this.port);
            server.setSoTimeout(1000);
            while (!stop) {
                if(this.getClass() != HostServer.class){
                    try {
                        Socket aClient = server.accept(); // blocking call
                        System.out.println("A new client has connected!");
                    } catch (SocketTimeoutException ignored) {}
                }else{
                    HostServer hs = (HostServer) this;
                    while(hs.getGameIsRunning()){
                        PrintWriter printWriter = new PrintWriter(hs.getClients().get(0).getOutputStream(),true);
                        printWriter.println("1");
                        Scanner scanner = new Scanner(hs.getClients().get(0).getInputStream());
                        if(scanner.hasNextLine()){
                            String msg = scanner.nextLine();
                            printWriter.println(msg);
                        }
                        hs.stopGame();
                    }
                    try {
                        if(hs.getClients().size() != 4){
                            Socket aClient = server.accept(); // blocking call
                            hs.addClient(aClient);
                            System.out.println("A new guest has connected!");
                        }else{
                            System.out.println("Game is full!");
                        }
                    } catch (SocketTimeoutException ignored) {}

                }
            }
            server.close();
        } catch (SocketException ignored) {
        }
    }

    private void hostTest(){
        System.out.println("Client connected to a host not just mainServer");
    }
}
