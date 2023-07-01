package view.data;

import model.logic.client.Client;
import model.logic.server.MyServer;
import model.logic.server.dictionary.BookScrabbleHandler;
import viewModel.ViewModel;

import java.io.IOException;
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

    /**
     * The ViewSharedData function is a constructor for the ViewSharedData class.
     * It takes in a ViewModel object and sets it to be the viewModel of this instance of
     * ViewSharedData. It also initializes all other fields to null or false, depending on their type.
     * @param viewModel viewModel Update the view
     */
    public ViewSharedData(ViewModel viewModel){
        this.calculationServer = null;
        this.viewModel = viewModel;
        this.gameModelReceiver = null;
        this.hostIp = "localhost";
        this.isHost = false;
    }

    /**
     * The getViewModel function returns the viewModel object.
     * @return The viewModel
     */
    public ViewModel getViewModel() {
        return viewModel;
    }

    /**
     * The setPlayer function sets the player variable to a new Client object.
     * @param player player Set the player variable to the client object that is passed in
     */
    public void setPlayer(Client player) {
        this.player = player;
    }

    /**
     * The getPlayer function returns the player object.
     * @return The player variable
     */
    public Client getPlayer() {
        return player;
    }

    /**
     * The setHostIp function sets the hostIp variable to a new value.
     * @param hostIp hostIp Set the host ip address
     */
    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    /**
     * The getHostIp function returns the hostIp variable.
     * @return The hostIp
     */
    public String getHostIp() {
        return hostIp;
    }

    /**
     * The setHost function sets the isHost variable to true or false.
     * @param host host Determine whether the user is a host or not
     */
    public void setHost(boolean host) {
        isHost = host;
    }

    /**
     * The getHost function returns the boolean value of isHost.
     * @return The value of the isHost variable
     */
    public boolean getHost(){
        return isHost;
    }

    /**
     * The setCalculationServer function sets the calculationServer variable to a new MyServer object.
     * @param calculationServer calculationServer Set the calculationServer variable in this class
     */
    public void setCalculationServer(MyServer calculationServer) {
        this.calculationServer = calculationServer;
    }

    /**
     * The getCalculationServer function returns the calculationServer object.
     * @return The calculationServer object
     */
    public MyServer getCalculationServer() {
        return calculationServer;
    }

    /**
     * The setPlayerName function sets the player's name to a new value.
     * @param playerName playerName Set the playerName variable
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * The setHostPort function sets the hostPort variable to the value of its parameter.
     * @param hostPort hostPort Set the hostPort variable
     */
    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    /**
     * The getHostPort function returns the port number of the host.
     * @return The hostPort variable
     */
    public int getHostPort() {
        return hostPort;
    }

    /**
     * The setGameModelReceiver function is used to set the gameModelReceiver variable.
     * @param gameModelReceiver gameModelReceiver Set the gameModelReceiver variable to the parameter
     */
    public void setGameModelReceiver(GameModelReceiver gameModelReceiver) {
        this.gameModelReceiver = gameModelReceiver;
        this.gameModelReceiver.addObserver(this);
    }

    /**
     * The update function is called by the Observable object when it has changed.
     * The update function then updates the viewModel with a new model from the gameModelReceiver.
     * @param o o Identify the observable object that has changed
     * @param arg arg Pass the updated model from the server to this client
     */
    @Override
    public void update(Observable o, Object arg) {
        this.getViewModel().setModel(this.gameModelReceiver.getUpdatedModel());
    }
}
