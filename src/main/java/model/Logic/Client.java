package model.Logic;

import model.Data.Player;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket server;
    private Scanner scanner;
    private PrintWriter printWriter;
    private Player player;

    public Client(String ip, int port, String clientName){
        try {
            this.server = new Socket(ip, port);
            this.printWriter = new PrintWriter(this.server.getOutputStream());
            this.scanner = new Scanner(this.server.getInputStream());
            player = new Player(clientName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Disconnect
    // Close everything


    // TryPlaceWord


    // Skip turn


    // Challenge


    //

}
