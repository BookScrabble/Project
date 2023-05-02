package Model.Data;

import java.util.ArrayList;
import java.util.List;
import Model.Data.Tile.Bag;

public class Player {
    String name;
    int score;
    List<Tile> tiles;

    public Player(String name){
        this.name = name;
        this.score = 0;
        this.tiles = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }

    /**
     *
     * @details  Make sure the player always has 7 tiles
     */
    public void checkHand(){
        while(tiles.size() < 7){
            tiles.add(Bag.getBag().getRand());
        }
    }
}
