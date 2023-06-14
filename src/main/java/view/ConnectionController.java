package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.logic.client.Client;

import java.io.IOException;
import java.util.Objects;

public class ConnectionController {

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

    public ConnectionController(){

    }

    @FXML
    public void StartAsHost(ActionEvent event) throws IOException {
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

    public boolean validPort(String port){
        return port.matches("(1000[1-9]|100[1-9]\\d|10[1-9]\\d{2}|1[1-9]\\d{3}|19999)");
    }

    public boolean validIp(String ip){
        return ip.matches("(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}");
    }

    public boolean validName(String name){
        return name.matches("^[A-Za-z]+$");
    }

    @FXML
    public void loadBoard(ActionEvent event) throws IOException{
        loadScene(event, "BoardPage");
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
}
