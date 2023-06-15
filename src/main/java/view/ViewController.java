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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.logic.host.GameManager;
import viewModel.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class ViewController {
    @FXML
    private Label nameLabelError;
    @FXML
    private TextField nameField;
    @FXML
    private Label portLabelError;
    @FXML
    private TextField portField;
    @FXML
    private Label ipLabelError;
    @FXML
    private TextField ipField;
    @FXML
    private GridPane boardGridPane;
    String word = "";
    boolean vertical;
    int flag = 0;
    ArrayList<Integer> indexRow = new ArrayList<>();
    ArrayList<Integer> indexCol = new ArrayList<>();

    //------------------New Params-------------------------//
    private GameController gameController; //Later move all game functionality their to make code cleaner.
    private ConnectionController connectionController; //Later move all connection functionality their to make code cleaner.
    ViewModel viewModel;

    public StringProperty wordFromPlayer;
    public StringProperty playerAction;

    public ViewController(){
        this.gameController = new GameController();
        this.connectionController = new ConnectionController();
        wordFromPlayer = new SimpleStringProperty();
        playerAction = new SimpleStringProperty();
    }

    public void setViewModel(ViewModel viewModel){
        this.viewModel = viewModel;
        viewModel.wordFromPlayer.bind(wordFromPlayer);
        viewModel.playerAction.bind(playerAction);
        gameController.setViewModel(viewModel);
        connectionController.setViewModel(viewModel);
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
                        String fullPath = ViewController.class.getResource(imagePath).toExternalForm();
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
        squareClickHandler();
    }

    @FXML
    public void Submit(ActionEvent event) throws IOException{
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


    @FXML
    public void StartTutorial(ActionEvent event) throws IOException{
        loadScene(event, "Tutorial");
    }

    @FXML
    public void StartGame(ActionEvent event) throws IOException{
        loadScene(event, "StartGame");
    }

    @FXML
    public void StartAsHost(ActionEvent event) throws IOException{
        boolean allValid = true;
        if(!validName(nameField.getText())){
            nameLabelError.setVisible(true);
            allValid = false;
        } else{
            nameLabelError.setVisible(false);
        }
        if(!validPort(portField.getText())){
            portLabelError.setVisible(true);
            allValid = false;
        } else{
            portLabelError.setVisible(false);
        }
        if(allValid){
            loadBoard(event);
        }
    }

    @FXML
    public void StartAsGuest(ActionEvent event) throws IOException{
        boolean allValid = true;
        if(!validName(nameField.getText())){
            nameLabelError.setVisible(true);
            allValid = false;
        } else{
            nameLabelError.setVisible(false);
        }
        if(!validPort(portField.getText())){
            portLabelError.setVisible(true);
            allValid = false;
        } else{
            portLabelError.setVisible(false);
        }
        if(!validIp(ipField.getText())){
            ipLabelError.setVisible(true);
            allValid = false;
        } else{
            ipLabelError.setVisible(false);
        }

        if(allValid){
            loadBoard(event);
        }

    }

    @FXML
    public void loadHostForm(ActionEvent event) throws IOException{
        loadScene(event, "HostPage");
    }

    @FXML
    public void loadHomePage(ActionEvent event) throws IOException{
        loadScene(event, "HomePage");
    }

    @FXML
    public void loadGuestForm(ActionEvent event) throws IOException{
        loadScene(event, "GuestPage");
    }

    @FXML
    public void loadBoard(ActionEvent event) throws IOException{
        loadScene(event, "BoardPage");
    }

    @FXML
    public void Exit(ActionEvent event) throws IOException{
        Platform.exit();
    }

    @FXML
    public void Back(ActionEvent event) throws IOException{
        StartGame(event);
    }

    @FXML
    public void BackToHomePage(ActionEvent event) throws IOException{
        loadHomePage(event);
    }

    @FXML
    public void loadScene(ActionEvent event, String sceneName) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(sceneName + ".fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
        //stage.show();
    }

    public boolean validPort(String port){
        return port.matches("(1000[1-9]|100[1-9]\\d|10[1-9]\\d{2}|1[1-9]\\d{3}|19999)");
    }

    public boolean validIp(String ip){
        return ip.matches("(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}");
    }

    public boolean validName(String name){
        return name.matches("^[A-Za-z]+$");
    }
}