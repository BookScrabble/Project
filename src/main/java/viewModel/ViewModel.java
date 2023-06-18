package viewModel;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.scene.image.Image;
import model.logic.host.GameManager;
import model.logic.host.data.Player;
import model.logic.host.data.Tile;
import view.ViewController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ViewModel{ // Later implement ViewModelFacade
    //Properties for view:...
    public StringProperty wordFromPlayer;
    public StringProperty playerAction;

    public IntegerProperty playerId;

    public StringProperty firstPlayerName;
    public StringProperty secondPlayerName;
    public StringProperty thirdPlayerName;
    public StringProperty fourthPlayerName;

    public IntegerProperty firstPlayerScore;
    public IntegerProperty secondPlayerScore;
    public IntegerProperty thirdPlayerScore;
    public IntegerProperty fourthPlayerScore;

    public ObjectProperty<Image> firstTile;
    public ObjectProperty<Image> secondTile;
    public ObjectProperty<Image> thirdTile;
    public ObjectProperty<Image> fourthTile;
    public ObjectProperty<Image> fifthTile;
    public ObjectProperty<Image> sixTile;
    public ObjectProperty<Image> sevenTile;


    public List<ObjectProperty<Image>> tiles;
    public List<StringProperty> playersNames;
    public List<IntegerProperty> playersScores;

    //Other parameters:...
    GameManager model; //Game status



    public ViewModel(){
        playersNames = new ArrayList<>();
        playersScores = new ArrayList<>();
        tiles = new ArrayList<>();
        playerId = new SimpleIntegerProperty();

        wordFromPlayer = new SimpleStringProperty();
        playerAction = new SimpleStringProperty();

        firstPlayerName = new SimpleStringProperty();
        secondPlayerName = new SimpleStringProperty();
        thirdPlayerName = new SimpleStringProperty();
        fourthPlayerName = new SimpleStringProperty();

        firstPlayerScore = new SimpleIntegerProperty();
        secondPlayerScore = new SimpleIntegerProperty();
        thirdPlayerScore = new SimpleIntegerProperty();
        fourthPlayerScore = new SimpleIntegerProperty();

        firstTile = new SimpleObjectProperty<>();
        secondTile = new SimpleObjectProperty<>();
        thirdTile = new SimpleObjectProperty<>();
        fourthTile = new SimpleObjectProperty<>();
        fifthTile = new SimpleObjectProperty<>();
        sixTile = new SimpleObjectProperty<>();
        sevenTile = new SimpleObjectProperty<>();

        playersNames.add(firstPlayerName);
        playersNames.add(secondPlayerName);
        playersNames.add(thirdPlayerName);
        playersNames.add(fourthPlayerName);

        playersScores.add(firstPlayerScore);
        playersScores.add(secondPlayerScore);
        playersScores.add(thirdPlayerScore);
        playersScores.add(fourthPlayerScore);

        tiles.add(firstTile);
        tiles.add(secondTile);
        tiles.add(thirdTile);
        tiles.add(fourthTile);
        tiles.add(fifthTile);
        tiles.add(sixTile);
        tiles.add(sevenTile);
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
            updatePlayerTiles();
        });
    }

    public void updatePlayerNames(){
        for(int playerId : model.getGameData().getAllPlayers().keySet().stream().sorted().toList()){
            playersNames.get(playerId-1).setValue(model.getGameData().getPlayer(playerId).getName());
        }
    }

    public void updatePlayerScores(){
        for(int playerId : model.getGameData().getAllPlayers().keySet().stream().sorted().toList()){
            playersScores.get(playerId-1).setValue(model.getGameData().getPlayer(playerId).getScore());
        }
    }

    public void updatePlayerTiles(){
        int i = 0;
        for(Tile tile : model.getGameData().getPlayer(playerId.get()).getAllTiles()){
            String fullPath = Objects.requireNonNull(ViewController
                    .class.getResource("/Images/Tiles/" + tile.letter + ".png")).toExternalForm();
            tiles.get(i).setValue(new Image(fullPath));
            i++;
        }
    }



    public GameManager getModel() {
        return model;
    }
}
