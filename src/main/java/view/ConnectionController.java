package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
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
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
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
    @FXML
    private ChoiceBox<String> dictionaryChoice;
    @FXML
    Label dictionaryChoiceLabelError;

    private Map<String, String> dictionaries;

    ViewSharedData viewSharedData;

    String chosenDictionary;

    /**
     * The ConnectionController function is the controller for the ConnectionView.fxml file, which allows
     * users to choose a dictionary from a list of dictionaries and connect to it. The function also
     * contains an event handler that handles when the user clicks on &quot;Connect&quot; in order to connect them
     * with their chosen dictionary. It also has an event handler that handles when the user clicks on
     * &quot;Cancel&quot; in order to cancel out of this view and return back to MainMenuView.fxml without connecting
     * with any dictionary at all (i.e., they will not be able to play any games). This function
     */
    public ConnectionController(){
        chosenDictionary = "";
        dictionaryChoice = new ChoiceBox<>();
    }

    /**
     * The setChoiceBoxOptions function is a helper function that sets the options for the choice box.
     * It adds all the keys from dictionaries to dictionaryChoice, and then it sets an onAction event handler
     * for when a user selects an option in dictionaryChoice. When this happens, getChoice is called with
     * ActionEvent e as its parameter. This allows us to use getChoice as our event handler method for when
     * a user selects an option in dictionaryChoice.
     */
    private void setChoiceBoxOptions() {
        dictionaryChoice.getItems().addAll(dictionaries.keySet());
        dictionaryChoice.setOnAction(this::getChoice);
    }

    /**
     * The initializeDictionaryOptions function initializes the dictionary options for the user to choose from.
     */
    private void initializeDictionaryOptions() {
        dictionaries = new HashMap<>();
        dictionaries.put("Alice in wonderland", "alice_in_wonderland.txt");
        dictionaries.put("Frank Herbert - Dune", "Frank Herbert - Dune.txt");
        dictionaries.put("Harry Potter", "Harry Potter.txt");
        dictionaries.put("Mobydick", "mobydick.txt");
        dictionaries.put("PG10", "pg10.txt");
        dictionaries.put("Shakespeare", "shakespeare.txt");
        dictionaries.put("The Matrix", "The Matrix.txt");
    }

    /**
     * The getChoice function is called when the user clicks on the &quot;Choose&quot; button.
     * It sets the chosenDictionary variable to be equal to whatever dictionary was selected by
     * the user in the drop-down menu. This function is necessary because it allows us to use
     * a single event handler for all of our buttons, and then we can just check which button was pressed
     * using an if statement inside of that event handler. The getChoice function also calls another method,
     * setUpDictionary(), which will create a new Dictionary object based on what dictionary was chosen by
     * calling one of three different
     * @param event event Get the source of the event
     */
    private void getChoice(ActionEvent event){
        chosenDictionary = dictionaryChoice.getValue();
    }

    /**
     * The setViewSharedData function is used to set the viewSharedData variable.
     * @param viewSharedData viewSharedData Set the viewSharedData variable to the value of the parameter
     */
    public void setViewSharedData(ViewSharedData viewSharedData) {
        this.viewSharedData = viewSharedData;
    }


    /**
     * The StartAsHost function is called when the user clicks on the &quot;Start as Host&quot; button.
     * It checks if all the fields are valid, and if they are it initializes a host server with
     * the port number that was entered by the user. It then connects to this server and loads up
     * a waiting room for other players to join in on. If any of these fields were invalid, an error message will be displayed next to them.
     */
    @FXML
    public void StartAsHost() throws IOException {
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
        if(chosenDictionary.equals("")){
            dictionaryChoiceLabelError.setVisible(true);
            allValid = false;
        }
        else{
            dictionaryChoiceLabelError.setVisible(false);
        }
        if(allValid){
            GameManager gameManager = GameManager.get();
            ipField = new TextField();
            ipField.setText("localhost"); //Default ip to make server run locally on host computer.
            gameManager.initializeHostServer(Integer.parseInt(portField.getText()));
            //gameManager.getGameData().setDictionaries("alice_in_wonderland.txt", "Frank Herbert - Dune.txt", "Harry Potter.txt");
            gameManager.getGameData().setDictionaries(dictionaries.get(chosenDictionary));
            checkOrCreateCalculationServer();
            connectToServer();
            viewSharedData.setHost(true);
            loadWaitingHostRoom();
        }
    }

    /**
     * The checkOrCreateCalculationServer function checks if the calculation server is running.
     * If it isn't, then it creates a new calculation server and starts it.
     */
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
            MyServer calculationServer = new MyServer(10000, new BookScrabbleHandler());
            calculationServer.start();
            viewSharedData.setCalculationServer(calculationServer);
        }
    }

    /**
     * The StartAsGuest function is called when the user clicks on the &quot;Start as Guest&quot; button.
     * It checks if all the fields are valid, and if they are it connects to a server and loads
     * a new scene where it waits for a host to start the game. If any of them aren't valid, then an error message appears next to that field.
     */
    @FXML
    public void StartAsGuest() throws IOException{
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

    /**
     * The connectToServer function is called when the user clicks on the &quot;Connect&quot; button.
     * It takes in a String ip, an int port, and a String name from their respective text fields.
     * It then creates a new Client object with those parameters and sets it as the playerClient in viewSharedData.
     * Then it creates a new GameModelReceiver object with those parameters and sets that as gameModelReceiver in viewSharedData.
     */
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

    /**
     * The validPort function checks to see if the port number is valid.
     * @param port port Check if the port number is valid
     * @return A boolean value
     */
    public boolean validPort(String port){
        return port.matches("(1000[1-9]|100[1-9]\\d|10[1-9]\\d{2}|1[1-9]\\d{3}|19999)");
    }

    /**
     * The validIp function takes a string as an argument and returns true if the string is a valid IP address.
     * @param ip ip Check if the ip address is valid
     * @return True if the input string is a valid ip address
     */
    public boolean validIp(String ip){
        return ip.matches("(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}") || ip.equals("localhost");
    }

    /**
     * The validName function checks to see if the name entered by the user is valid.
     * @param name name Check if the name is valid
     * @return True if the name parameter is a valid name
     */
    public boolean validName(String name){
        return name.matches("^[A-Za-z]+$");
    }

    /**
     * The loadScene function is used to load a new scene into the stage.
     * It takes in a string that represents the name of the scene to be loaded, and then loads it.
     * The function also initializes all controllers for each page, and sets their viewSharedData variable to this class's viewSharedData variable.
     * @param sceneName sceneName Load the corresponding fxml file
     * @return A boolean
     */
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
        else if(connectionController != null) {
            connectionController.initializeDictionaryOptions();
            connectionController.setChoiceBoxOptions();
            connectionController.setViewSharedData(this.viewSharedData);
        }
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

    /**
     * The Back function is used to return the user to the main menu.
     */
    @FXML
    public void Back() throws IOException{
        StartGame();
    }

    /**
     * The StartGame function is called when the user clicks on the &quot;Start Game&quot; button.
     * It loads a new scene, which is defined in StartGame.fxml and controlled by StartGameController.
     */
    @FXML
    public void StartGame() throws IOException{
        loadScene("StartGame");
    }

    /**
     * The loadHostForm function is called when the user clicks on the &quot;Host&quot; button.
     * It loads a new scene, which is the HostPage.fxml file, and sets it as the current scene.
     */
    @FXML
    public void loadHostForm() throws IOException{
        loadScene("HostPage");
    }

    /**
     * The loadHomePage function is used to load the HomePage.fxml file into the main window of
     * our application. This function is called when a user clicks on the &quot;Home&quot; button in our
     * application's menu bar, and it will display all the information that we have stored about
     * each student in our database. It also allows us to add new students, edit existing students'
     * information, or delete any student from our database entirely.
     */
    @FXML
    public void loadHomePage() throws IOException{
        loadScene("HomePage");
    }

    /**
     * The loadGuestForm function is used to load the GuestPage.fxml file, which contains
     * the GUI for a guest user. This function is called when a guest user clicks on the &quot;Guest&quot; button
     * in the LoginPage.fxml file, and it loads up all of their information into an object that can be passed around
     * throughout this program's various GUIs and controllers so that they can access their data at any time without having to re-enter it again.
     */
    @FXML
    public void loadGuestForm() throws IOException{
        loadScene("GuestPage");
    }

    /**
     * The loadWaitingHostRoom function loads the WaitingHostRoom scene.
     */
    @FXML
    public void loadWaitingHostRoom() throws IOException{
        loadScene("WaitingHostRoom");
    }

}
