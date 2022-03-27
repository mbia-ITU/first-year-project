package bfst22.vector;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;

public class OSMWay implements Drawable {
    List<OSMNode> nodes;
    Type type;
    public OSMWay(List<OSMNode> nodes, Type type) {
        this.nodes = new ArrayList<>(nodes);
        this.type = type;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.beginPath();
        var first = nodes.get(0);
        gc.moveTo(first.lat, first.lon);
        for (var i = 1 ; i < nodes.size() ; i++) {
            var node = nodes.get(i);
            gc.lineTo(node.lat, node.lon);
        }
        gc.stroke();
    }

    @Override 
    public Type getType(){
        return type;
    }
}
