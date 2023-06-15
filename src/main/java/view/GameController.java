package view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextInputDialog;
import viewModel.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class GameController {

    String word;
    boolean vertical;
    int flag;
    ArrayList<Integer> indexRow;
    ArrayList<Integer> indexCol;

    ViewModel viewModel;

    //All functionality buttons will be linked here and from viewModel we will have access to those methods.
    public GameController(){
        this.word = "";
        this.vertical = false;
        this.flag = 0;
        this.indexRow = new ArrayList<>();
        this.indexCol = new ArrayList<>();
        //Binding..
    }

    public void setViewModel(ViewModel viewModel) {
        System.out.println("Setting viewModel in GameController -> " + viewModel);
        this.viewModel = viewModel;
    }

    @FXML
    public void Submit(ActionEvent event) throws IOException {
        while(this.flag == 0){
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Choose Vertical or Horizontal");
            dialog.setHeaderText("Enter Vertical yes/no");
            dialog.setContentText("Vertical:");
            Optional<String> result = dialog.showAndWait();
            if(result.get().equals("yes")){
                this.vertical=true;
                this.flag=1;
                break;
            }else if (result.get().equals("no")){
                this.vertical=false;
                this.flag=0;
                break;
            }
        }
        int startRow =-1, startCol=-1;
        int endRow =-1, endCol=-1;
        if(this.vertical){
            startRow = this.indexRow.get(0);
            startCol = this.indexCol.get(0);
            endRow = this.indexRow.get(this.indexRow.size()-1);
            endCol = this.indexCol.get(this.indexCol.size()-1);
        }
        else{
            startRow = this.indexRow.get(0);
            startCol = this.indexCol.get(0);
            endRow = this.indexRow.get(this.indexRow.size()-1);
            endCol = this.indexCol.get(this.indexCol.size()-1);
        }
        System.out.println("Submit");
        System.out.println("Word: " + this.word + ", Vertical: " + this.vertical);
        System.out.println("Start at index: " + startRow + ", " + startCol);
        System.out.println("End at index: " + endRow + ", " + endCol);
        this.indexRow.clear();
        this.indexCol.clear();
        this.word = "";
        this.flag = 0;
    }
    @FXML
    public void Challenge(ActionEvent event) throws IOException{
        System.out.println("Challenge");
    }
    @FXML
    public void SwapTiles(ActionEvent event) throws IOException{
        System.out.println("SwapTiles");
    }
    @FXML
    public void SortTiles(ActionEvent event) throws IOException{
        System.out.println("Sort");
    }
    @FXML
    public void SkipTurn(ActionEvent event) throws IOException{
        System.out.println("SkipTurn");
    }
    @FXML
    public void Resign(ActionEvent event) throws IOException{
        System.out.println("Resign");
    }

}
