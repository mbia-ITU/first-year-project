package bfst22.vector;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // var filename = getParameters().getRaw().get(0);
        //var model = new Model("data/Bornholm.zip");
        new View("Menu.fxml");
        //new View(model, primaryStage);
    }
}
