package bfst22.vector;

import java.util.*;

public class kdNode {
    float split;
    kdNode left, right;
    BoundingBox box;
    
    public kdNode(List<OSMWay> list) {
        box = box.combineMany(list);
    }
}
 

