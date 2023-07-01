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

    /**
     * The GameManager function is a singleton class that manages the game.
     * It has a GameData object, which stores all the data for the game.
     * It also has a TurnManager object, which handles all the turns in
     * each round and keeps track of whose turn it is. The GameManager also
     * contains information about where to find its calculation server (the IP address) and what port to use when connecting to it (the port number).
     */
    private GameManager() {
        calculationServerPort = 10000;
        calculationServerIp = "localhost";
        gameData = new GameData();
        turnManager = null;
    }


    /**
     * The get function is a static function that returns the singleton instance of GameManager.
     * If no instance exists, it creates one and then returns it.
     * @return A singleton instance of the game manager class
     */
    public static GameManager get() {
        if (single_instance == null)
            single_instance = new GameManager();
        return single_instance;
    }


    /**
     * The initializeTurnManager function is used to initialize the turnManager object.
     * It does this by adding all players to the turnManager, and then setting up a list of turns for each player.
     */
    public void initializeTurnManager(){
        for(Player player : gameData.getAllPlayers().values()) addTile(player);
        turnManager = new TurnManager(gameData.getAllPlayers());
        System.out.println("Turns decided by turn manager -> " + turnManager.getPlayersTurn());
    }

    /**
     * The initializeHostServer function initializes the host server.
     * @param port port Set the port number that the host server will listen on
     */
    public void initializeHostServer(int port){
        host = new HostServer(port, new GuestHandler());
        host.start();
    }

    /**
     * The isGameRunning function returns a boolean value that indicates whether the game is running.
     * @return A boolean value
     */
    public boolean isGameRunning() {
        return host.isGameRunning();
    }


    /**
     * The initializeGame function initializes the turnManager object, which is used to keep track of whose turn it is.
     * It also sets up the game board and adds all the pieces to their respective teams.
     */
    public void initializeGame(){
        if(turnManager != null) return;
        initializeTurnManager();
    }


    /**
     * The startGame function is called when the game is ready to begin.
     * It initializes the turnManager, fills each player's hand with cards, and then calls startGame on the host.
     */
    public void startGame() {
        if(turnManager == null){
            initializeGame();
        }
        for(Player player : this.gameData.getAllPlayers().values()){
            fillHand(player);
        }
        this.host.startGame();
    }


    /**
     * The fillHand function is used to fill the player's hand with 7 tiles.
     * @param player player Pass in the player object that will be used to fill their hand
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
     * The addPlayer function adds a player to the gameData object.
     * @param clientName clientName Add a new player to the game data
     */
    public void addPlayer(String clientName) {
        if(gameData.getAllPlayers().size()<4){
            gameData.addPlayer(gameData.getAllPlayers().size() + 1, new Player(clientName));
        }
    }


    /**
     * The submit function is called when a player submits a word to the board.
     * It takes in the data of the word, and checks if it can be placed on the board.
     * If it can, then it places that word on the board and updates all necessary information.
     * @param wordData wordData Get the data from the client
     * @return A string, which is then parsed by the front end
     */
    public String submit(String wordData) {
        if(wordData.length() != 0){
            String[] splitData = wordData.split(",");

            String word = splitData[0].toUpperCase();
            int row = Integer.parseInt(splitData[1]);
            int col = Integer.parseInt(splitData[2]);
            boolean vertical = Boolean.parseBoolean(splitData[3]);

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
                return "1";
            }
        }
        return "-2";
    }


    /**
     * The buildWord function takes in a player, a string representing the word to be built,
     * and the row and column of where it will start. It returns an array of tiles that can be used
     * to build the word on the board. If there is no tile at that position (i.e., if it's an empty space),
     * then null is returned instead of a tile object. This function assumes that all words are valid; i.e.,
     * they have been checked by checkWord before being passed into this function!
     * @param player player Get the tiles from a player's rack
     * @param word word Store the word that is being played
     * @param row row Determine the row of the first letter in a word
     * @param col col Determine the column of the word
     * @param vertical vertical Determine whether the word is vertical or horizontal
     * @return A word object
     */
    private Word buildWord(Player player, String word, int row, int col,boolean vertical){
        Tile[] tiles = new Tile[word.length()];
        for(int i=0; i<word.length(); i++){
            if(word.charAt(i) == '_') tiles[i] = null;
            else tiles[i] = player.getTile(word.charAt(i));
        }
        return new Word(tiles, row, col, vertical);
    }


    /**
     * The challenge function sends a challenge request to the calculation server.
     * The format of the request is &quot;C,&lt;dictionary&gt;,&lt;word&gt;&quot;.
     * @param word word Send the word to the calculation server
     * @return A string in the form of &quot;c,&lt;result&gt;,&lt;error&gt;&quot;
     */
    public String challenge(String word) {
        return sendToCalculationServer("C," + gameData.getDictionaries() + "," + word);
    }


    /**
     * The query function takes in a word and returns the score of that word.
     * @param word word Get the tiles in the word string.
     * @return A string that is a comma separated list of words
     */
    public String query(Word word) {
        StringBuilder sb = new StringBuilder();
        StringBuilder wordString = new StringBuilder();
        for(Tile tile : word.getTiles()){
            wordString.append(tile.letter);
        }
        sb.append("Q,").append(gameData.getDictionaries()).append(",").append(wordString);
        System.out.println(sb);
        return sendToCalculationServer(sb.toString());
    }


    /**
     * The sendToCalculationServer function takes a String as input and sends it to the calculation server.
     * The function then waits for an answer from the calculation server, which is returned by this function.
     * @param w w Send the word to the calculation server
     * @return A string
     */
    private String sendToCalculationServer(String w){
        try {
            Socket socket = new Socket(calculationServerIp, calculationServerPort);
            Scanner scanner = new Scanner(socket.getInputStream());
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println(w);
            String result = scanner.nextLine();
            socket.close();
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * The swapTiles function is called when the player clicks on the &quot;Swap Tiles&quot; button.
     * It removes all the tiles from a player's hand and replaces them with new ones.
     */
    public void swapTiles() {
        Player player = gameData.getPlayer(turnManager.getCurrentPlayerTurn());
        if(player != null){
            for(Tile t : player.getAllTiles()){
                Tile.Bag.getBag().put(t);
            }
            player.setTiles(new ArrayList<>());
            fillHand(player);
        }
        skipTurn();
    }


    /**
     * The skipTurn function is called when the player clicks on the &quot;Skip Turn&quot; button.
     * It resets the timer task, which will cause it to be run again after a certain amount of time has passed.
     */
    public void skipTurn() {
        GameManager.get().host.resetTimerTask();
    }

    /**
     * The getGameData function returns the gameData object.
     * @return The game data object
     */
    public GameData getGameData() {
        return this.gameData;
    }


    /**
     * The getCurrentPlayerID function returns the ID of the current player.
     * @return The current player id
     */
    public int getCurrentPlayerID() {
        return turnManager.getCurrentPlayerTurn();
    }


    /**
     * The getTurnManager function returns the turnManager object.
     * @return The turn manager object
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
     * The stopGame function is called when the game has ended.
     * It calls the nextTurn function in turnManager, which will end the game.
     * Then it calls stopGame in host, which will close all of its sockets and streams.
     */
    public void stopGame(){
        if(this.turnManager != null) this.turnManager.nextTurn();
        this.host.stopGame();
    }

    /**
     * The setCalculationServerIp function sets the calculationServerIp variable to the value of its parameter.
     * @param calculationServerIp calculationServerIp Set the value of the calculationServerIp variable
     */
    public void setCalculationServerIp(String calculationServerIp) {
        this.calculationServerIp = calculationServerIp;
    }

    /**
     * The getCalculationServerIp function returns the IP address of the calculation server.
     * @return The ip address of the calculation server
     */
    public String getCalculationServerIp() {
        return calculationServerIp;
    }
}