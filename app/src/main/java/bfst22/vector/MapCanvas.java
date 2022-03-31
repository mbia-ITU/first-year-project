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
        gc.setFill(Color.PINK);
        for (var line : model.iterable(WayType.COASTLINE)) {
            line.fill(gc);
        }
        gc.setFill(Color.DARKGREEN);
        for (var line : model.iterable(WayType.FOREST)) {
            line.fill(gc);
        }
        gc.setFill(Color.KHAKI);
        for (var line : model.iterable(WayType.FARMLAND)) {
            line.fill(gc);
        }
        gc.setFill(Color.LIGHTGREY);
        for (var line : model.iterable(WayType.RESIDENTIAL)) {
            line.fill(gc);
        }
        gc.setFill(Color.TAN);
        for (var line : model.iterable(WayType.HEATH)) {
            line.fill(gc);
        }
        gc.setFill(Color.MOCCASIN);
        for (var line : model.iterable(WayType.BEACH)) {
            line.fill(gc);
        }
        gc.setFill(Color.OLDLACE);
        for (var line : model.iterable(WayType.SAND)) {
            line.fill(gc);
        }
        gc.setFill(Color.TEAL);
        for (var line : model.iterable(WayType.WETLAND)) {
            line.fill(gc);
        }
        gc.setFill(Color.PEACHPUFF);
        for (var line : model.iterable(WayType.FARMYARD)) {
            line.fill(gc);
        }
        gc.setFill(Color.PLUM);
        for (var line : model.iterable(WayType.INDUSTRIAL)) {
            line.fill(gc);
        }
        gc.setFill(Color.HONEYDEW);
        for (var line : model.iterable(WayType.GOLFCOURSE)) {
            line.fill(gc);
        }
        gc.setFill(Color.ALICEBLUE);
        for (var line : model.iterable(WayType.AIRPORT)) {
            line.fill(gc);
        }
        gc.setFill(Color.LIGHTGREEN);
        for (var line : model.iterable(WayType.GRASS)) {
            line.fill(gc);
        }
        gc.setFill(Color.DARKSEAGREEN);
        for (var line : model.iterable(WayType.SCRUB)) {
            line.fill(gc);
        }
        gc.setFill(Color.PALEGREEN);
        for (var line : model.iterable(WayType.MEADOW)) {
            line.fill(gc);
        }
        gc.setFill(Color.SEASHELL);
        for (var line : model.iterable(WayType.RESORT)) {
            line.fill(gc);
        }
        gc.setFill(Color.CHARTREUSE);
        for (var line : model.iterable(WayType.VINEYARD)) {
            line.fill(gc);
        }
        gc.setFill(Color.LIGHTBLUE);
        for (var line : model.iterable(WayType.LAKE)) {
            line.fill(gc);
        }        
        gc.setFill(Color.LIGHTYELLOW);
        for (var line : model.iterable(WayType.HOSPITAL)) {
            line.fill(gc);
        }
        gc.setFill(Color.MEDIUMPURPLE);
        for (var line : model.iterable(WayType.HELIPAD)) {
            line.fill(gc);
        }
        /*
        gc.setStroke(Color.LIGHTBLUE);
        for (var line : model.iterable(WayType.RIVER)) {
            line.draw(gc);
        }
        gc.setStroke(Color.LIGHTBLUE);
        for (var line : model.iterable(WayType.TERTIARY)) {
            line.draw(gc);
        }
        */
        gc.setFill(Color.DARKTURQUOISE);
        for (var line : model.iterable(WayType.PITCH)) {
            line.fill(gc);
        }
        gc.setFill(Color.DARKTURQUOISE);
        for (var line : model.iterable(WayType.SOCCER)) {
            line.fill(gc);
        }
        gc.setFill(Color.LAVENDER);
        for (var line : model.iterable(WayType.PARKING)) {
            line.fill(gc);
        }
        gc.setFill(Color.GOLDENROD);
        for (var line : model.iterable(WayType.PROTECTEDAREA)) {
            line.fill(gc);
        }
        gc.setFill(Color.SEASHELL);
        for (var line : model.iterable(WayType.RESERVE)) {
            line.fill(gc);
        }
        gc.setFill(Color.MEDIUMAQUAMARINE);
        for (var line : model.iterable(WayType.CEMETERY)) {
            line.fill(gc);
        }
        /*
        gc.setFill(Color.ORANGE);
        for (var line : model.iterable(WayType.PRIMARYHIGHWAY)) {
            line.fill(gc);
        }
        */
        gc.setFill(Color.LIGHTCYAN);
        for (var line : model.iterable(WayType.APRON)) {
            line.fill(gc);
        }
        gc.setFill(Color.LIGHTSTEELBLUE);
        for (var line : model.iterable(WayType.RACE)) {
            line.fill(gc);
        }
        //Tjek raceway
        gc.setFill(Color.RED);
        for (var line : model.iterable(WayType.RACEWAY)) {
            line.draw(gc);
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