package model.logic.host;

import model.logic.host.data.Player;
import model.logic.host.data.Tile;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TurnManager {

    private int currentPlayerTurn;
    private List<Integer> playersTurn;

    public TurnManager(Map<Integer,Player> players){
        playersTurn = new ArrayList<>();
        playersTurn = players.keySet().stream()
                .sorted((firstPlayerID, secondPlayerID) -> players.get(secondPlayerID)
                        .getAllTiles().get(0).score - players.get(firstPlayerID).getAllTiles().get(0).score)
                .collect(Collectors.toCollection(ArrayList::new));
        currentPlayerTurn = 0;
    }



}
