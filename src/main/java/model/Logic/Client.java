package model.Logic;

import model.Data.Player;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Client{
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
            run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        new Thread( () -> {
            String msgFromGroupChat;
            while(server.isConnected()) {
                if(scanner.hasNextLine()) {
                    msgFromGroupChat = scanner.nextLine();
                    if(Objects.equals(msgFromGroupChat, "1")) {
                        System.out.println("1" + " from Client(run)");
                        turn();
                    }
                    else if(Objects.equals(msgFromGroupChat, "2")) {
                        System.out.println("2" + " from Client(run)");
                        turn();
                    }
                    else{
                        System.out.println(msgFromGroupChat + " HI");
                    }
                    msgFromGroupChat = "";
                }
            }
        }).start();
    }

    public void turn(){
        new Thread(()->{
            if(server.isConnected()){
                BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
                String msgFromGroupChat;
                try {
                    System.out.println("before readLine");
                    msgFromGroupChat= bf.readLine();
                    System.out.println("after readLine");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                printWriter.println(msgFromGroupChat);
                printWriter.flush();
            }
        }).start();
    }

    // Disconnect
    // Close everything


    // TryPlaceWord


    // Skip turn


    // Challenge


    //

}
