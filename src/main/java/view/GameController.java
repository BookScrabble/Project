package view;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
    Button startGame;

    StringProperty clientAction;

    //Properties:


    public GameController(){
        this.word = "";
        this.vertical = false;
        this.flag = 0;
        this.indexRow = new ArrayList<>();
        this.indexCol = new ArrayList<>();
        this.startGame = new Button();
        initiatePlayersName();
        initiatePlayersScore();
    }

    public void initiatePlayersName(){
        firstPlayerName = new Label();
        secondPlayerName = new Label();
        thirdPlayerName = new Label();
        fourthPlayerName = new Label();
    }

    public void initiatePlayersScore(){
        firstPlayerScore = new Label();
        secondPlayerScore = new Label();
        thirdPlayerScore = new Label();
        fourthPlayerScore = new Label();
    }

    public void toggleStartButton(){
        startGame.setVisible(true);
    }

    public void initializePlayerAction(){
        clientAction = new SimpleStringProperty();
        clientAction.bind(viewSharedData.getPlayer().getAction());
        System.out.println(clientAction);
        viewSharedData.getPlayer().getAction().addListener(((observable, oldAction, newAction) -> {
            handleClientAction(newAction);
        }));
    }

    public void handleClientAction(String action){
        Platform.runLater(() -> {
            switch(action){
                case "loadBoard" -> {
                    try {loadBoard();} catch (IOException ignored) {}
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
    }

    public void setViewSharedData(ViewSharedData viewSharedData) {
        this.viewSharedData = viewSharedData;
    }

    @FXML
    public void Submit() throws IOException {
        while(this.flag == 0){
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Choose Vertical or Horizontal");
            dialog.setHeaderText("Enter Vertical yes/no");
            dialog.setContentText("Vertical:");
            Optional<String> result = dialog.showAndWait();
            if(result.get().equals("yes")){
                this.vertical=true;
                this.flag=1;
                break;
            }else if (result.get().equals("no")){
                this.vertical=false;
                this.flag=0;
                break;
            }
        }
        int startRow =-1, startCol=-1;
        int endRow =-1, endCol=-1;
        if(this.vertical){
            startRow = this.indexRow.get(0);
            startCol = this.indexCol.get(0);
            endRow = this.indexRow.get(this.indexRow.size()-1);
            endCol = this.indexCol.get(this.indexCol.size()-1);
        }
        else{
            startRow = this.indexRow.get(0);
            startCol = this.indexCol.get(0);
            endRow = this.indexRow.get(this.indexRow.size()-1);
            endCol = this.indexCol.get(this.indexCol.size()-1);
        }
        System.out.println("Submit");
        System.out.println("Word: " + this.word + ", Vertical: " + this.vertical);
        System.out.println("Start at index: " + startRow + ", " + startCol);
        System.out.println("End at index: " + endRow + ", " + endCol);
        this.indexRow.clear();
        this.indexCol.clear();
        this.word = "";
        this.flag = 0;
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
            if (node instanceof StackPane) {
                StackPane cell = (StackPane) node;
                Label label = (Label) cell.getChildren().get(0);
                ImageView imageView = new ImageView();

                // Add click event handler to each cell
                cell.setOnMouseClicked(event -> {
                    // Create a TextInputDialog for entering the letter and orientation
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Enter a Letter");
                    dialog.setHeaderText("Enter a letter for the cell");
                    dialog.setContentText("Letter:");
                    TextField orientationField = new TextField();

                    // Show the dialog and wait for the user's input
                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(word -> {
                        String letter = word.trim();
                        String orientation = orientationField.getText().trim().toLowerCase();
                        this.vertical = orientation.equals("yes");
                        // TODO - implement the vertical logic.
                        // Validate the entered letter
                        if (letter.length() != 1 || !Character.isLetter(letter.charAt(0))) {
                            new Alert(Alert.AlertType.ERROR, "Only one letter is allowed.").showAndWait();
                            return;
                        }

                        // Generate the image path based on the entered letter
                        String imagePath = "/Images/Tiles/" + letter.toUpperCase() + ".png";

                        // Set the background image and remove the background color
                        cell.setId("cell"); // Set an ID for the StackPane
                        cell.setStyle("-fx-background-color: transparent;"); // Remove the background color

                        // Update the label text
                        label.setText(letter.toUpperCase());
                        label.setVisible(false);

                        // Set the background image using JavaFX
                        String fullPath = Objects.requireNonNull(ViewController.class.getResource(imagePath)).toExternalForm();
                        imageView.setImage(new Image(fullPath));
                        imageView.setPreserveRatio(true);
                        imageView.setFitWidth(cell.getWidth());
                        imageView.setFitHeight(cell.getHeight()+ 3);
                        cell.getChildren().add(imageView);

                        // Save the word, its orientation, and the index
                        int index = boardGridPane.getChildren().indexOf(cell);
                        saveWordWithOrientationAndIndex(letter, orientation, index);
                    });
                });
            }
        }
    }

    // Method to save the entered word, its orientation, and retrieve the column and row index
    private void saveWordWithOrientationAndIndex(String word, String orientation, int index) {
        // Retrieve the column and row index based on the StackPane index within the GridPane
        int numColumns = GridPane.getColumnIndex(boardGridPane.getChildren().get(index));
        int numRows = GridPane.getRowIndex(boardGridPane.getChildren().get(index));
        // Implement your saving logic here
        this.word += word;
        this.indexCol.add(numColumns);
        this.indexRow.add(numRows);
    }

    @FXML
    public void start(ActionEvent event) throws IOException{
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
            gameController.initializePlayerAction();
        }

        Stage stage = BookScrabbleApplication.getPrimaryStage();
        Scene scene = null;
        if(Objects.equals(sceneName, "BoardPage")){
            scene = new Scene(root,1400,1000);
        } else{
            scene = new Scene(root);
        }
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
