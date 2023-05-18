package model.logic.client;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Client{
    private Socket server;
    private Scanner scanner;
    private PrintWriter printWriter;

    private String username;

    /**
     * Client constructor which both initialize the client parameters but also
     * opens a thread for this client to listen on.
     * @param ip - Ip of the Host server.
     * @param port - Port 0f the Host server.
     * @param clientName - Client name for the game.
     */
    public Client(String ip, int port, String clientName){
        try {
            this.server = new Socket(ip, port);
            this.printWriter = new PrintWriter(this.server.getOutputStream());
            this.scanner = new Scanner(this.server.getInputStream());
            username = clientName;
            /*
            TODO - Send host server the username
             */
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
                        System.out.println("1" + " from Client(run) " + username);
                        turn();
                    }
                    else if(Objects.equals(msgFromGroupChat, "2")) {
                        System.out.println("2" + "server received your message " + username);
                    }
                    else{
                        System.out.println(msgFromGroupChat + " HI " + username);
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
