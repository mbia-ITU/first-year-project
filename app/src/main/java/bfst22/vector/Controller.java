package bfst22.vector;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class Controller {
    private Point2D lastMouse;

    @FXML
    private MapCanvas canvas;
    @FXML
    private Label percentText;

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
        new MapView(model, "UI.fxml");
        View.exitMenu();
    }
}
