package view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloController {
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


//    public void squareClickHandler(){
//        // run all over the boardGridPane and add a click handler to each square
//        for(int i = 0; i < boardGridPane.getChildren().size(); i++){
//            for(int j = 0; j < boardGridPane.getChildren().size(); j++){
//                boardGridPane.getChildren().get(i).setOnMouseClicked(event -> {
//                    ((Label)((StackPane)event.getSource()).getChildren().get(0)).setText("x");
//                });
//            }
//        }
//    }

public void squareClickHandler() {
    // Run through all the children of boardGridPane
    for (Node node : boardGridPane.getChildren()) {
        if (node instanceof StackPane) {
            StackPane cell = (StackPane) node;
            Label label = (Label) cell.getChildren().get(0);
            TextField textField = new TextField();

            // Add click and key event handler to each cell
            cell.setOnMouseClicked(event -> {
                // Show the text field to capture input
                cell.getChildren().add(textField);
                textField.requestFocus();
            });

            textField.setOnKeyTyped(event -> {
                // Retrieve the typed character
                String typedCharacter = event.getCharacter();

                // Generate the image path based on the typed character
                String imagePath = "./resources/Images/Tiles/" + typedCharacter + ".png";

                // Set the background image and remove the background color
                cell.setId("cell"); // Set an ID for the StackPane
                cell.setStyle("-fx-background-color: transparent;"); // Remove the background color

                // Update the label text
                label.setText(typedCharacter);
                label.setVisible(false);
                // Remove the text field from the cell
                cell.getChildren().remove(textField);

                // Set the background image using CSS
                cell.getStyleClass().add("cell-background");
            });
        }
    }
}

    @FXML
    public void Submit(ActionEvent event) throws IOException{
        System.out.println("Submit");
        squareClickHandler();
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
        stage.show();
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