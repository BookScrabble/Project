package test.testBackend;

import model.logic.client.Client;
import model.logic.host.GameManager;
import model.logic.host.data.Tile;

import java.util.ArrayList;
import java.util.List;

public class TestHostServer {
    public static void main(String[] args) {
        GameManager gameManager = GameManager.get();
        Client lior = new Client("localhost", 20000, "Lior");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<Tile> tiles = new ArrayList<>();
        tiles.add(Tile.Bag.getBag().getTile('B'));
        tiles.add(Tile.Bag.getBag().getTile('E'));
        tiles.add(Tile.Bag.getBag().getTile('L'));
        tiles.add(Tile.Bag.getBag().getTile('I'));
        tiles.add(Tile.Bag.getBag().getTile('E'));
        tiles.add(Tile.Bag.getBag().getTile('V'));
        tiles.add(Tile.Bag.getBag().getTile('E'));
        gameManager.getGameData().getPlayer(1).setTiles(tiles);
        System.out.println("Expecting 1 player connected, Result: " +
                (gameManager.getGameData().getAllPlayers().size() == 1));
        gameManager.startGame(); //Player("Lior") should get his turn.

    }
}
