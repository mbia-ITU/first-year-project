package bfst22.vector;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Cirkle implements Drawable, Serializable {
    public static final long serialVersionUID = 1325234;
    List<Drawable> parts = new ArrayList<>();
    List<OSMNode> nodes = new ArrayList<>();
    float x;
    float y;
    float radius = (float) 0.00018;

    public Cirkle(OSMNode node) {
        this.radius = 40;
        this.x = node.getLat();
        this.y = node.getLon();
        //a square not a cirkle
        nodes.add(new OSMNode(1,this.x + radius,this.y+radius));
        nodes.add(new OSMNode(1,this.x+radius,this.y-radius));
        nodes.add(new OSMNode(1,this.x-radius,this.y-radius));
        nodes.add(new OSMNode(1,this.x-radius,this.y+radius));
        nodes.add(new OSMNode(1,this.x+radius,this.y+radius));
        parts.add(new PolyLine(nodes));
    }
    @Override
    public void trace(GraphicsContext gc) {
        //gc.setFill(Color.RED);
        gc.fillOval(x,y,radius,radius);
        gc.setLineWidth(10);
        for (var part : parts) part.trace(gc);
    }
    //changes size based on zoom
    public void resize(double zoomLevel){
        this.radius = (float) (20/zoomLevel);
    }

@Override
public BoundingBox getBoundingBox() {
    return new BoundingBox(x,x,y,y);
}

}

