package bfst22.vector;

import java.io.Serializable;
import java.util.*;

import javax.management.RuntimeErrorException;

import javafx.scene.canvas.GraphicsContext;

public class OSMWay implements Serializable{
    public static final long serialVersionUID = 42;
    List<OSMNode> nodes;
    float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE;
    WayType type;
    
    public OSMWay(List<OSMNode> nodes, WayType type) {
        this.nodes = new ArrayList<>(nodes);
        this.type = type;
    }

    public int size() {
        return nodes.size();
    }

    public WayType getType(){
        return type;
    }

}
