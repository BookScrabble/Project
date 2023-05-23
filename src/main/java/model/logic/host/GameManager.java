package model.logic.host;

import model.logic.host.data.Tile;
import model.logic.host.data.Word;
import model.logic.server.MyServer;
import model.logic.host.data.GameData;
import model.logic.host.data.Player;

import java.net.Socket;

public class GameManager implements GameHandler {
    private static GameManager single_instance = null;
    MyServer host;
    Socket calculationsServer;
    GameData gameData;

    int currentPlayer;

    private GameManager() {
    }

    /**
     * @Details Returns the single instance of the GameManager class.
     *
     * @return The GameManager instance.
     */
    public static GameManager get() {
        if (single_instance == null)
            single_instance = new GameManager();
        return single_instance;
    }

    /**
     * @Details Adds a player to the game.
     */
    public void addPlayer() {
        if(gameData.getAllPlayers().size()<4){
            //TODO - We need to know which player is it.
        }
    }

    /**
     * @Details Submits a word to the game.
     *
     * @params word The word to be submitted.
     */
    public void submit(String wordPosition) {
        if(wordPosition.length() != 0){
            String[] wordData = wordPosition.split(",");

            String word = wordData[0];
            int row = Integer.parseInt(wordData[1]);
            int col = Integer.parseInt(wordData[2]);
            boolean vertical = Boolean.parseBoolean(wordData[3]);
            Tile[] tiles = new Tile[word.length()];

            Player player = gameData.getPlayer(currentPlayer);
            for(int i=0; i<word.length(); i++){
                tiles[i] = player.getTile(word.charAt(i));
            }
            gameData.getBoard().tryPlaceWord(new Word(tiles, row, col, vertical));
        }
    }

    /**
     * @Details Challenges the last submitted word.
     */
    public void challenge(String word) {
        if( word.length() != 0){
            //TODO - Challenge word
        }
    }

    /**
     * @Details Swaps tiles for a player.
     */
    public void swapTiles() {
        //TODO - We need to know which player is it.
    }

    /**
     * @Details Resigns from the game.
     */
    public void resign() {
        //TODO - We need to know which player is it.
    }

    /**
     * @Details Skips the turn of the current player.
     */
    public void skipTurn() {
        //TODO - We need to know which player is it.
    }

    public void sort() {
        //TODO - We need to know which player is it.
    }

    public GameData getGameData() {
        return this.gameData;
    }
}