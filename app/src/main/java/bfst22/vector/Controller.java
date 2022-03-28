package bfst22.vector;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.imageio.IIOException;
import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Controller {
    private Point2D lastMouse;

    @FXML
    private MapCanvas canvas;
    @FXML
    private Label percentText;
    @FXML
    private TextField dir1;
    @FXML
    private Text desc;

    public void init(Model model) {
        canvas.init(model);
    }

    @FXML
    private void onScroll(ScrollEvent e) {
        var factor = e.getDeltaY();
        canvas.zoom(Math.pow(1.003, factor), e.getX(), e.getY());
        percentText.setText(String.valueOf("Zoom: "+canvas.getZoomPercentage() + "%"));
    }

    @FXML
    private void onMouseDragged(MouseEvent e) {
        var dx = e.getX() - lastMouse.getX();
        var dy = e.getY() - lastMouse.getY();
        canvas.pan(dx, dy);
        lastMouse = new Point2D(e.getX(), e.getY());
    }

    @FXML
    private void onMousePressed(MouseEvent e) {
        lastMouse = new Point2D(e.getX(), e.getY());
    }

    //loads DK map
    @FXML
    private void onPress(ActionEvent e)throws Exception {
        var model = new Model("data/Bornholm.zip");
        Stage mapStage = new Stage();
        new MapView(model, mapStage,"UI.fxml");
        View.exitMenu();
    }
    //loads custom map from .zip or .osm
    @FXML
    private void onCustomPress(ActionEvent e) throws Exception {
        String dir = dir1.getCharacters().toString();
        dir = dir.replaceAll("\\\\", "/");
        try {
            var model = new Model(dir);
            Stage mapStage = new Stage();
            new MapView(model, mapStage, "UI.fxml");
            View.exitMenu();
        }catch (Exception ex){
        desc.setText("The chosen file was not found");
        desc.setFill(Color.RED);
        }

    }

}
