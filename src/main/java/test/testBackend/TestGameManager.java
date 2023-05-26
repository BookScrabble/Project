package test.testBackend;

import model.logic.client.Client;
import model.logic.host.GameManager;
import model.logic.host.data.GameData;
import model.logic.host.data.Tile;
import model.logic.host.data.Word;
import model.logic.server.MyServer;
import model.logic.server.dictionary.BookScrabbleHandler;

public class TestGameManager {

    public static void main(String[] args) {
        GameManager gameManager = GameManager.get(); //Launches host server in the background.
        gameManager.getGameData().setDictionaries("alice_in_wonderland.txt","Frank Herbert - Dune.txt");
        Client lior = new Client("localhost", 20000, "Lior");
        String submitResult = gameManager.submit("BELIEVE,5,3,vertical");
        System.out.println("Result of submit method: " + submitResult); //Expected -1, Word was sent without tiles because player has none.
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
        String challengeResult = gameManager.challenge("BELIEVE");
        System.out.println("Result of challenge method: " + challengeResult);
        //gameManager.updateGuests(); TODO - Test this method when working with Multi client(thread pool).
    }
}
