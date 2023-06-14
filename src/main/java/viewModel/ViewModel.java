package viewModel;

import javafx.beans.property.StringProperty;
import model.logic.host.GameManager;
import view.MenuController;

import java.util.Observable;
import java.util.Observer;

public class ViewModel implements Observer { // Later implement ViewModelFacade

    //Properties for view:...
    StringProperty wordFromPlayer;
    StringProperty playerAction;


    //Other parameters:...
    GameManager model; //Game status

    public ViewModel(){
        this.model = null;
    }



    @Override
    public void update(Observable o, Object arg) {

    }

    public void updateGameManager() {
        //Sends request to host to receive updated gameManager(single instance that sits on host side)
    }
}
