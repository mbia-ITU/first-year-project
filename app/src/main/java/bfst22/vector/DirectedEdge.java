package bfst22.vector;

public class DirectedEdge {
    private final OSMNode v;
    private final OSMNode w;
    private final int speedLimit;
    private final double distance;


    public DirectedEdge(OSMNode v, OSMNode w, int speedLimit) {
        if (Double.isNaN(speedLimit)) throw new IllegalArgumentException("speedlimit is NaN");
        this.v = v;
        this.w = w;
        this.speedLimit = speedLimit;
        this.distance = calcDist(); 
    }

    public double calcDist(){
        return Math.sqrt(Math.abs(v.getLat() - w.getLat()) + Math.abs(v.getLon() - w.getLon())); 
    }

    public double weight(){
        return speedLimit*distance;
    }

    public OSMNode from(){
        return v;
    }

    public OSMNode to(){
        return w;
    }
}
