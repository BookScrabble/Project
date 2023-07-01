package model.logic.host.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GameData implements Serializable {
    Board board;
    Tile.Bag bag;
    Map<Integer, Player> playerData;
    ArrayList<String> dictionaries;


    /**
     * The GameData function is a constructor for the GameData class.
     * It initializes the board, bag, player data and dictionaries.
     */
    public GameData() {
        this.board = Board.getBoard();
        this.bag = Tile.Bag.getBag();
        this.playerData = new HashMap<>();
        this.dictionaries = new ArrayList<>();
    }


    /**
     * The getBoard function returns the board object.
     * @return The board object
     */
    public Board getBoard() {
        return board;
    }


    /**
     * The getBag function returns the bag of tiles that is associated with a player.
     * @return The bag of the tile
     */
    public Tile.Bag getBag() {
        return bag;
    }


    /**
     * The getAllPlayers function returns a map of all the players in the game.
     * @return A map of all the players in the game
     */
    public Map<Integer, Player> getAllPlayers() {
        return playerData;
    }


    /**
     * The getPlayer function returns the player object associated with a given playerId.
     * @param playerId playerId Get the player from the hashmap
     * @return A player object
     */
    public Player getPlayer(int playerId) {
        return playerData.get(playerId);
    }


    /**
     * The addPlayer function adds a player to the game.
     * @param playerId playerId Identify the player
     * @param playerData playerData Add a player to the game
     */
    public void addPlayer(int playerId, Player playerData) {
        if(!this.playerData.containsKey(playerId)){
            this.playerData.put(playerId, playerData);
        }
    }


    /**
     * The getDictionaries function returns a string of all the dictionaries that are currently being used by the program.
     * @return A string of all the dictionaries in the list
     */
    public String getDictionaries() {
        StringBuilder query = new StringBuilder();
        for (String dictionary : dictionaries) {
            query.append(dictionary).append(",");
        }
        query.deleteCharAt(query.length()-1);
        return query.toString();
    }


    /**
     * The setDictionaries function takes a variable number of arguments and
     * stores them in an ArrayList.
     * @param args args Pass in an array of strings
     */
    public void setDictionaries(String... args) {
        this.dictionaries = Arrays.stream(args).
                collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * The removePlayer function removes a player from the game.
     * @param playerId playerId Identify the player that is being removed from the game
     */
    public void removePlayer(int playerId){
        this.playerData.remove(playerId);
    }

}
