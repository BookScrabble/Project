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
import model.logic.host.GameManager;
import view.data.ViewSharedData;
import viewModel.ViewModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
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
        System.out.println("ViewSharedData in Connection controller -> " + this.viewSharedData);
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
            connectToServer();
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
            connectToServer();
            loadBoard(event);
        }
    }

    public void connectToServer(){
        try {
            Socket pingCheck = new Socket(ipField.getText(), Integer.parseInt(portField.getText()));
            PrintWriter printWriter = new PrintWriter(pingCheck.getOutputStream(), true);
            printWriter.println("ping");
            ObjectInputStream objectInputStream = new ObjectInputStream(pingCheck.getInputStream());
            try {
                GameManager model = (GameManager) objectInputStream.readObject();
                model.addObserver(viewSharedData.getViewModel());
                this.viewSharedData.getViewModel().setModel(model);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            pingCheck.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        Client playerClient = new Client(ipField.getText(), Integer.parseInt(portField.getText()), nameField.getText());
        this.viewSharedData.setPlayer(playerClient);
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
    public void loadScene(ActionEvent event, String sceneName) throws IOException {
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
        else if(gameController != null) gameController.setViewSharedData(this.viewSharedData);

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

    @FXML
    public void Back(ActionEvent event) throws IOException{
        StartGame(event);
    }

    @FXML
    public void StartGame(ActionEvent event) throws IOException{
        loadScene(event, "StartGame");
    }

    @FXML
    public void loadBoard(ActionEvent event) throws IOException{
        loadScene(event, "BoardPage");
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
}
