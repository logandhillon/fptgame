module com.logandhillon.fptgame {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.logandhillon.fptgame to javafx.fxml;
    exports com.logandhillon.fptgame;
}