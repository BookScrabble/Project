package test;

import model.logic.client.Client;
import model.logic.host.GameManager;
import model.logic.server.MyServer;
import model.logic.server.dictionary.BookScrabbleHandler;

public class TestServerArchitecture {
    public static void main(String[] args) {
        MyServer mainServer = new MyServer(10000, new BookScrabbleHandler());
        mainServer.start();
        GameManager gameManager = GameManager.get(); // Launch host server in the background.
        gameManager.getGameData().setDictionaries("alice_in_wonderland.txt", "Harry Potter.txt");
        Client firstPlayer = new Client("localhost", 20000, "Lior");
//        firstPlayer.turn();
    }
}
