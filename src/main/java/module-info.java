module view.bookscrabble {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens view to javafx.fxml;
    exports view;
}