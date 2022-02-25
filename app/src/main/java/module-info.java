module bfst22 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    opens bfst22.vector to javafx.fxml;

    exports bfst22.vector;
}
