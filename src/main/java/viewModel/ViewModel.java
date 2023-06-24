package viewModel;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import model.logic.host.GameManager;
import model.logic.host.data.Tile;
import view.ViewController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewModel{ // Later implement ViewModelFacade
    //Properties for view:...

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

    public ObjectProperty<Button> skipTurn;
    public ObjectProperty<Button> submit;
    public ObjectProperty<Button> challenge;
    public ObjectProperty<Button> swap;
    public ObjectProperty<Button> sort;
    public ObjectProperty<Button> resign;


    public List<ObjectProperty<Image>> tiles;
    public List<StringProperty> playersNames;
    public List<IntegerProperty> playersScores;

    public ObjectProperty<String> imagePath;

    //Other parameters:...
    GameManager model; //Game status

    public ViewModel(){
        playersNames = new ArrayList<>();
        playersScores = new ArrayList<>();
        tiles = new ArrayList<>();
        playerId = new SimpleIntegerProperty();
        imagePath = new SimpleObjectProperty<>();

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

        resign = new SimpleObjectProperty<>();
        submit = new SimpleObjectProperty<>();
        swap = new SimpleObjectProperty<>();
        sort = new SimpleObjectProperty<>();
        challenge = new SimpleObjectProperty<>();
        skipTurn = new SimpleObjectProperty<>();

        resign.set(new Button());
        submit.set(new Button());
        challenge.set(new Button());
        swap.set(new Button());
        sort.set(new Button());
        skipTurn.set(new Button());


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
            updateButtons();
            updateBoard();
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
        if(model.getGameData().getPlayer(playerId.get()) == null) return;
        for(Tile tile : model.getGameData().getPlayer(playerId.get()).getAllTiles()){
            String fullPath = Objects.requireNonNull(ViewController
                    .class.getResource("/Images/Tiles/" + tile.letter + ".png")).toExternalForm();
            tiles.get(i).setValue(new Image(fullPath));
            i++;
        }
    }

    public void updateBoard(){
        int counter = 0;
        Tile[][] gameBoard = model.getGameData().getBoard().getTiles();
        for(int i = 0; i < 15; i++){
            for (int j = 0; j < 15; j++) {
                if(gameBoard[i][j] != null){
                    System.out.println("ViewModel->UpdateBoard-> ! Found tile in new received board i:"+i+", j:" + j + "| counter = " + counter);
                    System.out.println(counter + "," + Objects.requireNonNull(ViewController
                            .class.getResource("/Images/Tiles/" + gameBoard[i][j].letter + ".png")).toExternalForm());
                    imagePath.set(counter + "," + Objects.requireNonNull(ViewController
                            .class.getResource("/Images/Tiles/" + gameBoard[i][j].letter + ".png")).toExternalForm());
                }
                counter++;
            }
        }
    }

    public void updateButtons(){
        if(!getModel().isGameRunning() || model.getTurnManager().getTurnManagerIndex() == -1) return;
        if(model.getCurrentPlayerID() == playerId.get()){
            resign.get().setVisible(true);
            submit.get().setVisible(true);
            swap.get().setVisible(true);
            sort.get().setVisible(true);
            skipTurn.get().setVisible(true);
            challenge.get().setVisible(true);
        }
        else{
            submit.get().setVisible(false);
            swap.get().setVisible(false);
            sort.get().setVisible(false);
            skipTurn.get().setVisible(false);
            challenge.get().setVisible(false);
        }
    }



    public GameManager getModel() {
        return model;
    }
}
