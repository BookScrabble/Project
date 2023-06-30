package view.data;

import model.logic.client.Client;
import model.logic.server.MyServer;
import model.logic.server.dictionary.BookScrabbleHandler;
import viewModel.ViewModel;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
/**
 * The shared data class for the views in the Book Scrabble game.
 * This class contains various data that can be shared between different views and components of the application.
 */
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
     * Constructs a new ViewSharedData object with the specified ViewModel.
     * @param viewModel The ViewModel associated with the shared data.
     */
    public ViewSharedData(ViewModel viewModel){
        this.calculationServer = null;
        this.viewModel = viewModel;
        this.gameModelReceiver = null;
        this.hostIp = "localhost";
        this.isHost = false;
    }

    /**
     * Retrieves the ViewModel associated with the shared data.
     * @return The ViewModel object.
     */
    public ViewModel getViewModel() {
        return viewModel;
    }

    /**
     * Sets the player client in the shared data.
     * @param player The Client object representing the player.
     */
    public void setPlayer(Client player) {
        this.player = player;
    }

    /**
     * Retrieves the player client from the shared data.
     * @return The Client object representing the player.
     */
    public Client getPlayer() {
        return player;
    }

    /**
     * Sets the host IP address in the shared data.
     * @param hostIp The host IP address.
     */
    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    /**
     * Retrieves the host IP address from the shared data.
     * @return The host IP address.
     */
    public String getHostIp() {
        return hostIp;
    }

    /**
     * Sets whether the current player is the host.
     * @param host true if the current player is the host, false otherwise.
     */
    public void setHost(boolean host) {
        isHost = host;
    }

    /**
     * Checks if the current player is the host.
     * @return true if the current player is the host, false otherwise.
     */
    public boolean getHost(){
        return isHost;
    }

    /**
     * Sets the calculation server in the shared data.
     * @param calculationServer The MyServer object representing the calculation server.
     */
    public void setCalculationServer(MyServer calculationServer) {
        this.calculationServer = calculationServer;
    }

    /**
     * Retrieves the calculation server from the shared data.
     * @return The MyServer object representing the calculation server.
     */
    public MyServer getCalculationServer() {
        return calculationServer;
    }

    /**
     * Sets the player name in the shared data.
     * @param playerName The player name.
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    /**
     * Sets the host port in the shared data.
     * @param hostPort The host port.
     */
    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    /**
     * Retrieves the host port from the shared data.
     * @return The host port.
     */
    public int getHostPort() {
        return hostPort;
    }

    /**
     * Sets the GameModelReceiver in the shared data and adds the current object as an observer.
     * @param gameModelReceiver The GameModelReceiver object.
     */
    public void setGameModelReceiver(GameModelReceiver gameModelReceiver) {
        this.gameModelReceiver = gameModelReceiver;
        this.gameModelReceiver.addObserver(this);
    }

    /**
     * Closes the model receiver by closing the player socket.
     */
    public void closeModelReceiver(){
        try {
            this.gameModelReceiver.getServer().getPlayerSocket().close();
        } catch (IOException ignored) {}
    }

    /**
     * Updates the shared data when changes occur in the observable object.
     * This method updates the ViewModel with the updated model from the GameModelReceiver.
     * @param o The observable object.
     * @param arg The argument passed to the notifyObservers() method.
     */
    @Override
    public void update(Observable o, Object arg) {
        this.getViewModel().setModel(this.gameModelReceiver.getUpdatedModel());
    }
}
