package view;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.logic.host.MySocket;
import model.logic.host.data.Board;
import model.logic.host.data.Player;
import view.data.ViewSharedData;
import viewModel.ViewModel;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;

public class GameController {
    String word;
    boolean vertical;
    int startRow;
    int startCol;
    int flag;
    ArrayList<Integer> indexRow;
    ArrayList<Integer> indexCol;
    @FXML
    GridPane boardGridPane;
    ViewSharedData viewSharedData;
    @FXML
    Label firstPlayerName;
    @FXML
    Label firstPlayerScore;

    @FXML
    Label secondPlayerName;
    @FXML
    Label secondPlayerScore;

    @FXML
    Label thirdPlayerName;
    @FXML
    Label thirdPlayerScore;

    @FXML
    Label fourthPlayerName;
    @FXML
    Label fourthPlayerScore;
    @FXML
    ImageView firstTile;
    @FXML
    ImageView secondTile;
    @FXML
    ImageView thirdTile;
    @FXML
    ImageView fourthTile;
    @FXML
    ImageView fifthTile;
    @FXML
    ImageView sixTile;
    @FXML
    ImageView sevenTile;

    @FXML
    Button skipTurn;
    @FXML
    Button swap;
    @FXML
    Button submit;
    @FXML
    Button exit;

    StringProperty imagePath;
    @FXML
    Button startGame;
    StringProperty playerAction;
    StringProperty messageFromHost;


    public GameController(){
        this.word = "";
        this.vertical = false;
        this.flag = 0;
        this.indexRow = new ArrayList<>();
        this.indexCol = new ArrayList<>();
        this.startGame = new Button();
        this.playerAction = new SimpleStringProperty();
        initiatePlayerName();
        initiatePlayerScore();
        initiatePlayerTiles();
        initiatePlayerButton();
    }

    private void initiatePlayerTiles() {
        firstTile = new ImageView();
        secondTile = new ImageView();
        thirdTile = new ImageView();
        fourthTile = new ImageView();
        fifthTile = new ImageView();
        sixTile = new ImageView();
        sevenTile = new ImageView();
    }

    public void initiatePlayerName(){
        firstPlayerName = new Label();
        secondPlayerName = new Label();
        thirdPlayerName = new Label();
        fourthPlayerName = new Label();
    }

    public void initiatePlayerScore(){
        firstPlayerScore = new Label();
        secondPlayerScore = new Label();
        thirdPlayerScore = new Label();
        fourthPlayerScore = new Label();
    }

    public void initiatePlayerButton(){
        swap = new Button();
        submit = new Button();
        skipTurn = new Button();
    }

    public void toggleStartButton(){
        startGame.setVisible(true);
    }

    public void initializeHostAction(){
        SimpleStringProperty test = new SimpleStringProperty();
        StringProperty newMessageFromHost = viewSharedData.getPlayer().getMessageFromHost();
        test.bind(newMessageFromHost);
        newMessageFromHost.addListener(((observable, oldAction, newAction) -> {
            handleHostAction(newAction);
        }));
        messageFromHost = test;
    }

    public void initializeBoardUpdateAction(){
        imagePath = new SimpleStringProperty();
        ObjectProperty<String> newImagePath = viewSharedData.getViewModel().getImagePath();
        imagePath.bind(newImagePath);
        newImagePath.addListener(((observable, oldAction, newAction) -> {
            updateBoardCellImage(newAction);
        }));
    }

    public void updateBoardCellImage(String newAction){
        String[] trimmed = newAction.split(",");
        int index = Integer.parseInt(trimmed[0]);
        String imageURL = trimmed[1];
        StackPane cell = (StackPane)boardGridPane.getChildren().get(index);
        Label label = (Label)cell.getChildren().get(0);

        boolean hasImageView = false;
        ImageView imageView = new ImageView();
        try{
            imageView = (ImageView)cell.getChildren().get(1);
            hasImageView = true;
        }catch(IndexOutOfBoundsException ignored){}

        if(imageURL.equals("labelVisible")){
            label.setVisible(true);
            if(hasImageView) cell.getChildren().remove(1);
            return;
        }
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(cell.getWidth());
        imageView.setFitHeight(cell.getHeight());
        imageView.setImage(new Image(imageURL));
        if(!cell.getChildren().contains(imageView)) cell.getChildren().add(imageView);
        label.setVisible(false);
    }

