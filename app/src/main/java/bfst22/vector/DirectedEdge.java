package bfst22.vector;

import java.io.Serializable;

/**
 * the class {@code DirectedEdge} represents an edge between a node "v" and another node "w", these 
 * nodes hold a speedlimit, and a dinstance. The distance is calculated through the coordinates
 * of the start and end node, and the speedlimit is set upon parsing in the {@code Model} class
 * this class also holds an important weight() method, which returns the weight of the edge such
 * that Dijkstra is able to calculate the shortest path.
 */
public class DirectedEdge implements Serializable {
    private final OSMNode v;
    private final OSMNode w;
    private final int speedLimit;
    private final double distance;


    /**
     * The constructor of DirectedEdge creates an edge between 2 OSMNode objects.
     * @param v - the start node of the edge
     * @param w - the end node of the edge
     * @param speedLimit - the speed at which you are allowed to travel on the road between the nodes
     */
    public DirectedEdge(OSMNode v, OSMNode w, int speedLimit) {
        if (Double.isNaN(speedLimit)) throw new IllegalArgumentException("speedlimit is NaN");
        this.v = v;
        this.w = w;
        this.speedLimit = speedLimit;
        this.distance = calcDist(); 
    }

    /**
     * overloaded constructor, not taking a speedlimit, but just setting it to the danish base of 80.
     * @param v - the start node of the edge
     * @param w - the end node of the edge
     */
    public DirectedEdge(OSMNode v, OSMNode w) {
        this.v = v;
        this.w = w;
        this.speedLimit = 80;
        this.distance = calcDist(); 
    }

    /**
     * calculates and returns the distance between two nodes
     * @return distance as double between node v and node w
     */
    public double calcDist(){
        return Math.sqrt(Math.abs(v.getLat() - w.getLat()) + Math.abs(v.getLon() - w.getLon())); 
    }

    /**
     * return the weight of the edge
     * @return weight as double, calculated through multiplying the distance by the speedlimit.
     */
    public double weight(){
        return speedLimit*distance;
    }

    /**
     * return the start node v
     * @return the start node v
     */
    public OSMNode from(){
        return v;
    }

    /**
     * return the end node w
     * @return the end node w
     */
    public OSMNode to(){
        return w;
    }
}
