package bfst22.vector;

import java.util.*;

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
    int drawLevel = 0;
    double zp;
    BoundingBox box;

    void init(Model model) {
        this.model = model;
        pan(-model.minlon, -model.minlat);
        zoom(640 / (model.maxlon - model.minlon), 0, 0);
        model.addObserver(this::repaint);
        repaint();
        initialZoomLevel = trans.getMxx();
    }

    void repaint() {
        List<OSMWay> results = new ArrayList<>(KdTree.searchTree(frame));
        var gc = getGraphicsContext2D();
        gc.setTransform(new Affine());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, getWidth(), getHeight());
        gc.setTransform(trans);
        for(ways : KdTree.searchTree(frame())){
            //if(way.type() == LAKE){
                //}
            }

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
        if (drawLevel >= 1) {
        gc.setFill(Color.LIGHTGREY);
        for (var line : model.iterable(WayType.RESIDENTIAL)) {
            line.fill(gc); }
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
        if (drawLevel == 2){
        gc.setFill(Color.SEASHELL);
        for (var line : model.iterable(WayType.RESORT)) {
            line.fill(gc); }
        }
        if (drawLevel == 2){
        gc.setFill(Color.LIGHTYELLOW);
        for (var line : model.iterable(WayType.HOSPITAL)) {
            line.fill(gc); }
        }
        if (drawLevel == 2){
        gc.setFill(Color.MEDIUMPURPLE);
        for (var line : model.iterable(WayType.HELIPAD)) {
            line.fill(gc); }
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
        if (drawLevel == 2) {
        gc.setFill(Color.DARKTURQUOISE);
        for (var line : model.iterable(WayType.PITCH)) {
            line.fill(gc); }
        }
        if (drawLevel == 2) {
        gc.setFill(Color.DARKTURQUOISE);
        for (var line : model.iterable(WayType.SOCCER)) {
            line.fill(gc); }
        }
        if (drawLevel == 2) {
        gc.setFill(Color.LAVENDER);
        for (var line : model.iterable(WayType.PARKING)) {
            line.fill(gc); }
        }
        if (drawLevel == 2) {
        gc.setFill(Color.HONEYDEW);
        for (var line : model.iterable(WayType.GOLFCOURSE)) {
            line.fill(gc); }
        }
        if (drawLevel >= 1) {
        gc.setFill(Color.GOLDENROD);
        for (var line : model.iterable(WayType.PROTECTEDAREA)) {
            line.fill(gc); }
        }
        if (drawLevel >= 1) {
        gc.setFill(Color.SEASHELL);
        for (var line : model.iterable(WayType.RESERVE)) {
            line.fill(gc); }
        }
        if (drawLevel == 2) {
        gc.setFill(Color.MEDIUMAQUAMARINE);
        for (var line : model.iterable(WayType.CEMETERY)) {
            line.fill(gc); }
        }
        /*
        gc.setFill(Color.ORANGE);
        for (var line : model.iterable(WayType.PRIMARYHIGHWAY)) {
            line.fill(gc);
        }
        */
        if (drawLevel == 2) {
        gc.setFill(Color.LIGHTSTEELBLUE);
        for (var line : model.iterable(WayType.RACE)) {
            line.fill(gc); }
        }
        if (drawLevel == 2) {
        gc.setFill(Color.RED);
        for (var line : model.iterable(WayType.RACEWAY)) {
            line.fill(gc); }
        }
        if (drawLevel == 2) {
        gc.setFill(Color.GRAY);
        for (var line : model.iterable(WayType.BUILDING)) {
            line.fill(gc); }
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
        if (zoomPercentage < 200) drawLevel = 0;
        if (zoomPercentage > 200 && zoomPercentage < 500) drawLevel = 1;
        if (zoomPercentage > 500) drawLevel = 2;
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
        zp = zoomPercentage;
        double percent1 = zp * 100;
        int percent2 = (int) percent1;
        return (double) percent2/100;
    }

    public BoundingBox frame(){
        float minX = 0;
        float minY = 0;
        Double db_maxX = getWidth();
        Double db_maxY = getHeight();
        float maxX = db_maxX.floatValue();
        float maxY = db_maxY.floatValue();

        return new BoundingBox(minX, maxX, minY, maxY);
    }
}