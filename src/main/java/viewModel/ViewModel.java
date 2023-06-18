package viewModel;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.logic.host.GameManager;
import model.logic.host.data.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViewModel{ // Later implement ViewModelFacade
    //Properties for view:...
    public StringProperty wordFromPlayer;
    public StringProperty playerAction;

    public StringProperty firstPlayerName;
    public StringProperty secondPlayerName;
    public StringProperty thirdPlayerName;
    public StringProperty fourthPlayerName;

    public List<StringProperty> playersNames;



    //Other parameters:...
    GameManager model; //Game status



    public ViewModel(){
        playersNames = new ArrayList<>();

        wordFromPlayer = new SimpleStringProperty();
        playerAction = new SimpleStringProperty();
        firstPlayerName = new SimpleStringProperty();
        secondPlayerName = new SimpleStringProperty();
        thirdPlayerName = new SimpleStringProperty();
        fourthPlayerName = new SimpleStringProperty();
        playersNames.add(firstPlayerName);
        playersNames.add(secondPlayerName);
        playersNames.add(thirdPlayerName);
        playersNames.add(fourthPlayerName);
        model = null;
    }

    public void setModel(GameManager model) {
        this.model = model;
        updateViewProperties();
    }

    public void updateViewProperties(){
        Platform.runLater(() -> {
            updatePlayerNames();
            updatePlayerScores();
        });
    }

    public void updatePlayerNames(){
        for(int playerId : model.getGameData().getAllPlayers().keySet().stream().sorted().toList()){
            playersNames.get(playerId-1).setValue(model.getGameData().getPlayer(playerId).getName());
        }
    }

    public void updatePlayerScores(){

    }



    public GameManager getModel() {
        return model;
    }
}
