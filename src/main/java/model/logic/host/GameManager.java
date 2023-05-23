package model.logic.host;

import model.logic.host.data.Tile;
import model.logic.host.data.Word;
import model.logic.server.MyServer;
import model.logic.host.data.GameData;
import model.logic.host.data.Player;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GameManager implements GameHandler {
    private static GameManager single_instance = null;
    MyServer host;
    GameData gameData;
    int currentPlayerID;

    String calculationServerIp;
    int calculationServerPort;

    private GameManager(int port) {
        host = new MyServer(port, new GuestHandler());
        calculationServerPort = 10000;
        calculationServerIp = "localhost";
        host.start();
    }

    /**
     * @Details Returns the single instance of the GameManager class.
     *
     * @return The GameManager instance.
     */
    public static GameManager get() {
        if (single_instance == null)
            single_instance = new GameManager(20000);
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

            Player currentPlayer = gameData.getPlayer(currentPlayerID);
            Word newWord = buildWord(currentPlayer, word, row, col, vertical);
            int score = gameData.getBoard().tryPlaceWord(newWord);
            if (score == 0) {
                //return the tiles to the currentPlayer, DictionaryLegal, Player can do challenge
            }
            else if(score == -1){
                // BoardLegal
            }else{
                int oldScore = currentPlayer.getScore();
                currentPlayer.setScore(oldScore + score);
                // add new tiles to the player
                // update everyone new gameState
            }
        }
    }

    /**
     * @Details Builds a word from the given data.
     * @return The word.
     */
    private Word buildWord(Player player, String word, int row, int col,boolean vertical){
        Tile[] tiles = new Tile[word.length()];
        for(int i=0; i<word.length(); i++){
            tiles[i] = player.getTile(word.charAt(i));
        }
        return new Word(tiles, row, col, vertical);
    }

    /**
     * @Details Challenges the last submitted word.
     */
    public String challenge(String word) {
        StringBuilder sb = new StringBuilder();
        sb.append("C,").append(gameData.getDictionaries()).append(",").append(word);
        return sendToCalculationServer(sb.toString());
    }

    /**
     * @Details Query the last submitted word.
     */
    public String query(Word word) {
        StringBuilder sb = new StringBuilder();
        StringBuilder wordString = new StringBuilder();
        for(Tile tile : word.getTiles()){
            wordString.append(tile.letter);
        }
        sb.append("Q,").append(gameData.getDictionaries()).append(",").append(wordString);
        return sendToCalculationServer(sb.toString());
    }

    /**
     * @params String | The message to be sent.
     * @Details Connect and Sends a message to the calculation server.
     * @return The result of the calculation server.
     */
    private String sendToCalculationServer(String w){
        try {
            Socket socket = new Socket(calculationServerIp, calculationServerPort);
            Scanner scanner = new Scanner(socket.getInputStream());
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println(w);
            String result = scanner.nextLine();
            printWriter.close();
            scanner.close();
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
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