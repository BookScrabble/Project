package model.logic.server;

import model.logic.client.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class HostServer extends MyServer {

    private final List<Socket> sockets;
    private int currentPlayerTurn;

    public HostServer(int port, ClientHandler clientHandler) {
        super(port, clientHandler);
        this.sockets = new ArrayList<>();
        currentPlayerTurn =0;
    }

    @Override
    protected void runServer() throws Exception {
        try {
            ServerSocket server = new ServerSocket(this.port);
            while (!stop) {
                // Connect clients
                server.setSoTimeout(1000);
                if(sockets.size() < 1){
                    try {
                        Socket aClient = server.accept(); // blocking call
                        sockets.add(aClient);
                    } catch (SocketTimeoutException ignored) {}
                }else{
                    //Handle clients
                    if(currentPlayerTurn == sockets.size()){
                        currentPlayerTurn = 0;
                    }
                    new Thread(()-> {
                        try {
                            clientHandler.handleClient(sockets.get(currentPlayerTurn).getInputStream(), sockets.get(currentPlayerTurn).getOutputStream());
                        } catch (IOException ignored) {}
                    }).start();
                    currentPlayerTurn = (currentPlayerTurn+1) % sockets.size();
                }
            }
            server.close();
        } catch (SocketException ignored) {}
    }


    @Override
    public void close() {
        this.stop = true;
        this.clientHandler.close();
        for(Socket aClient : sockets) {
            try {
                aClient.close();
            } catch (IOException ignored) {}
        }
    }
}
