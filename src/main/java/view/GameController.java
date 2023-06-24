package view;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.logic.host.MySocket;
import model.logic.host.data.Board;
import view.data.ViewSharedData;
import viewModel.ViewModel;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class GameController {
    String word;
    boolean vertical;
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
    Button resign;
    @FXML
    Button skipTurn;
    @FXML
    Button challenge;
    @FXML
    Button sort;
    @FXML
    Button swap;
    @FXML
    Button submit;

    StringProperty imagePath;


    @FXML
    Button startGame;

    StringProperty playerAction;

    StringProperty messageFromHost;

    //Properties:


    public GameController(){
        this.word = "";
        this.vertical = false;
        this.flag = 0;
        this.indexRow = new ArrayList<>();
        this.indexCol = new ArrayList<>();
        this.startGame = new Button();
        playerAction = new SimpleStringProperty();
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
        resign = new Button();
        challenge = new Button();
        swap = new Button();
        sort = new Button();
        submit = new Button();
        skipTurn = new Button();
    }

    public void toggleStartButton(){
        startGame.setVisible(true);
    }

    public void initializeHostAction(){
        messageFromHost = new SimpleStringProperty();
        messageFromHost.bind(viewSharedData.getPlayer().getMessageFromHost());
        viewSharedData.getPlayer().getMessageFromHost().addListener(((observable, oldAction, newAction) -> {
            handleHostAction(newAction);
        }));
    }

    public void initializeBoardUpdateAction(){
        imagePath = new SimpleStringProperty();
        imagePath.bind(viewSharedData.getViewModel().imagePath);
        viewSharedData.getViewModel().imagePath.addListener(((observable, oldAction, newAction) -> {
            updateBoardCellImage(newAction);
        }));
    }

    public void updateBoardCellImage(String newAction){
        String[] trimmed = newAction.split(",");
        int index = Integer.parseInt(trimmed[0]);
        String imageURL = trimmed[1];
        StackPane cell = (StackPane)boardGridPane.getChildren().get(index);
        ImageView imageView = (ImageView)cell.getChildren().get(1);
        imageView.setImage(new Image(imageURL));
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
            }
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
        resign.visibleProperty().bind(viewSharedData.getViewModel().resign.get().visibleProperty());
        challenge.visibleProperty().bind(viewSharedData.getViewModel().challenge.get().visibleProperty());
        submit.visibleProperty().bind(viewSharedData.getViewModel().submit.get().visibleProperty());
        sort.visibleProperty().bind(viewSharedData.getViewModel().sort.get().visibleProperty());
        swap.visibleProperty().bind(viewSharedData.getViewModel().swap.get().visibleProperty());
        skipTurn.visibleProperty().bind(viewSharedData.getViewModel().skipTurn.get().visibleProperty());
    }

    public void setViewSharedData(ViewSharedData viewSharedData) {
        this.viewSharedData = viewSharedData;
    }

    @FXML
    public void Submit() throws IOException {
        int startRow =-1, startCol=-1;
        int endRow =-1, endCol=-1;

        if(this.indexRow.isEmpty() || this.indexCol.isEmpty()) return;

        startRow = this.indexRow.get(0);
        startCol = this.indexCol.get(0);
        endRow = this.indexRow.get(this.indexRow.size()-1);
        endCol = this.indexCol.get(this.indexCol.size()-1);

        System.out.println("Submit");
        System.out.println("Word: " + this.word + ", Vertical: " + this.vertical);
        System.out.println("Start at index: " + startRow + ", " + startCol);
        System.out.println("End at index: " + endRow + ", " + endCol);

        playerAction.set("submit" + "," + word + "," + startRow + "," + startCol + "," + vertical);

        this.indexRow.clear();
        this.indexCol.clear();
        this.word = "";
    }
    @FXML
    public void Challenge(ActionEvent event) throws IOException{
        System.out.println("Challenge");
    }
    @FXML
    public void SwapTiles(ActionEvent event) throws IOException{
        System.out.println("SwapTiles");
    }
    @FXML
    public void SortTiles(ActionEvent event) throws IOException{
        System.out.println("Sort");
    }
    @FXML
    public void SkipTurn(ActionEvent event) throws IOException{
        System.out.println("SkipTurn");
    }
    @FXML
    public void Resign(ActionEvent event) throws IOException{
        System.out.println("Resign");
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
                        if(word.length() > 1) vertical = (indexRow.get(0) - indexRow.get(1) < 0);
                        switch(validationResult){
                            case -2 -> {
                                new Alert(Alert.AlertType.ERROR, "First word in board must start in middle cell(Starting Point).").showAndWait();
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
                        label.setText(letter.toUpperCase());
                        label.setVisible(false);

                        // Set the background image using JavaFX
                        String fullPath = Objects.requireNonNull(ViewController.class.getResource(imagePath)).toExternalForm();
                        //imageView.setImage(new Image(fullPath));
                        imageView.setImage(new Image(fullPath));
                        imageView.setPreserveRatio(true);
                        imageView.setFitWidth(cell.getWidth());
                        imageView.setFitHeight(cell.getHeight());
                        cell.getChildren().add(imageView);


                        // Save word and index
                        saveWordAndIndex(letter, index);
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
        //TODO - Implement all validations for every tile placement.
        return 1;
    }

    // Method to save the entered word, its orientation, and retrieve the column and row index
    private void saveWordAndIndex(String word, int index) {
        // Retrieve the column and row index based on the StackPane index within the GridPane
        int numColumns = GridPane.getColumnIndex(boardGridPane.getChildren().get(index));
        int numRows = GridPane.getRowIndex(boardGridPane.getChildren().get(index));
        // Implement your saving logic here
        this.word += word;
        this.indexCol.add(numColumns);
        this.indexRow.add(numRows);
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
        switch(sceneName){
            case "HomePage" -> viewController = loader.getController();
            case "HostPage", "GuestPage", "StartGame" -> connectionController = loader.getController();
            case "BoardPage" -> gameController = loader.getController();
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

    @FXML
    public void Exit(ActionEvent event) throws IOException{
        Platform.exit();
    }
}
