package model.logic.host.data;

import java.io.Serializable;
import java.util.*;

;
/**
 * The Player class represents a player in the game.
 */
public class Player implements Serializable {
    String name;
    int score;
    List<Tile> tiles;

    /**
     * Constructs a new Player object with the specified name.
     * @param name The name of the player.
     */
    public Player(String name){
        this.name = name;
        this.score = 0;
        this.tiles = new ArrayList<>();
    }

    /**
     * Retrieves the name of the player.
     * @return The name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the player.
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the score of the player.
     * @return The score of the player.
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the score of the player.
     * @param score The score to set.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Retrieves a tile with the specified letter from the player's tiles.
     * If a tile with the specified letter is found, it is removed from the player's tiles.
     * @param letter The letter of the tile to retrieve.
     * @return The retrieved tile, or null if no tile with the specified letter is found.
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
     * Retrieves all the tiles currently held by the player.
     * @return A list of all the player's tiles.
     */
    public List<Tile> getAllTiles() {
        return tiles;
    }

    /**
     * Sets the tiles currently held by the player.
     * @param tiles The list of tiles to set.
     */
    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }

    /**
     * Adds a tile to the player's tiles.
     * @param tile The tile to add.
     */
    public void addTile(Tile tile){
        this.tiles.add(tile);
    }

}
