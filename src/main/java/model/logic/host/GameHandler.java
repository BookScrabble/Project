package model.logic.host;

import model.logic.host.data.Word;

public interface GameHandler {

    void addPlayer();
    void submit(String word);
    String challenge (String word);
    String query(Word word);
    void swapTiles();
    void resign();
    void skipTurn();
    void sort();
}
