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

    public TurnManager(Map<Integer,Player> players){
        playersTurn = new ArrayList<>();
        playersTurn = players.keySet().stream()
                .sorted((firstPlayerID, secondPlayerID) -> players.get(secondPlayerID)
                        .getAllTiles().get(0).score - players.get(firstPlayerID).getAllTiles().get(0).score)
                .collect(Collectors.toCollection(ArrayList::new));
        currentPlayerTurn = -1; //Initiated to -1 until game is starting.
    }

    public void nextTurn(){
        this.currentPlayerTurn = (this.currentPlayerTurn+1)%playersTurn.size();
    }

    public int getCurrentPlayerTurn(){
        return playersTurn.get(currentPlayerTurn);
    }

    public List<Integer> getPlayersTurn(){
        return this.playersTurn;
    }

    public void setCurrentPlayerTurn(int newTurn){
        this.currentPlayerTurn = newTurn;
    }

    public void removePlayer(int playerId){
        this.playersTurn.remove((Object)playerId);
    }

    public int getTurnManagerIndex(){
        return currentPlayerTurn;
    }


}
