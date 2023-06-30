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

    public ViewController(){
        wordFromPlayer = new SimpleStringProperty();
        playerAction = new SimpleStringProperty();
    }

    /**
     * Sets the shared data for the view.
     *
     * @param viewSharedData The shared data object.
     */
    public void setViewSharedData(ViewSharedData viewSharedData) {
        this.viewSharedData = viewSharedData;
    }


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

    @FXML
    public void StartTutorial() throws IOException{
        loadScene("Tutorial");
    }

    @FXML
    public void ChooseGameMode() throws IOException{
        loadScene("GameModePage");
    }

    @FXML
    public void loadHomePage() throws IOException{
        loadScene("HomePage");
    }

    @FXML
    public void Exit(ActionEvent event) throws IOException{
        Platform.exit();
    }

    @FXML
    public void Back(ActionEvent event) throws IOException{
        ChooseGameMode();
    }

}