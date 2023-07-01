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

public class BookScrabbleApplication extends Application {

    private static Stage primaryStage;

    /**
     * The start function is the main function of the program.
     * It loads all the FXML files and sets up their controllers, as well as setting up a ViewModel for each controller.
     * @param stage stage Set the title of the window
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
     * The getPrimaryStage function returns the primary stage of the application.
     * @return The primaryStage object
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * The main function is the entry point of a JavaFX application.
     * It creates an instance of the Application class and calls its start method, which then calls the init and stop methods.
     * The main function also passes any command line arguments to this Application object via its init method's parameters.
     * @param args args Pass arguments to the application
     */
    public static void main(String[] args) {
        launch(args);
        System.out.println("main is dead");
    }
}