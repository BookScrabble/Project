package model.logic.host.data;

import java.io.Serializable;
import java.util.*;

public class Player implements Serializable {
    String name;
    int score;
    List<Tile> tiles;

    /**
     * The Player function is a constructor for the Player class.
     * It takes in a String name and sets it as the player's name,
     * initializes their score to 0, and creates an ArrayList of Tiles called tiles.
     * @param name name Set the name of the player
     */
    public Player(String name){
        this.name = name;
        this.score = 0;
        this.tiles = new ArrayList<>();
    }

    /**
     * The getName function returns the name of the person.
     * @return The name of the student
     */
    public String getName() {
        return name;
    }

    /**
     * The setName function sets the name of the person.
     * @param name name Set the name of the object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The getScore function returns the score of the player.
     * @return The score of the player
     */
    public int getScore() {
        return score;
    }

    /**
     * The setScore function sets the score of a player.
     * @param score score Set the score of the player
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * The getTile function takes in a letter and returns the tile with that letter.
     * @param letter letter Find the tile with the corresponding letter
     * @return The tile with the given letter
     */
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

    /**
     * The getAllTiles function returns a list of all the tiles in the game.
     * @return A list of all the tiles in the game
     */
    public List<Tile> getAllTiles() {
        return tiles;
    }

    /**
     * The setTiles function is used to set the tiles of a board.
     * @param tiles&lt;Tile&gt; tiles Set the tiles of a board
     */
    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }

    /**
     * The addTile function adds a tile to the tiles' arraylist.
     * @param tile tile Add a tile to the tiles arraylist
     */
    public void addTile(Tile tile){
        this.tiles.add(tile);
    }

}
