package bfst22.vector;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

public class MapCanvas extends Canvas {
    Model model;
    Affine trans = new Affine();
    double initialZoomLevel;
    double currentZoomLevel;
    double zoomPercentage;
    double zp;

    void init(Model model) {
        this.model = model;
        pan(-model.minlon, -model.minlat);
        zoom(640 / (model.maxlon - model.minlon), 0, 0);
        model.addObserver(this::repaint);
        repaint();
        initialZoomLevel = trans.getMxx();
    }

    void repaint() {
        var gc = getGraphicsContext2D();
        gc.setTransform(new Affine());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, getWidth(), getHeight());
        gc.setTransform(trans);
        gc.setFill(Color.LIGHTBLUE);
        for (var line : model.iterable(WayType.LAKE)) {
            line.fill(gc);
        }
        gc.setFill(Color.LIGHTGREEN);
        for (var line : model.iterable(WayType.GRASS)) {
            line.fill(gc);
        }
        gc.setFill(Color.GRAY);
        for (var line : model.iterable(WayType.BUILDING)) {
            line.fill(gc);
        }
        gc.setLineWidth(1/Math.sqrt(trans.determinant()));
        for (var line : model.iterable(WayType.UNKNOWN)) {
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
        currentZoomLevel = trans.getMxx();
        zoomPercentage = 100 / (initialZoomLevel / currentZoomLevel);
        //System.out.println(zoomPercentage);
        repaint();
    }

    public Point2D mouseToModel(Point2D point) {
        try {
            return trans.inverseTransform(point);
        } catch (NonInvertibleTransformException e) {
            throw new RuntimeException(e);
        }
    }

    public double getZoomPercentage(){
        if(zoomPercentage<99){
            zp = -1 * (100 / (currentZoomLevel/initialZoomLevel));
        }else{
            zp = zoomPercentage;
        }
        double percent1 = zp * 100;
        int percent2 = (int) percent1;
        return (double) percent2/100;
    }
}