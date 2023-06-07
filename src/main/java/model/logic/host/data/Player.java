package model.logic.host.data;

import java.util.*;

import model.logic.host.data.Tile.Bag;

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

    public Tile getTile(char letter){
        ArrayList<Tile> copyTiles = new ArrayList<>(tiles);
        for (Tile tile : copyTiles) {
            if (tile.letter == letter) {
                tiles.remove(tile);
                return tile;
            }
        }
        return null;
    }

    public List<Tile> getAllTiles() {
        return tiles;
    }

    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }

    public void addTile(Tile tile){
        this.tiles.add(tile);
    }

}
