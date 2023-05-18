package model.Logic;

import model.Data.Word;

import java.net.Socket;

public interface GameHandler {

    void addPlayer();
    void submit(Word word);
    void challenge ();
    void swapTiles(Socket player, int[] tilesToSwap);
    void resign();
    void skipTurn();
}
