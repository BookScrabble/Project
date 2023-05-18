package model.Logic;

import model.Data.Word;
import model.Data.GameData;

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
    public void submit(Word word) {
        //Gamedata.getBoard.tryPlaceWord();
    }

    /**
     * @Details Challenges the last submitted word.
     */
    public void challenge() {
    }

    /**
     * @Details Swaps tiles for a player.
     *
     * @param player      The player socket.
     * @param tilesToSwap The indices of the tiles to be swapped.
     */
    public void swapTiles(Socket player, int[] tilesToSwap) {
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
}