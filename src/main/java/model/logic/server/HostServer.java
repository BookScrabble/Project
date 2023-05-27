package model.logic.server;

import model.logic.client.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;


public class HostServer extends MyServer {

    private final Map<Integer, Socket> clients;

    public HostServer(int port, ClientHandler clientHandler) {
        super(port, clientHandler);
        this.clients = new HashMap<>();
    }

    @Override
    protected void runServer() throws Exception {
        try {
            ServerSocket server = new ServerSocket(this.port);
            server.setSoTimeout(1000);
            while (!stop) {
                try {
                    Socket aClient = server.accept(); // blocking call
                    clients.put(clients.size()+1, aClient);
                    try {
                        clientHandler.handleClient(aClient.getInputStream(), aClient.getOutputStream());
                    } catch (IOException ignored) {}
                } catch (SocketTimeoutException ignored) {}
            }
            server.close();
        } catch (SocketException ignored) {}
    }

    public void connectClients() {

    }


    @Override
    public void close() {
        this.stop = true;
        this.clientHandler.close();
        for(Socket aClient : clients.values()) {
            try {
                aClient.close();
            } catch (IOException ignored) {}
        }
    }
}
