package bfst22.vector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;

/**
 * The class {@code MultiPolygon} represents a drawable line to be drawn by
 * {@code MapCanvas}.
 */
public class MultiPolygon implements Drawable, Serializable {
    public static final long serialVersionUID = 1325234; // Used to declare specific serializable class.
    List<Drawable> parts = new ArrayList<>(); // List of drawables that make up MultiPolygon.

    /**
     * Initializes a MultiPolygon from a list of OSMWays.
     * The List in then converted to a list of Drawables.
     * 
     * @param rel List of OSMWays that makeup a MultiPolygon.
     */
    public MultiPolygon(ArrayList<OSMWay> rel) {
        for (var way : rel) {
            parts.add(new PolyLine(way.nodes));
        }
    }

    /**
     * Uses coordinates from list of drawables in MultiPolygon to tell
     * where to draw without drawing.
     * 
     * @param gc GraphicsContext used later to draw MultiPolygon.
     */
    public void trace(GraphicsContext gc) {
        for (var part : parts)
            part.trace(gc);
    }

    /**
     * Used to implement drawable is MultiPolygon.
     * Left Empty of purpose.
     */
    public void resize(double zoomlevel) {
    }

    /**
     * 
     */
    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(parts);
    }
}
