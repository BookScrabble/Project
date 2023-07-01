package model.logic.host;

import model.logic.host.data.Player;
import model.logic.host.data.Tile;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TurnManager implements Serializable {
    private int currentPlayerTurn;
    private List<Integer> playersTurn;

    /**
     * The TurnManager function is responsible for keeping track of the order in which players take their turns.
     * It does this by maintaining a list of player IDs, sorted from highest to lowest score.
     * The TurnManager function also keeps track of whose turn it currently is, and can be used to determine who's next.
     * @param players&gt; players Get the players from the game
     */
    public TurnManager(Map<Integer,Player> players){
        playersTurn = new ArrayList<>();
        playersTurn = players.keySet().stream()
                .sorted((firstPlayerID, secondPlayerID) -> players.get(secondPlayerID)
                        .getAllTiles().get(0).score - players.get(firstPlayerID).getAllTiles().get(0).score)
                .collect(Collectors.toCollection(ArrayList::new));
        currentPlayerTurn = -1;
    }

    /**
     * The nextTurn function is used to change the current player turn.
     * It does this by incrementing the currentPlayerTurn variable and then using modulo to ensure that it stays within bounds of 0-3.
     */
    public void nextTurn(){
        this.currentPlayerTurn = (this.currentPlayerTurn+1)%playersTurn.size();
    }

    /**
     * The getCurrentPlayerTurn function returns the current player's turn.
     * @return The current player's turn
     */
    public int getCurrentPlayerTurn(){
        return playersTurn.get(currentPlayerTurn);
    }

    /**
     * The getPlayersTurn function returns the list of players who have not yet had their turn.
     * @return The players turn variable, which is a list of integers
     */
    public List<Integer> getPlayersTurn(){
        return this.playersTurn;
    }

    /**
     * The setCurrentPlayerTurn function sets the current player turn to a new value.
     * @param newTurn newTurn Set the current player turn variable to a new value
     */
    public void setCurrentPlayerTurn(int newTurn){
        this.currentPlayerTurn = newTurn;
    }

    /**
     * The removePlayer function removes a player from the game.
     * @param playerId playerId Remove the player from the players turn list
     */
    public void removePlayer(int playerId){
        this.playersTurn.remove((Object)playerId);
    }

    /**
     * The getTurnManagerIndex function returns the index of the current player in turn.
     * @return The index of the player whose turn it currently is
     */
    public int getTurnManagerIndex(){
        return currentPlayerTurn;
    }


}
