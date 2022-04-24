package bfst22.vector;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.Serializable;

public class OSMNode implements Serializable, Drawable{
    public static final long serialVersionUID = 9082413;
    long id;
    float lat, lon;

    public OSMNode(long id, float lat, float lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public float getLon() {
        return lon;
    }

    public float getLat() {
        return lat;
    }


    @Override
    public void draw(GraphicsContext gc) {
        Drawable.super.draw(gc);
    }

    @Override
    public void fill(GraphicsContext gc) {
        Drawable.super.fill(gc);
    }

    @Override
    public void trace(GraphicsContext gc) {

    }

    @Override
    public void resize(double zoomlevel) {

    }
}