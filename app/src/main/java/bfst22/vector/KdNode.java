package bfst22.vector;

import java.io.Serializable;
import java.util.*;

public class KdNode implements Serializable {
    List<Drawable> wayList;
    KdNode left, right;
    BoundingBox bb;
    
    public KdNode(List<Drawable> list) {
        bb = new BoundingBox(list);
    }

}
 
