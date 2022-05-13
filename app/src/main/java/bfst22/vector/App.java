package bfst22.vector;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * main application class called in the main method {@code Main}
 */
public class App extends Application {
    /**
     * the start method called in main launching a new {@code View} class
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        new View();
    }
}
