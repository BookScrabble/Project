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
    public ObjectProperty<Button> swap;


    public List<ObjectProperty<Image>> tiles;
    public List<StringProperty> playersNames;
    public List<IntegerProperty> playersScores;

    public ObjectProperty<String> imagePath;

    //Other parameters:...
    GameManager model; //Game status

    /**
     * The ViewModel function is the constructor of the ViewModel class.
     * It initializes all the properties that are used in this class, and it also adds them to their respective lists.
     */
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

        submit = new SimpleObjectProperty<>();
        swap = new SimpleObjectProperty<>();
        skipTurn = new SimpleObjectProperty<>();

        submit.set(new Button());
        swap.set(new Button());
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

    /**
     * The setModel function is used to set the model of the view.
     * @param model model Set the model for this view
     */
    public void setModel(GameManager model) {
        this.model = model;
        updateViewProperties();
    }

    /**
     * The getImagePath function returns the imagePath property.
     * @return A stringProperty
     */
    public ObjectProperty<String> getImagePath() {
        imagePath = new SimpleObjectProperty<>();
        return imagePath;
    }

    /**
     * The updateViewProperties function updates the view properties of the game.
     * It calls all the update functions for each property that needs to be updated.
     */
    public void updateViewProperties(){
        updatePlayerNames();
        updatePlayerScores();
        updatePlayerTiles();
        updateButtons();
        updateBoard();
    }

    /**
     * The updatePlayerNames function updates the player names in the GUI.
     * It is called whenever a new player joins or leaves, and when a game starts.
     */
    public void updatePlayerNames(){
        Platform.runLater(() -> {
            for(int playerId : model.getGameData().getAllPlayers().keySet().stream().sorted().toList()){
                playersNames.get(playerId-1).setValue(model.getGameData().getPlayer(playerId).getName());
            }
            int playersConnected = model.getGameData().getAllPlayers().size();
            for (StringProperty playersName : playersNames) {
                if (playersConnected == 0) {
                    playersName.setValue("");
                } else playersConnected--;
            }
        });
    }

    /**
     * The updatePlayerScores function updates the player scores in the GUI.
     * It is called whenever a player's score changes, and it updates all the players' scores.
     */
    public void updatePlayerScores(){
        Platform.runLater(() -> {
            for(int playerId : model.getGameData().getAllPlayers().keySet().stream().sorted().toList()){
                playersScores.get(playerId-1).setValue(model.getGameData().getPlayer(playerId).getScore());
            }
        });
    }

    /**
     * The updatePlayerTiles function is used to update the tiles that are displayed on the screen.
     * It does this by iterating through all the tiles in a player's hand and setting their image
     * to be an image of that tile. This function is called whenever a player makes a move, or when they
     * draw new tiles from the bag. The reason it uses Platform.runLater() is because it needs to run on
     * JavaFx's thread, not its own thread (which would cause errors). This function also checks if there are any null values in order to prevent errors from occurring when players leave mid-game and their data
     */
    public void updatePlayerTiles(){
        Platform.runLater(() -> {
            int i = 0;
            if(model.getGameData().getPlayer(playerId.get()) == null) return;
            for(Tile tile : model.getGameData().getPlayer(playerId.get()).getAllTiles()){
                String fullPath = Objects.requireNonNull(ViewController
                        .class.getResource("/Images/Tiles/" + tile.letter + ".png")).toExternalForm();
                tiles.get(i).setValue(new Image(fullPath));
                i++;
            }
        });
    }

    /**
     * The updateBoard function is called whenever the board needs to be updated.
     * This function will update the imagePath property of each tile in the gameBoard,
     * which will then cause a change listener to fire and update all the images on screen.
     */
    public void updateBoard(){
        Platform.runLater(() -> {
            int counter = 0;
            Tile[][] gameBoard = model.getGameData().getBoard().getTiles();
            for(int i = 0; i < 15; i++){
                for (int j = 0; j < 15; j++) {
                    if(gameBoard[i][j] != null){
                        System.out.println("Updating Image for letter " + gameBoard[i][j].letter);
                        System.out.println(counter + "," + Objects.requireNonNull(ViewController
                                .class.getResource("/Images/Tiles/" + gameBoard[i][j].letter + ".png")).toExternalForm());
                        imagePath.set(counter + "," + Objects.requireNonNull(ViewController
                                .class.getResource("/Images/Tiles/" + gameBoard[i][j].letter + ".png")).toExternalForm());
                    }
                    else{
                        imagePath.set(counter + "," + "labelVisible");
                    }
                    counter++;
                }
            }
        });
    }

    /**
     * The updateButtons function is called whenever the game state changes.
     * It updates the visibility of buttons on the screen depending on whether it is a player's turn, and if so, which player's turn it is.
     */
    public void updateButtons(){
        Platform.runLater(() -> {
            if(!getModel().isGameRunning() || model.getTurnManager().getTurnManagerIndex() == -1) return;
            if(model.getCurrentPlayerID() == playerId.get()){
                submit.get().setVisible(true);
                swap.get().setVisible(true);
                skipTurn.get().setVisible(true);
            }
            else{
                submit.get().setVisible(false);
                swap.get().setVisible(false);
                skipTurn.get().setVisible(false);
            }
        });
    }

    /**
     * The getModel function returns the GameManager object that is being used by the controller.
     * @return The model
     */
    public GameManager getModel() {
        return model;
    }
}
