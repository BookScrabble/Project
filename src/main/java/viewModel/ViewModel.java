package viewModel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.logic.host.GameManager;

import java.util.Observable;
import java.util.Observer;

public class ViewModel implements Observer { // Later implement ViewModelFacade

    //Properties for view:...
    public StringProperty wordFromPlayer;
    public StringProperty playerAction;

    //Other parameters:...
    GameManager model; //Game status

    public ViewModel(){
        wordFromPlayer = new SimpleStringProperty();
        playerAction = new SimpleStringProperty();
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public void setModel(GameManager model) {
        this.model = model;
    }
}
