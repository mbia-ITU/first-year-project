package bfst22.vector;

import java.io.Serializable;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;

public class PolyLine implements Drawable, Serializable {
    public static final long serialVersionUID = 134123;
    float[] coords;

    public PolyLine(List<OSMNode> nodes) {
        coords = new float[nodes.size() * 2];
        int i = 0;
        for (var node : nodes) {
            coords[i++] = node.lat;
            coords[i++] = node.lon;
        }
    }

    @Override
    public void trace(GraphicsContext gc) {
        gc.moveTo(coords[0], coords[1]);
        for (var i = 2; i < coords.length; i += 2) {
            gc.lineTo(coords[i], coords[i + 1]);
        }
    }

    public void resize(double zoomlevel) {

    }

    @Override
    public BoundingBox getBoundingBox() {
        // find værdierne minX, maxX, minY, max Y
        float minX = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;

        for (int i = 0; i >= coords.length; i++){
            //Find latitude for Boundingbox
            if (coords[i]<minY){
                minY = coords[i];
            }

            if (coords[0]>maxY){
                maxY = coords[i];
            }

            //Find longitude for Boundingbox
            if (coords[i+1]<minX){
                minX = coords[i+1];
            }

            if (coords[i+1]>maxX){
                maxX = coords[i+1];
            }

            i++;
        }

        //Return new Boundingbox with max coordinates and min coordinates from Polyline coordinates
        return new BoundingBox(minX, maxX, minY, maxY);
    }
}
