package view.data;

import model.logic.client.Client;
import viewModel.ViewModel;

public class ViewSharedData {
    ViewModel viewModel;

    Client player;

    public ViewSharedData(ViewModel viewModel){
        this.viewModel = viewModel;
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
}
