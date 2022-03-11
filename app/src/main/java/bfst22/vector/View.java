package bfst22.vector;

import java.io.File;
import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class View {
    Canvas canvas;
    Model model;
    Affine trans = new Affine();

    public View(Model model, Stage primaryStage) {
        this.model = model;
        model.addObserver(this::repaint);

        canvas = new Canvas(640, 480);

        ToolBar toolBar = new ToolBar();
        Button button = new Button("Åben nyt kort");
        button.setOnAction(event -> {openFile(primaryStage);});
        toolBar.getItems().add(button);
        VBox vBox = new VBox(toolBar, new BorderPane(canvas));
        

        primaryStage.setTitle("Kort");
        primaryStage.show();

        canvas.widthProperty().bind(primaryStage.widthProperty());
        canvas.heightProperty().bind(primaryStage.heightProperty());
        canvas.widthProperty().addListener((a,b,c) -> repaint());
        canvas.heightProperty().addListener((a,b,c) -> repaint());
        primaryStage.setScene(new Scene(vBox));
        repaint();
    }

    void repaint() {
        var gc = canvas.getGraphicsContext2D();
        gc.setTransform(new Affine());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setTransform(trans);
        gc.setLineWidth(0.2);
        for (var line : model) {
            line.draw(gc);
        }
    }

    void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        repaint();
    }

    void zoom(double factor, double x, double y) {
        trans.prependTranslation(-x, -y);
        trans.prependScale(factor, factor);
        trans.prependTranslation(x, y);
        repaint();
    }

    public Point2D mouseToModel(Point2D point) {
        try {
            return trans.inverseTransform(point);
        } catch (NonInvertibleTransformException e) {
            throw new RuntimeException(e);
        }
    }

    public void openFile(Stage primaryStage){
        FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Åben et kort");
            File file = fileChooser.showOpenDialog(primaryStage);
            try {model = new Model(file.getAbsolutePath());} catch (IOException exception) {}
            repaint();
    }
}
