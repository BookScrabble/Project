package test;

import model.logic.client.Client;
import model.logic.host.GameManager;
import model.logic.host.GuestHandler;
import model.logic.server.HostServer;
import model.logic.server.MyServer;
import model.logic.server.dictionary.BookScrabbleHandler;

/**
 * Tests communication between Client --to--> Host and Host --to--> CalculationServer.
 */
public class TestServerArchitecture {
    public static void main(String[] args) {
        MyServer mainServer = new MyServer(10000, new BookScrabbleHandler());
        mainServer.start();
        GameManager gameManager = GameManager.get(); // Launch host server in the background.
        Client firstPlayer = new Client("localhost", 20000, "Lior");
//        firstPlayer.turn();
    }
}
