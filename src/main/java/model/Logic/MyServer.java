package model.Logic;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class MyServer {
    private final int port;
    private final ClientHandler clientHandler;
    private volatile boolean stop;

    public MyServer(int port, ClientHandler clientHandler) {
        this.port = port;
        this.clientHandler = clientHandler;
        this.stop = false;
    }

    public void start() {
        new Thread(()-> {
            try {
                runServer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void close() {
        this.stop = true;
        this.clientHandler.close();
    }

    private void runServer() throws Exception {
        try {
            ServerSocket server = new ServerSocket(this.port);
            server.setSoTimeout(1000);
            while (!stop) {
                try {
                    Socket aClient = server.accept(); // blocking call
                    try {
                        clientHandler.handleClient(aClient.getInputStream(), aClient.getOutputStream());
                        aClient.getInputStream().close();
                        aClient.getOutputStream().close();
                        aClient.close();
                    } catch (IOException ignored) {}
                } catch (SocketTimeoutException ignored) {}
            }
            server.close();
        } catch (SocketException ignored) {}
    }
}
