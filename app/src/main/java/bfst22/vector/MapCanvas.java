package bfst22.vector;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
    GraphicsContext gc = getGraphicsContext2D();
    int drawType = 1;

    void init(Model model) {
        this.model = model;
        pan(-model.minlon, -model.minlat);
        zoom(640 / (model.maxlon - model.minlon), 0, 0);
        model.addObserver(this::repaint);
        repaint();
        initialZoomLevel = trans.getMxx();
    }

    void repaint() {
        gc.setTransform(new Affine());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, getWidth(), getHeight());
        gc.setTransform(trans);
        if (drawType == 0){
            colorMap();
        }
        else if (drawType == 1) {
            drawLineMap();
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
        //System.out.println(drawLevel);
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

    private void colorMap(){
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
        gc.setFill(Color.DARKSEAGREEN);
        for (var line : model.iterable(WayType.SCRUB)) {
            line.fill(gc);
        }
        gc.setFill(Color.PALEGREEN);
        for (var line : model.iterable(WayType.MEADOW)) {
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
        gc.setFill(Color.LIGHTBLUE);
        for (var line : model.iterable(WayType.LAKE)) {
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
    }

    private void drawLineMap(){
        gc.setLineWidth(1/Math.sqrt(trans.determinant()));
        for (var line : model.iterable(WayType.BEACH)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.BUILDING)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.CEMETERY)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.COASTLINE)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.FARMLAND)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.FARMYARD)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.FOREST)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.GOLFCOURSE)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.GRASS)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.HEATH)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.HELIPAD)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.HOSPITAL)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.INDUSTRIAL)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.LAKE)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.MEADOW)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.PARKING)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.PITCH)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.PRIMARYHIGHWAY)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.PROTECTEDAREA)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.RACE)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.RACEWAY)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.RESERVE)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.RESIDENTIAL)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.RESORT)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.RIVER)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.SAND)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.SCRUB)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.SOCCER)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.TERTIARY)) {
            line.draw(gc);
        }
        for (var line : model.iterable(WayType.WETLAND)) {
            line.draw(gc);
        }
    }

    public void setDrawType(int l){
        drawType = l;
        repaint();
    }

}