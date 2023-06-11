package test.testBackend;

import model.logic.client.Client;
import model.logic.host.GameManager;
import model.logic.host.data.Player;
import model.logic.server.MyServer;
import model.logic.server.dictionary.BookScrabbleHandler;

public class TestTurnManager {

    public static void main(String[] args) {
        //Initialize calculation server for dictionary checks:
        MyServer calculationServer = new MyServer(10000, new BookScrabbleHandler());
        calculationServer.start();

        //Initialize GameManager:
        GameManager gameManager = GameManager.get();
        gameManager.getGameData().setDictionaries("alice_in_wonderland.txt","Frank Herbert - Dune.txt");

        //Connect players:
        //Player 1 ->
        Client Lior = new Client("localhost", 20000, "Lior");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Player 2 ->
        Client Idan = new Client("localhost", 20000, "Idan");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Player 3 ->
        Client Burger = new Client("localhost", 20000, "Burger");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Player 4 ->
        Client Liav = new Client("localhost", 20000, "Liav");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Start game (GameManager) and initialize turnManager:
        gameManager.startGame();

        //Check turnManager shuffle turns:
        //gameManager.initializeTurnManager();
        for(Player player : gameManager.getGameData().getAllPlayers().values()){
            System.out.println("Player " + player.getName() + " Got tiles: " + player.getAllTiles().get(0).score);
        }

        System.out.println(gameManager.getTurnManager().getPlayersTurn());

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        gameManager.stopGame();
        calculationServer.close();

        System.out.println("Main is dead!");
    }
}
