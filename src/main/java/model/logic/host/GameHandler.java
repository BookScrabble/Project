package model.logic.host;

import model.logic.host.data.Word;

public interface GameHandler {

    void addPlayer(String clientName);
    String submit(String word);
    String challenge (String word);
    String query(Word word);
    void swapTiles();
    void skipTurn();
}
