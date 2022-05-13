package bfst22.vector;

import java.io.Serializable;
import java.util.*;

/**
 * The class {@code OSMWay} represents an OSMWay from the OSM-datefile.
 */
public class OSMWay implements Serializable {
    public static final long serialVersionUID = 42; // Used to declare specific serializable class.
    List<OSMNode> nodes; // List of OSMNodes in OSMWay.
    float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE; // Coordinates
                                                                                                          // used for
                                                                                                          // creating
                                                                                                          // BoundingBox.
    WayType type; // Waytype of OSMWay.

    /**
     * Initializes an OSMWay from a list of OSMNodes and sets its type.
     * 
     * @param nodes List of nodes in an OSMWay.
     * @param type  Type of OSMWay.
     */
    public OSMWay(List<OSMNode> nodes, WayType type) {
        this.nodes = new ArrayList<>(nodes);
        this.type = type;
    }

    /**
     * Returns the number of nodes in an OSMWay.
     * 
     * @return number of nodes in an OSMWay.
     */
    public int size() {
        return nodes.size();
    }

    /**
     * Returns type of OSMWay.
     * 
     * @return type of an OSMWay.
     */
    public WayType getType() {
        return type;
    }

}
