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
    int radius;
    float x;
    float y;
    float plus = (float) 0.00018;

    public Cirkle(OSMNode node) {
        this.radius = 40;
        this.x = node.getLat();
        this.y = node.getLon();
        //a square not a cirkle
        nodes.add(new OSMNode(1,this.x + plus,this.y+plus));
        nodes.add(new OSMNode(1,this.x+plus,this.y-plus));
        nodes.add(new OSMNode(1,this.x-plus,this.y-plus));
        nodes.add(new OSMNode(1,this.x-plus,this.y+plus));
        nodes.add(new OSMNode(1,this.x+plus,this.y+plus));
        parts.add(new PolyLine(nodes));
    }
    @Override
    public void trace(GraphicsContext gc) {
        gc.setFill(Color.ORANGE);
        gc.fillOval(x,y,0.00018,0.00018);
    }

}

