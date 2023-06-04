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

    public void setNameLabel(ActionEvent event){
        String name = nameField.getText();
        String port = portField.getText();
        String ip = ipField.getText();
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
        Parent root = FXMLLoader.load(getClass().getResource("HostPage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("HostPage.css").toExternalForm());
        stage.show();
    }

    @FXML
    public void loadGuestForm(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("GuestPage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("GuestPage.css").toExternalForm());
        stage.show();
    }

    @FXML
    public void loadBoard(ActionEvent event) throws IOException{
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