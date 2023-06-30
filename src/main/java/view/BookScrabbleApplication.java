package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.data.ViewSharedData;
import viewModel.ViewModel;

import java.io.IOException;
import java.util.Objects;

/**
 * The main application class for the Book Scrabble game.
 */
public class BookScrabbleApplication extends Application {

    private static Stage primaryStage;

    /**
     * Starts the JavaFX application and initializes the primary stage.
     * @param stage The primary stage of the application.
     * @throws IOException if an I/O error occurs while loading the home page.
     */
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HomePage.fxml"));
        Parent root = loader.load();

        ViewModel viewModel = new ViewModel();
        ViewSharedData sharedData = new ViewSharedData(viewModel);

        ViewController viewController = loader.getController();
        viewController.setViewSharedData(sharedData);

        Scene scene = new Scene(root, 1400, 900);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("HomePage.css")).toExternalForm());
        stage.setTitle("Book Scrabble");
        stage.setUserData(viewModel);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Retrieves the primary stage of the application.
     * @return The primary stage.
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * The main entry point of the application.
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
        System.out.println("main is dead");
    }
}