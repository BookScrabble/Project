package model.logic.host;

import model.logic.server.MyServer;
import model.logic.host.data.GameData;

import java.net.Socket;

public class GameManager implements GameHandler {
    private static GameManager single_instance = null;
    MyServer host;
    Socket calculationsServer;
    GameData gameData;

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
        //Gamedata.addPlayer();
    }

    /**
     * @Details Submits a word to the game.
     *
     * @param word The word to be submitted.
     */
    public void submit(String word) {
        //Gamedata.getBoard.tryPlaceWord();
    }

    /**
     * @Details Challenges the last submitted word.
     */
    public void challenge(String word) {
    }

    /**
     * @Details Swaps tiles for a player.
     */
    public void swapTiles() {
    }

    /**
     * @Details Resigns from the game.
     */
    public void resign() {
    }

    /**
     * @Details Skips the turn of the current player.
     */
    public void skipTurn() {
    }

    public void sort() {
    }

    public GameData getGameData() {
        return this.gameData;
    }
}