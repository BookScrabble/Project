package view.data;

import javafx.event.ActionEvent;
import model.logic.client.Client;
import model.logic.server.MyServer;
import viewModel.ViewModel;

import java.net.ServerSocket;
import java.util.Observable;
import java.util.Observer;

public class ViewSharedData implements Observer {
    ViewModel viewModel;
    Client player;
    GameModelReceiver gameModelReceiver;
    MyServer calculationServer;
    String hostIp;
    String playerName;
    int hostPort;
    boolean isHost;

    public ViewSharedData(ViewModel viewModel){
        this.calculationServer = null;
        this.viewModel = viewModel;
        this.gameModelReceiver = null;
        this.hostIp = "localhost";
        this.isHost = false;
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

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    public boolean getHost(){
        return isHost;
    }

    public void setCalculationServer(MyServer calculationServer) {
        this.calculationServer = calculationServer;
    }

    public MyServer getCalculationServer() {
        return calculationServer;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    public int getHostPort() {
        return hostPort;
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
