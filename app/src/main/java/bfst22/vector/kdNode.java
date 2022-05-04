package bfst22.vector;

import java.util.*;

public class KdNode {
    List<Drawable> wayList;
    KdNode left, right;
    BoundingBox bb;
    
    public KdNode(List<Drawable> list) {
        
        bb = new BoundingBox(list);
    }

}
 
