package bfst22.vector;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;

/**
 * The AddressCircle Class serves to mark Address objects on the {@code MapCanvas} with a circle
 * that scales in size depending on the zoomlevel of the {@code MapCanvas}
 *
 */
public class AddressCircle implements Drawable, Serializable {
    private List<Drawable> parts = new ArrayList<>();
    private List<OSMNode> nodes = new ArrayList<>();
    private float x;
    private float y;
    private float radius;

    /**
     * Draws a circle with the coordinates of {@code node} as the center.
     * @param node represents the node of an Address
     */
    public AddressCircle(OSMNode node) {
        this.radius = 40;
        this.x = node.getLat();
        this.y = node.getLon();

        nodes.add(new OSMNode(1,this.x + radius,this.y+radius));
        nodes.add(new OSMNode(1,this.x+radius,this.y-radius));
        nodes.add(new OSMNode(1,this.x-radius,this.y-radius));
        nodes.add(new OSMNode(1,this.x-radius,this.y+radius));
        nodes.add(new OSMNode(1,this.x+radius,this.y+radius));
        parts.add(new PolyLine(nodes));
    }

    /**
     *
     * @param gc graphics context from {@code Mapcanvas}
     */
    @Override
    public void trace(GraphicsContext gc) {
        gc.fillOval(x,y,radius,radius);
        gc.setLineWidth(10);
        for (var part : parts) part.trace(gc);
    }

    /**
     *
     * @param zoomLevel zoomlevel of current zoom from {@code Mapcanvas}
     */
    public void resize(double zoomLevel){
        this.radius = (float) (20/zoomLevel);
    }

    /**
     *
     * @return the Boundingbox of the center of the circle
     */
    @Override
    public BoundingBox getBoundingBox() {
        return new BoundingBox(x,x,y,y);
    }

    }

