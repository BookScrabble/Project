package view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.logic.host.data.Player;
import view.data.ViewSharedData;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EndGameController{

    @FXML
    Label scoreBoardP1Name;
    @FXML
    Label scoreBoardP2Name;
    @FXML
    Label scoreBoardP3Name;
    @FXML
    Label scoreBoardP4Name;

    @FXML
    Label scoreBoardP1Score;
    @FXML
    Label scoreBoardP2Score;
    @FXML
    Label scoreBoardP3Score;
    @FXML
    Label scoreBoardP4Score;

    @FXML
    Label scoreBoardWinnerName;

    List<Label> scoreBoardNames;
    List<Label> scoreBoardScores;

    ViewSharedData viewSharedData;

    /**
     * The EndGameController function is a constructor that initializes the EndGameController class.
     */
    public EndGameController(){

    }

    /**
     * The initializeLabels function initializes the scoreBoardNames and scoreBoardScores ArrayLists.
     * The initializeLabels function also adds the labels for each player's name and score to their respective ArrayList.
     */
    public void initializeLabels() {
        scoreBoardNames = new ArrayList<>();
        scoreBoardScores = new ArrayList<>();

        scoreBoardNames.add(scoreBoardP1Name);
        scoreBoardNames.add(scoreBoardP2Name);
        scoreBoardNames.add(scoreBoardP3Name);
        scoreBoardNames.add(scoreBoardP4Name);

        scoreBoardScores.add(scoreBoardP1Score);
        scoreBoardScores.add(scoreBoardP2Score);
        scoreBoardScores.add(scoreBoardP3Score);
        scoreBoardScores.add(scoreBoardP4Score);
    }

    /**
     * The updateScoreBoardPage function is called when the game ends. It updates the score board page with
     * all the players' names and scores, sorted by highest to lowest score. If there are less than 4 players,
     * then it will leave some of the fields blank. The winner's name is also displayed on this page as well.
     */
    public void updateScoreBoardPage() {
        Map<Integer, Player> players = viewSharedData.getViewModel().getModel().getGameData().getAllPlayers();
        List<Integer> sortedKeys = players.keySet().stream()
                .sorted((firstKey, secondKey) -> players.get(secondKey).getScore() - players.get(firstKey).getScore())
                .collect(Collectors.toCollection(ArrayList::new));
        int currentIndex;
        for(int i = 0; i < 4; i++) {
            if (i < sortedKeys.size()) {
                currentIndex = sortedKeys.get(i);
                scoreBoardNames.get(i).setText(players.get(currentIndex).getName());
                scoreBoardScores.get(i).setText(String.valueOf(players.get(currentIndex).getScore()));
            }
            else{
                scoreBoardNames.get(i).setText("");
                scoreBoardScores.get(i).setText("");
            }
        }
        scoreBoardWinnerName.setText(scoreBoardNames.get(0).getText());
    }

    /**
     * The setViewSharedData function is used to set the viewSharedData variable in this class.
     * This function is called by the controller when it creates a new instance of this class.
     * The purpose of setting the viewSharedData variable in this class is so that we can access
     * all of its functions and variables from within our ScoreBoardPageController class, which
     * allows us to update our scoreboard page with information about each player's score and name.
     * @param viewSharedData viewSharedData Set the viewSharedData variable to the parameter
     */
    public void setViewSharedData(ViewSharedData viewSharedData) {
        this.viewSharedData = viewSharedData;
        initializeLabels();
        updateScoreBoardPage();
    }

    /**
     * The exitScoreBoardPage function is a function that exits the scoreboard page.
     */
    @FXML
    private void exitScoreBoardPage(){
        Platform.exit();
    }
}
