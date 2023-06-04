package view;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label nameLabel;
    @FXML
    private TextField nameField;
    @FXML
    private Label portLabel;
    @FXML
    private TextField portField;

    public void setNameLabel(ActionEvent event){
        String name = nameField.getText();
        nameLabel.setText("Hello : " + name);
        String port = portField.getText();
        portLabel.setText("Port : " + port);
    }

    @FXML
    public void StartTutorial(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("Tutorial.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("Tutorial.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void StartGame(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("StartGame.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("StartGame.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void StartAsHost(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("HostPage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("HostPage.css").toExternalForm());
        stage.show();
    }

    @FXML
    public void StartAsGuest(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("GuestPage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("GuestPage.css").toExternalForm());
        stage.show();
    }

    @FXML
    public void StartBoard(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("BoardPage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void Exit(ActionEvent event) throws IOException{
        Platform.exit();
    }


}