    public void handleHostAction(String action){
        Platform.runLater(() -> {
            switch(action){
                case "loadBoard" -> {
                    try {loadBoard();} catch (IOException ignored) {}
                }
                case "bindButtons" -> {
                    bindButtons();
                    viewSharedData.getViewModel().updateViewProperties();
                }
                case "updateView" -> viewSharedData.getViewModel().updateViewProperties();
                case "dictionaryNotLegal" -> challenge();
                case "boardNotLegal" -> {
                    viewSharedData.getViewModel().updateViewProperties();
                    System.out.println("board placement wasn't legal");
                    resetWordParameters();
                }
                case "turnEnded" -> {
                    resetWordParameters();
                }
                case "challengeFailed" -> {
                    viewSharedData.getViewModel().updateViewProperties();
                    System.out.println("challenge failed");
                }
                case "challengeAccepted" -> {
                    viewSharedData.getViewModel().updateViewProperties();
                    System.out.println("challenge accepted");
                }
                case "serverIsClosing" -> {
                    try {loadScoreBoard();} catch (IOException ignored) {}
                }
                case "playerDisconnected" -> playerDisconnected();
            }
        });
    }

    private void playerDisconnected() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Player disconnected update");
        alert.setHeaderText(null);
        alert.setContentText("A player has been disconnected, Updated game data was received.");

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);

        alert.getDialogPane().setExpandableContent(vbox);
        alert.getDialogPane().setExpanded(true);

        alert.showAndWait().ifPresent(response -> {
            alert.close();
        });
    }

    public void bindAll(){
        ViewModel viewModel = this.viewSharedData.getViewModel();

        firstPlayerName.textProperty().bind(viewModel.firstPlayerName);
        secondPlayerName.textProperty().bind(viewModel.secondPlayerName);
        thirdPlayerName.textProperty().bind(viewModel.thirdPlayerName);
        fourthPlayerName.textProperty().bind(viewModel.fourthPlayerName);

        firstPlayerScore.textProperty().bind(viewModel.firstPlayerScore.asString());
        secondPlayerScore.textProperty().bind(viewModel.secondPlayerScore.asString());
        thirdPlayerScore.textProperty().bind(viewModel.thirdPlayerScore.asString());
        fourthPlayerScore.textProperty().bind(viewModel.fourthPlayerScore.asString());

        firstTile.imageProperty().bind(viewModel.firstTile);
        secondTile.imageProperty().bind(viewModel.secondTile);
        thirdTile.imageProperty().bind(viewModel.thirdTile);
        fourthTile.imageProperty().bind(viewModel.fourthTile);
        fifthTile.imageProperty().bind(viewModel.fifthTile);
        sixTile.imageProperty().bind(viewModel.sixTile);
        sevenTile.imageProperty().bind(viewModel.sevenTile);

        viewModel.playerId.bind(viewSharedData.getPlayer().playerId);
        viewSharedData.getPlayer().playTurn.bind(playerAction);
    }

    public void bindButtons(){
        submit.visibleProperty().bind(viewSharedData.getViewModel().submit.get().visibleProperty());
        swap.visibleProperty().bind(viewSharedData.getViewModel().swap.get().visibleProperty());
        skipTurn.visibleProperty().bind(viewSharedData.getViewModel().skipTurn.get().visibleProperty());
    }

    public void setViewSharedData(ViewSharedData viewSharedData) {
        this.viewSharedData = viewSharedData;
    }

    @FXML
    public void Submit() throws IOException {
        int endRow =-1, endCol=-1;

        if(this.indexRow.isEmpty() || this.indexCol.isEmpty()) return;

        startRow = indexRow.get(0);
        startCol = indexCol.get(0);

        for(int i = 0; i < indexRow.size(); i++){
            if(indexRow.get(i) < startRow || indexCol.get(i) < startCol){
                startRow = indexRow.get(i);
                startCol = indexCol.get(i);
            }
        }

        for(int i = 0; i < indexRow.size(); i++){
            if(indexRow.get(i) > endRow || indexCol.get(i) > endCol){
                endRow = indexRow.get(i);
                endCol = indexCol.get(i);
            }
        }

        int wordLength;
        if(vertical) wordLength = (endRow - startRow) + 1;
        else wordLength = (endCol - startCol) + 1;
        System.out.println("Length -> " + wordLength);

        //Building the correct word to send:
        boolean foundNext;
        StringBuilder finalWord = new StringBuilder();
        int currentRow = startRow;
        int currentCol = startCol;
        Set<Integer> foundLetters = new HashSet<>();

        for (int i = 0; i < wordLength; i++) {
            foundNext = false;
            for (int j = 0; j < indexRow.size(); j++) {
                if((currentRow == indexRow.get(j) && currentCol == indexCol.get(j) + 1)
                        || (currentRow == indexRow.get(j) + 1 && currentCol == indexCol.get(j))
                        || (currentRow == indexRow.get(j) && currentCol == indexCol.get(j))){
                    if(foundLetters.contains(j)) continue;
                    finalWord.append(word.charAt(j));
                    foundLetters.add(j);
                    foundNext = true;
                    break;
                }
            }
            if(!foundNext){
                finalWord.append('_');
            }
            if(vertical) currentRow += 1;
            else currentCol += 1;
        }

        word = finalWord.toString();

        System.out.println("Submit");
        System.out.println("Word: " + word + ", Vertical: " + this.vertical);
        System.out.println("Start at index: " + startRow + ", " + startCol);
        System.out.println("End at index: " + endRow + ", " + endCol);

        playerAction.set("submit" + "," + word + "," + startRow + "," + startCol + "," + vertical);
    }
    @FXML
    public void challenge() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Dictionary Challenge");
        alert.setHeaderText(null);
        alert.setContentText("Would you like to challenge the dictionary?");

        Button yesButton = (Button) alert.getDialogPane().lookupButton(javafx.scene.control.ButtonType.OK);
        yesButton.setText("Yes");

        Button noButton = (Button) alert.getDialogPane().lookupButton(javafx.scene.control.ButtonType.CANCEL);
        noButton.setText("No");

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);

        alert.getDialogPane().setExpandableContent(vbox);
        alert.getDialogPane().setExpanded(true);

        alert.showAndWait().ifPresent(response -> {
            if(response == ButtonType.OK){
                playerAction.set("challenge" + "," + word + "," + startRow + "," + startCol + "," + vertical);
                resetWordParameters();
            }
            else{
                viewSharedData.getViewModel().updateViewProperties();
            }
            alert.close();
        });
    }

    @FXML
    public void exit() {
        boolean isHost = viewSharedData.getHost();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit!");
        alert.setHeaderText(null);
        if(isHost) alert.setContentText("You are the host, Closing the game will disconnect all guests!" +
                "\n Are you sure you want to stop the game?");
        else alert.setContentText("Are you sure you want to exit the game?");

        Button exit = (Button) alert.getDialogPane().lookupButton(javafx.scene.control.ButtonType.OK);
        exit.setText("Exit");

        Button stay = (Button) alert.getDialogPane().lookupButton(javafx.scene.control.ButtonType.CANCEL);
        stay.setText("Stay");

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);

        alert.getDialogPane().setExpandableContent(vbox);
        alert.getDialogPane().setExpanded(true);

        alert.showAndWait().ifPresent(response -> {
            if(response == ButtonType.OK){
                if(isHost){
                    closeGame();
                    viewSharedData.getCalculationServer().close();
                }
                else{
                    leaveGame();
                }
            }
            alert.close();
        });
    }

    private void leaveGame() {
        try {
            MySocket socket = new MySocket(new Socket(viewSharedData.getHostIp(), viewSharedData.getHostPort()));
            PrintWriter printWriter = new PrintWriter(socket.getPlayerSocket().getOutputStream(), true);
            printWriter.println("playerDisconnected,"+viewSharedData.getPlayer().playerId.get());
        } catch (IOException ignored) {}
        Platform.exit();
    }

    private void closeGame() {
        try {
            MySocket socket = new MySocket(new Socket(viewSharedData.getHostIp(), viewSharedData.getHostPort()));
            PrintWriter printWriter = new PrintWriter(socket.getPlayerSocket().getOutputStream(), true);
            printWriter.println("serverIsClosing");
        } catch (IOException ignored) {}
    }

    private void resetWordParameters(){
        word = "";
        indexRow.clear();
        indexCol.clear();
    }

    @FXML
    public void SwapTiles() {
        playerAction.set("reset");
        playerAction.set("swapTiles");
    }
    @FXML
    public void SkipTurn() {
        playerAction.set("reset");
        playerAction.set("skipTurn");
    }

    public void squareClickHandler() {
        // Run through all the children of boardGridPane
        for (Node node : boardGridPane.getChildren()) {
            if (node instanceof StackPane cell) {
                Label label = (Label) cell.getChildren().get(0);
                ImageView imageView = new ImageView();

                // Add click event handler to each cell
                cell.setOnMouseClicked(event -> {
                    // Create a TextInputDialog for entering the letter and orientation
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Enter a Letter");
                    dialog.setHeaderText("Enter a letter for the cell");
                    dialog.setContentText("Letter:");

                    // Show the dialog and wait for the user's input
                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(word -> {
                        String letter = word.trim();
                        if (letter.length() != 1 || !Character.isLetter(letter.charAt(0))) {
                            new Alert(Alert.AlertType.ERROR, "Only one letter is allowed.").showAndWait();
                            return;
                        }
                        int index = boardGridPane.getChildren().indexOf(cell);
                        int validationResult = cellValidationCheck(index);
                        switch(validationResult){
                            case -2 -> {
                                new Alert(Alert.AlertType.ERROR, "First word in board must start in middle cell(Starting Point).").showAndWait();
                                return;
                            }
                            case -1 -> {
                                new Alert(Alert.AlertType.ERROR, "You are trying to place a new Tile on already existing one.").showAndWait();
                                return;
                            }
                            case 0 -> {
                                new Alert(Alert.AlertType.ERROR, "Tile placement should be adjacent to already placed tiles.").showAndWait();
                                return;
                            }
                        }
                        // Generate the image path based on the entered letter
                        String imagePath = "/Images/Tiles/" + letter.toUpperCase() + ".png";

                        // Set the background image and remove the background color
                        cell.setId("cell"); // Set an ID for the StackPane
                        //cell.setStyle("-fx-background-color: transparent;"); // Remove the background color

                        // Update the label text
                        label.setVisible(false);

                        // Set the background image using JavaFX
                        String fullPath = Objects.requireNonNull(ViewController.class.getResource(imagePath)).toExternalForm();
                        imageView.setImage(new Image(fullPath));
                        imageView.setPreserveRatio(true);
                        imageView.setFitWidth(cell.getWidth());
                        imageView.setFitHeight(cell.getHeight());
                        cell.getChildren().add(imageView);


                        // Save letter,Index and update if vertical or not.
                        saveLetterAndIndex(letter, index);
                    });
                });
            }
        }
    }

    private int cellValidationCheck(int index){
        int numColumns = GridPane.getColumnIndex(boardGridPane.getChildren().get(index));
        int numRows = GridPane.getRowIndex(boardGridPane.getChildren().get(index));
        Board gameBoard = viewSharedData.getViewModel().getModel().getGameData().getBoard();
        if(gameBoard.getTiles()[7][7] == null && (numRows != 7 || numColumns != 7) && indexRow.isEmpty() && indexCol.isEmpty()) return -2;
        else if(gameBoard.getTiles()[numRows][numColumns] != null) return -1;
        else if(gameBoard.getTiles()[7][7] != null && !adjacentToGameBoardTiles(numRows, numColumns)
                && !adjacentInLocalGameBoard(numRows, numColumns)) return 0;
        return 1;
    }

    private boolean adjacentToGameBoardTiles(int i, int j){
        Board gameBoard = viewSharedData.getViewModel().getModel().getGameData().getBoard();
        return (i-1 > -1 && gameBoard.getTiles()[i-1][j] != null) || (j-1 > -1 && gameBoard.getTiles()[i][j-1] != null)
                || (j+1 < 15 && gameBoard.getTiles()[i][j+1] != null) || (i+1 < 15 && gameBoard.getTiles()[i+1][j] != null);
    }

    private boolean adjacentInLocalGameBoard(int i, int j){
        for(int k = 0; k < indexCol.size(); k++){
            if((indexRow.get(k) == i && indexCol.get(k) == j-1) || (indexRow.get(k) == i && indexCol.get(k) == j+1)
                    || (indexRow.get(k) == i-1 && indexCol.get(k) == j) || (indexRow.get(k) == i+1 && indexCol.get(k) == j)){
                return true;
            }
        }
        return false;
    }

    // Method to save the entered word, its orientation, and retrieve the column and row index
    private void saveLetterAndIndex(String letter, int index) {
        // Retrieve the column and row index based on the StackPane index within the GridPane
        int numColumns = GridPane.getColumnIndex(boardGridPane.getChildren().get(index));
        int numRows = GridPane.getRowIndex(boardGridPane.getChildren().get(index));
        boolean indexFound = false;
        for(int i = 0; i < indexCol.size(); i++){
            if(numRows == indexRow.get(i) && numColumns == indexCol.get(i)){
                word = word.replace(word.charAt(i), letter.charAt(0));
                indexFound = true;
            }
        }
        if(!indexFound) {
            this.word += letter;
            this.indexCol.add(numColumns);
            this.indexRow.add(numRows);
        }
        if(word.length() > 1) vertical = (indexRow.get(0) - indexRow.get(1) < 0);
    }

    @FXML
    public void start() throws IOException{
        loadBoard();
        sendStartToServer();
    }

    //Testing ONLY
    public void sendStartToServer(){
        MySocket initiateServer = null;
        try {
            initiateServer = new MySocket(new Socket(viewSharedData.getHostIp(), viewSharedData.getHostPort()));
            PrintWriter printWriter = new PrintWriter(initiateServer.getPlayerSocket().getOutputStream(),true);
            printWriter.println("start");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(initiateServer != null && initiateServer.getPlayerSocket().isConnected()){
                try {
                    initiateServer.getPlayerSocket().close();
                } catch (IOException ignored) {}
            }
        }
    }

    @FXML
    public void loadScene(String sceneName) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(sceneName + ".fxml")));
        Parent root = loader.load();

        ViewController viewController = null;
        ConnectionController connectionController = null;
        GameController gameController = null;
        EndGameController endGameController = null;
        switch(sceneName){
            case "HomePage" -> viewController = loader.getController();
            case "HostPage", "GuestPage", "StartGame" -> connectionController = loader.getController();
            case "BoardPage" -> gameController = loader.getController();
            case "ScoreBoardPage" -> endGameController = loader.getController();
        }

        if(viewController != null) viewController.setViewSharedData(this.viewSharedData);
        else if(connectionController != null) connectionController.setViewSharedData(this.viewSharedData);
        else if(gameController != null) {
            gameController.setViewSharedData(this.viewSharedData);
            gameController.squareClickHandler();
            gameController.bindAll();
            gameController.initializeHostAction();
            gameController.initializeBoardUpdateAction();
        }
        else if(endGameController != null){
            endGameController.setViewSharedData(this.viewSharedData);
        }

        Stage stage = BookScrabbleApplication.getPrimaryStage();
        Scene scene = null;
        scene = new Scene(root);
        try {
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(sceneName + ".css")).toExternalForm());
        } catch (NullPointerException ignored){}
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void loadBoard() throws IOException{
        loadScene("BoardPage");
    }

    private void loadScoreBoard() throws IOException{
        viewSharedData.getViewModel().getModel().stopGame();
        loadScene("ScoreBoardPage");
    }
}
