package bfst22.vector;

import java.io.Serializable;
import java.util.*;

/**
 * The class {@code KdNode} represents a node of a KdTree.
 * A KdNode is made from a list of drawable ways and a BoundingBox.
 */

public class KdNode implements Serializable {
    List<Drawable> wayList; // List of OSMWays.
    KdNode left, right; // KdNodes used by KdTree to branch tree.
    BoundingBox bb; // BoundingBox of KdNode.

    /**
     * Constructor for the KdNode Class.
     * Constructs a KdNode with an attached BoundingBox from the total list of
     * drawable ways.
     * 
     * @param list A Total list of drawable ways.
     */
    public KdNode(List<Drawable> list) {
        bb = new BoundingBox(list);
    }

}
