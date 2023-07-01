package view;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;
import view.data.ViewSharedData;

import java.io.IOException;
import java.util.Objects;

public class ViewController {
    ViewSharedData viewSharedData;
    public StringProperty wordFromPlayer;
    public StringProperty playerAction;

    /**
     * The ViewController function is the main function of the ViewController class.
     * It creates a new instance of the GameModel class, and then calls its startGame() method.
     * The ViewController also sets up all the GUI elements for this game, including:
     * - A TextField for entering words to guess (wordFromPlayer)
     * - A Label that displays whether a word was guessed correctly (playerAction)
     */
    public ViewController(){
        wordFromPlayer = new SimpleStringProperty();
        playerAction = new SimpleStringProperty();
    }

    /**
     * The setViewSharedData function is used to set the viewSharedData variable.
     * @param viewSharedData viewSharedData Set the viewSharedData variable to the viewSharedData object passed in
     */
    public void setViewSharedData(ViewSharedData viewSharedData) {
        this.viewSharedData = viewSharedData;
    }


    /**
     * The loadScene function is used to load a new scene into the stage.
     * @param sceneName sceneName Load the scene that is passed in
     */
    @FXML
    public void loadScene(String sceneName) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(sceneName + ".fxml")));
        Parent root = loader.load();
        ViewController viewController = null;
        ConnectionController connectionController = null;
        switch(sceneName){
            case "GameModePage" -> connectionController = loader.getController();
            case "Tutorial", "HomePage" -> viewController = loader.getController();
        }
        if(viewController != null) viewController.setViewSharedData(this.viewSharedData);
        else if(connectionController != null) connectionController.setViewSharedData(this.viewSharedData);

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
     * The StartTutorial function is called when the user clicks on the &quot;Tutorial&quot; button.
     * It loads a new scene, which displays instructions for how to play the game.
     */
    @FXML
    public void StartTutorial() throws IOException{
        loadScene("Tutorial");
    }

    /**
     * The ChooseGameMode function is a function that allows the user to choose between
     * playing in single player mode or multiplayer mode. It does this by loading the GameModePage scene.
     */
    @FXML
    public void ChooseGameMode() throws IOException{
        loadScene("GameModePage");
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
     * The Exit function is a function that exits the program.
     * @param event event Get the source of the action
     */
    @FXML
    public void Exit(ActionEvent event) throws IOException{
        Platform.exit();
    }

    /**
     * The Back function is used to return the user to the ChooseGameMode screen.
     * @param event event Get the source of the action
     */
    @FXML
    public void Back(ActionEvent event) throws IOException{
        ChooseGameMode();
    }

}