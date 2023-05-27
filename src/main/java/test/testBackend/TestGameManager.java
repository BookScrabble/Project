package test.testBackend;

import model.logic.client.Client;
import model.logic.host.GameManager;
import model.logic.host.data.GameData;
import model.logic.host.data.Tile;
import model.logic.host.data.Word;
import model.logic.server.MyServer;
import model.logic.server.dictionary.BookScrabbleHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestGameManager {

    public static void main(String[] args) {
        GameManager gameManager = GameManager.get(); //Launches host server in the background.
        gameManager.getGameData().setDictionaries("alice_in_wonderland.txt","Frank Herbert - Dune.txt");
        Client lior = new Client("localhost", 20000, "Lior");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String submitResult = gameManager.submit("BELIEVE,7,7,vertical");
        System.out.println("Result of submit method: " + submitResult);
        System.out.println("Checking if addPlayer method worked Current player expected[Lior], " +
                "Found in players array: " + gameManager.getGameData().getPlayer(1).getName());
        //gameManager.resign(); TODO - Test this method when implemented.
        //gameManager.swapTiles(); TODO - Test this method when implemented.
        //gameManager.sort(); TODO - Test this method when implemented.
        //gameManager.skipTurn(); TODO - Test this method when implemented.

        //Tests between GameManager and CalculationServer:
        MyServer calculationServer = new MyServer(10000, new BookScrabbleHandler());
        calculationServer.start();

        //Check query method:
        Tile[] tiles = new Tile[7];
        tiles[0] = Tile.Bag.getBag().getTile('B');
        tiles[1] = Tile.Bag.getBag().getTile('E');
        tiles[2] = Tile.Bag.getBag().getTile('L');
        tiles[3] = Tile.Bag.getBag().getTile('I');
        tiles[4] = Tile.Bag.getBag().getTile('E');
        tiles[5] = Tile.Bag.getBag().getTile('V');
        tiles[6] = Tile.Bag.getBag().getTile('E');
        Word word = new Word(tiles, 3, 5, true);
        String queryResult = gameManager.query(word);
        System.out.println("Result of query method: " + queryResult);

        //Check challenge method:
        String challengeResult = gameManager.challenge("believe");
        String challengeResult2 = gameManager.challenge("adjourn");
        String challengeResult3 = gameManager.challenge("adjourn,");
        String challengeResult4 = gameManager.challenge("Adjourn");
        String challengeResult5 = gameManager.challenge("Adjourn,");
        String challengeResult6 = gameManager.challenge("remedies"); //Not found
        String challengeResult7 = gameManager.challenge("remedies--'"); //Found
        System.out.println("Result of challenge method: " + challengeResult);
        System.out.println("Result of challenge method: " + challengeResult2);
        System.out.println("Result of challenge method: " + challengeResult3);
        System.out.println("Result of challenge method: " + challengeResult4);
        System.out.println("Result of challenge method: " + challengeResult5);
        System.out.println("Result of challenge method: " + challengeResult6);
        System.out.println("Result of challenge method: " + challengeResult7);

        //Check submit for player with tiles:
        List<Tile> tilesForPlayer = new ArrayList<>();
        tilesForPlayer.add(Tile.Bag.getBag().getTile('B'));
        tilesForPlayer.add(Tile.Bag.getBag().getTile('E'));
        tilesForPlayer.add(Tile.Bag.getBag().getTile('L'));
        tilesForPlayer.add(Tile.Bag.getBag().getTile('I'));
        tilesForPlayer.add(Tile.Bag.getBag().getTile('E'));
        tilesForPlayer.add(Tile.Bag.getBag().getTile('V'));
        tilesForPlayer.add(Tile.Bag.getBag().getTile('E'));
        gameManager.getGameData().getPlayer(1).setTiles(tilesForPlayer);
        String submitWithTiles = gameManager.submit("BELIEVE,7,7,vertical");
        System.out.println("Result of submit method with tiles in player: " + submitWithTiles);

        //Checking fillHand method after player placed his word.
        System.out.println("Checking tiles in player after he placed his word should be 7, Result: "
                + gameManager.getGameData().getPlayer(1).getAllTiles().size());

        //gameManager.updateGuests(); TODO - Test this method when working with Multi client(thread pool).
    }
}
