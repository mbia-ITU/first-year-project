package bfst22.vector;

import java.util.*;

public class kdNode {
    List<OSMWay> wayList;
    kdNode left, right;
    BoundingBox box;
    
    public kdNode(List<OSMWay> list) {
        box = list.get(0).getPlotBounds().combineMany(list);
    }
}
 

