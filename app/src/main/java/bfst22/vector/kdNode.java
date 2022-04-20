package bfst22.vector;

import java.util.*;

public class kdNode {
    List<OSMWay> wayList;
    kdNode left, right;
    BoundingBox box;
    
    public kdNode(List<OSMWay> list) {
        box = list.get(0).getPlotBounds().combineMany(list);
    }

    public static BoundingBox getPlotBounds(List<OSMWay> nodes) {
        if(nodes.size() == 0){
        throw new RuntimeException("OSMWay does not have any nodes");
        }
        float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE;

        for (OSMWay node : nodes) {
            minX = Math.min(minX, node.minX);
            minY = Math.min(minY, node.minY);
            maxX = Math.max(maxX, node.minX);
            maxY = Math.max(maxY, node.minY);
        }
    
        return new BoundingBox(minX, maxX, minY, maxY);
    }
}
 

