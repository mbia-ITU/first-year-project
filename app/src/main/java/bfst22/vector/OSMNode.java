package bfst22.vector;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.Serializable;


/**
 * The class {@code OSMNode} represents an OSMNode from the OSM-datafile.
 */
public class OSMNode implements Serializable, Drawable{
    public static final long serialVersionUID = 9082413;    //Used to declare specific serializable class.
    long id;                                                //Id of OSMNode.
    float lat, lon;                                         //Latitude and longitude of OSMNode.

    /**
     * Initializes an OSMNode based on an id, latitude and longitude parsed from {@code Model}.
     * @param id Id of a node in dataset.
     * @param lat latitude of node with given id.
     * @param lon longitude of node with given id.
     */
    public OSMNode(long id, float lat, float lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * Returns Longitude of OSMNode.
     * @return Longitude of OSMNode.
     */
    public float getLon() {
        return lon;
    }

    /**
     * Returns Latitude of OSMNode.
     * @return Latitude of OSMNode.
     */
    public float getLat() {
        return lat;
    }

    /**
     * 
     */
    @Override
    public void draw(GraphicsContext gc) {
        Drawable.super.draw(gc);
    }

    /** */
    @Override
    public void fill(GraphicsContext gc) {
        Drawable.super.fill(gc);
    }

    /** 
     * Used to implement drawable is OSMNode.
     * Left Empty of purpose.
    */
    @Override
    public void trace(GraphicsContext gc) {

    }

    /** 
     * Used to implement drawable is OSMNode.
     * Left Empty of purpose.
    */
    @Override
    public void resize(double zoomlevel) {

    }

    /** 
     * Returns Boundingbox for OSMNode.
     * @return BoundingBox for OSMNode.
    */
    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(lon, lon, lat, lat);
    }
}