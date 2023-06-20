package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.logic.client.Client;
import model.logic.host.GameManager;
import model.logic.server.MyServer;
import model.logic.server.dictionary.BookScrabbleHandler;
import view.data.GameModelReceiver;
import view.data.ViewSharedData;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
    ViewSharedData viewSharedData;

    public ConnectionController(){

    }

    public void setViewSharedData(ViewSharedData viewSharedData) {
        this.viewSharedData = viewSharedData;
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
            GameManager gameManager = GameManager.get();
            ipField = new TextField();
            ipField.setText("localhost"); //Default ip to make server run locally on host computer.
            gameManager.initializeHostServer(Integer.parseInt(portField.getText()));
            gameManager.getGameData().setDictionaries("alice_in_wonderland.txt", "Frank Herbert - Dune.txt", "Harry Potter.txt");
            connectToServer();
            viewSharedData.setHost(true);
            loadWaitingHostRoom();
            checkOrCreateCalculationServer();
        }
    }

    public void checkOrCreateCalculationServer(){
        boolean connectionEstablished = false;
        try {
            Socket checkConnection = new Socket("localhost", 10000);
            PrintWriter printWriter = new PrintWriter(checkConnection.getOutputStream(),true);
            printWriter.println("connectionCheck");
            connectionEstablished = true;
            checkConnection.close();
        } catch (IOException ignored) {}
        if(!connectionEstablished){
            viewSharedData.setCalculationServer(new MyServer(10000,new BookScrabbleHandler()));
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
            connectToServer();
            viewSharedData.setHost(false);
            loadWaitingHostRoom();
        }
    }

    public void connectToServer(){
        String ip = ipField.getText();
        int port = Integer.parseInt(portField.getText());
        String name = nameField.getText();

        Client playerClient = new Client(ip, port, name);
        GameModelReceiver playerGameModelReceiver = new GameModelReceiver(ip, port);
        this.viewSharedData.setPlayer(playerClient);
        this.viewSharedData.setGameModelReceiver(playerGameModelReceiver);
        this.viewSharedData.setHostIp(ip);
        this.viewSharedData.setHostPort(port);
        this.viewSharedData.setPlayerName(name);
    }

    public boolean validPort(String port){
        return port.matches("(1000[1-9]|100[1-9]\\d|10[1-9]\\d{2}|1[1-9]\\d{3}|19999)");
    }

    public boolean validIp(String ip){
        return ip.matches("(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}") || ip.equals("localhost");
    }

    public boolean validName(String name){
        return name.matches("^[A-Za-z]+$");
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
            case "WaitingHostRoom" -> gameController = loader.getController();
        }

        if(viewController != null) viewController.setViewSharedData(this.viewSharedData);
        else if(connectionController != null) connectionController.setViewSharedData(this.viewSharedData);
        else if(gameController != null) {
            gameController.setViewSharedData(this.viewSharedData);
            gameController.bindAll();
            gameController.initializeHostAction();
            if(viewSharedData.getHost()) gameController.toggleStartButton();
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
    public void Back(ActionEvent event) throws IOException{
        StartGame();
    }

    @FXML
    public void StartGame() throws IOException{
        loadScene("StartGame");
    }

    @FXML
    public void loadHostForm() throws IOException{
        loadScene("HostPage");
    }

    @FXML
    public void loadHomePage() throws IOException{
        loadScene("HomePage");
    }

    @FXML
    public void loadGuestForm() throws IOException{
        loadScene("GuestPage");
    }

    @FXML
    public void loadWaitingHostRoom() throws IOException{
        loadScene("WaitingHostRoom");
    }

}
