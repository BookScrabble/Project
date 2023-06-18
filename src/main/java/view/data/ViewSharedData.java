package view.data;

import model.logic.client.Client;
import viewModel.ViewModel;
import java.util.Observable;
import java.util.Observer;

public class ViewSharedData implements Observer {
    ViewModel viewModel;
    Client player;
    GameModelReceiver gameModelReceiver;

    public ViewSharedData(ViewModel viewModel){
        this.viewModel = viewModel;
        gameModelReceiver = null;
    }

    public ViewModel getViewModel() {
        return viewModel;
    }

    public void setPlayer(Client player) {
        this.player = player;
    }

    public Client getPlayer() {
        return player;
    }

    public void setGameModelReceiver(GameModelReceiver gameModelReceiver) {
        this.gameModelReceiver = gameModelReceiver;
        this.gameModelReceiver.addObserver(this);
    }

    public GameModelReceiver getGameModelReceiver() {
        return gameModelReceiver;
    }

    @Override
    public void update(Observable o, Object arg) {
        this.getViewModel().setModel(this.gameModelReceiver.getUpdatedModel());
    }
}
