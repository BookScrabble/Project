package model.logic.server;

import model.logic.client.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;


public class HostServer extends MyServer {

    private final List<Socket> sockets;

    public HostServer(int port, ClientHandler clientHandler) {
        super(port, clientHandler);
        this.sockets = new ArrayList<>();
    }

    @Override
    protected void runServer() throws Exception {
        try {
            ServerSocket server = new ServerSocket(this.port);
            server.setSoTimeout(1000);
            while (!stop) {
                try {
                    Socket aClient = server.accept(); // blocking call
                    try {
                        sockets.add(aClient);
                        clientHandler.handleClient(aClient.getInputStream(), aClient.getOutputStream());
                    } catch (IOException ignored) {}
                } catch (SocketTimeoutException ignored) {}
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
