package bfst22.vector;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import javafx.scene.canvas.GraphicsContext;

public class OSMWay implements Serializable {
    public static final long serialVersionUID = 42;
    List<OSMNode> nodes;
    float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE;
    
    public OSMWay(List<OSMNode> nodes) {
        this.nodes = new ArrayList<>(nodes);
    }
    
    public int size() {
        return nodes.size();
    }

    private BoundingBox getPlotBounds() {
        if(nodes.size() == 0){
        throw new RuntimeException("OSMWay does not have any nodes");
        }

        for (OSMNode node : nodes) {
            minX = Math.min(minX, node.getLon());
            minY = Math.min(minY, node.getLat());
            maxX = Math.max(maxX, node.getLon());
            maxY = Math.max(maxY, node.getLat());
        }
    
        return new BoundingBox(minX, maxX, minY, maxY);
    }

}
