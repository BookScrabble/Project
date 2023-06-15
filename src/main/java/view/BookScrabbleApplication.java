package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import viewModel.ViewModel;

import java.io.IOException;

public class BookScrabbleApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
        Scene scene = new Scene(root, 1000, 550);
        scene.getStylesheets().add(getClass().getResource("HomePage.css").toExternalForm());

        ViewModel viewModel = new ViewModel();
        ViewController mc = new ViewController();
        mc.setViewModel(viewModel);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
        System.out.println("main is dead");
    }
}