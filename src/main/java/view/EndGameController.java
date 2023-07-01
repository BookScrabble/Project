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

    public EndGameController(){

    }

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

    public void setViewSharedData(ViewSharedData viewSharedData) {
        this.viewSharedData = viewSharedData;
        initializeLabels();
        updateScoreBoardPage();
    }

    public ViewSharedData getViewSharedData() {
        return viewSharedData;
    }

    @FXML
    private void exitScoreBoardPage(){
        Platform.exit();
    }
}
