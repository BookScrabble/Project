package model.logic.host;

import model.logic.host.data.Tile;
import model.logic.host.data.Word;
import model.logic.server.HostServer;
import model.logic.host.data.GameData;
import model.logic.host.data.Player;


import java.io.*;
import java.net.Socket;
import java.util.*;

public class GameManager implements GameHandler,Serializable {
    private static GameManager single_instance = null;
    HostServer host;
    GameData gameData;
    String calculationServerIp;
    int calculationServerPort;
    private TurnManager turnManager;

    private GameManager() {
        //host = new HostServer(port, new GuestHandler());
        calculationServerPort = 10000;
        calculationServerIp = "localhost";
        gameData = new GameData();
        //host.start();
        turnManager = null;
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
     * @details Randomize 1 tile for each connected players and initialize turnManager Class to manage turns.
     */
    public void initializeTurnManager(){
        for(Player player : gameData.getAllPlayers().values()) addTile(player);
        turnManager = new TurnManager(gameData.getAllPlayers());
    }


    public void initializeHostServer(int port){
        host = new HostServer(port, new GuestHandler());
        host.start();
    }

    /**
     * @return the game status.
     * TODO - Remove if no use, otherwise delete TODO.
     */
    public boolean isGameRunning() {
        return host.isGameRunning();
    }

    /**
     * @details Used to initialize turn manager, Show players which tile they got. and later prompt players for
     * the turn order decided by turn manager.
     */
    public void initializeGame(){
        //if(turnManager != null) return;
        //initializeTurnManager();
        //updateGuests("tiles");
        System.out.println("Test clients connected size -> " + this.getGameData().getAllPlayers().size());
    }

    /**
     * @details Used to initiate the game. Initialize turn manager if needed, fill all
     * players hands and uses HostServer startGame method.
     */
    public void startGame() {
        if(turnManager == null){
            initializeGame();
        }
        for(Player player : this.gameData.getAllPlayers().values()){
            fillHand(player);
        }
        updateGuests("tiles");
        this.host.startGame();
    }

    /**
     * @details fill hand of player(maximum 7 tiles).
     */
    public void fillHand(Player player) {
        while(player.getAllTiles().size() < 7){
            player.getAllTiles().add(Tile.Bag.getBag().getRand());
        }
    }

    /**
     * @details Adds random tile to player.
     * @param player - given player to add tile to.
     */
    private void addTile(Player player){
        if(player.getAllTiles().size() < 7) player.addTile(gameData.getBag().getRand());
    }

    /**
     * @Details Adds a player to the game.
     */
    public void addPlayer(String clientName) {
        if(gameData.getAllPlayers().size()<4){
            gameData.addPlayer(gameData.getAllPlayers().size() + 1, new Player(clientName));
        }
    }

    /**
     * @Details Submits a word to the game.
     * @params word The word to be submitted.
     */
    public String submit(String wordData) {
        if(wordData.length() != 0){
            String[] splitData = wordData.split(",");

            String word = splitData[0];
            int row = Integer.parseInt(splitData[1]);
            int col = Integer.parseInt(splitData[2]);
            boolean vertical = Boolean.parseBoolean(splitData[3]);
            Tile[] tiles = new Tile[word.length()];

            Player currentPlayer = gameData.getPlayer(turnManager.getCurrentPlayerTurn());
            if(currentPlayer == null) return "Player Is Not Found!";

            Word newWord = buildWord(currentPlayer, word, row, col, vertical);
            int score = gameData.getBoard().tryPlaceWord(newWord);
            if (score == 0) {
                return "0";
            }
            else if(score == -1){
                return "-1";
            }else{
                currentPlayer.setScore(currentPlayer.getScore() + score);
                fillHand(currentPlayer);
                updateGuests("turnEnded");
                return "1";
            }
        }
        return "-2";
    }

    /**
     * @Details Updates all connected guests if change was made in game state.
     */
    public void updateGuests(String change){
        /*
        changes so far:
        -"tiles"
        -"turnEnded"
        -
         */

    }

    /**
     * @Details Builds a word from the given data.
     * @return new word.
     */
    private Word buildWord(Player player, String word, int row, int col,boolean vertical){
        Tile[] tiles = new Tile[word.length()];
        for(int i=0; i<word.length(); i++){
            tiles[i] = player.getTile(word.charAt(i));
        }
        return new Word(tiles, row, col, vertical);
    }

    /**
     * @param word that the user believe is found within dictionary and would like to challenge for IO Search.
     * @Details Uses helper method to communicate with Calculation server which runs the Challenge query on given word.
     */
    public String challenge(String word) {
        return sendToCalculationServer("C," + gameData.getDictionaries() + "," + word);
    }

    /**
     * @Details Query last player submitted word.
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
            String result = scanner.nextLine(); //Blocking call waiting for answer.
            printWriter.close();
            scanner.close();
            socket.close();
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Details Swaps tiles for a player.
     */
    public void swapTiles() {
        //TODO - Implementation required
    }

    /**
     * @Details Resigns from the game.
     */
    public void resign() {
        //TODO - Implementation required
    }

    /**
     * @Details Skips the turn of the current player.
     */
    public void skipTurn() {
        turnManager.nextTurn();
    }

    public void sort() {
        //TODO - Implementation required
    }

    public GameData getGameData() {
        return this.gameData;
    }

    /**
     * @details Returns the ID of current players turn.
     */
    public int getCurrentPlayerID() {
        return turnManager.getCurrentPlayerTurn();
    }

    /**
     * @details Returns the turnManager class.
     */
    public TurnManager getTurnManager(){
        return this.turnManager;
    }

    public void removePlayer(int playerId){
        gameData.removePlayer(playerId);
        host.removePlayer(playerId);
        turnManager.removePlayer(playerId);
    }

    /**
     * @details Stops current game, Skips current player turn to stop current
     * action and calls HostServer stopGame method.
     */
    public void stopGame(){
        this.turnManager.nextTurn();
        this.host.stopGame();
    }

    public void setCalculationServerIp(String calculationServerIp) {
        this.calculationServerIp = calculationServerIp;
    }

    public String getCalculationServerIp() {
        return calculationServerIp;
    }
}