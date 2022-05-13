package bfst22.vector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;

/**
 * The class {@code Polyline} represents a drawable line to be drawn by
 * {@code MapCanvas}.
 */
public class PolyLine implements Drawable, Serializable {
    public static final long serialVersionUID = 134123; // Used to declare specific serializable class.
    float[] coords; // List of coordinates for a given Polyline.
    List<OSMNode> nodes; // List of OSMNodes in a given Polyline.

    /**
     * Initializes a Polyline from a list of OSMNodes.
     * 
     * @param nodes List of OSMNodes.
     */
    public PolyLine(List<OSMNode> nodes) {
        coords = new float[nodes.size() * 2];
        int i = 0;
        for (var node : nodes) {
            coords[i++] = node.lat;
            coords[i++] = node.lon;
        }
        this.nodes = new ArrayList<>(nodes);
    }

    /**
     * Uses coordinates from Polyline to tell where to draw without drawing.
     * 
     * @param gc GraphicsContext used later to draw Polyline.
     */
    @Override
    public void trace(GraphicsContext gc) {
        gc.moveTo(coords[0], coords[1]);
        for (var i = 2; i < coords.length; i += 2) {
            gc.lineTo(coords[i], coords[i + 1]);
        }
    }

    /**
     * Used to implement drawable is Polyline.
     * Left Empty of purpose.
     */
    public void resize(double zoomlevel) {

    }

    /**
     * Returns list of nodes in a Polyline.
     * 
     * @return List of nodes in a Polyline.
     */
    public List<OSMNode> getListOfNodes() {
        return this.nodes;
    }

    /**
     * Returns BoundingBox for a Polyline.
     * 
     * @return BoundingBox for a Polyline.
     */
    @Override
    public BoundingBox getBoundingBox() {
        // find values for minX, maxX, minY, max Y
        float minX = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;

        for (int i = 0; i < coords.length; i += 2) {
            // Find latitude for Boundingbox
            if (coords[i + 1] < minY) {
                minY = coords[i + 1];
            }

            if (coords[i + 1] > maxY) {
                maxY = coords[i + 1];
            }

            // Find longitude for Boundingbox
            if (coords[i] < minX) {
                minX = coords[i];
            }

            if (coords[i] > maxX) {
                maxX = coords[i];
            }

        }

        // Return new Boundingbox with max coordinates and min coordinates from Polyline
        // coordinates
        return new BoundingBox(minX, maxX, minY, maxY);
    }

}
