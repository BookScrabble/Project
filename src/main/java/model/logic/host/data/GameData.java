package model.logic.host.data;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

public class GameData {
    Board board;
    Tile.Bag bag;
    Map<Integer, Player> playerData;
    ArrayList<String> dictionaries;

    /**
     * @details  Constructor for GameData class. Initializes the game data with specified board, bag, player data and dictionaries.
     * @params board, bag, dictionaries
     */
    public GameData(Board board, Tile.Bag bag, ArrayList<String> dictionaries) {
        this.board = board;
        this.bag = bag;
        this.dictionaries = dictionaries;
    }

    /**
     * @return Returns the board of the game.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @return Returns the bag of the game.
     */
    public Tile.Bag getBag() {
        return bag;
    }

    /**
     * @return Returns the all players data of the game.
     */
    public Map<Integer, Player> getAllPlayers() {
        return playerData;
    }

    /**
     * @return Returns the player data of the game.
     */
    public Player getPlayer(int playerId) {
        return playerData.get(playerId);
    }

    /**
     * @details set the player data of the game.
     */
    public void addPlayer(int playerId, Player playerData) {
        if(!this.playerData.containsKey(playerId)){
            this.playerData.put(playerId, playerData);
        }
    }

    /**
     * @return Returns the dictionary of the game in string format.(dictionary1,dictionary2,...)
     */
    public String getDictionaries() {
        StringBuilder query = new StringBuilder();
        for (String dictionary : dictionaries) {
            query.append(dictionary).append(",");
        }
        return query.toString();
    }

    /**
     * @details set the dictionaries of the game.
     */
    public void setDictionaries(ArrayList<String> dictionaries) {
        this.dictionaries = dictionaries;
    }

    /**
     * @details add a single dictionary to the dictionaries of the game.
     */
    public void addDictionary(String dictionary){
        if(!this.dictionaries.contains(dictionary)){
            this.dictionaries.add(dictionary);
        }
    }

}