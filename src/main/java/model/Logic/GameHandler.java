package model.Logic;

import model.Data.Word;

import java.net.Socket;

public interface GameHandler {

    void addPlayer();
    void submit(String word);
    void challenge (String word);
    void swapTiles();
    void resign();
    void skipTurn();
    void sort();
}
