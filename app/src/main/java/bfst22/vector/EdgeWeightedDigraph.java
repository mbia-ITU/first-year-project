package bfst22.vector;

import java.util.*;

public class EdgeWeightedDigraph {
    public Map<OSMNode, List<DirectedEdge>> adj;
    private int[] indegree;

    public EdgeWeightedDigraph() {
        
    }

    public int V() {
        return adj.size();
    }
}

