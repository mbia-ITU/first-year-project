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

    public Iterable<DirectedEdge> edges(){
        List allEdges = new ArrayList<DirectedEdge>();
        for (List<DirectedEdge> es : adj.values()){
            for (DirectedEdge e : es){
                allEdges.add(e);
            }
        }
        return allEdges;
    }

    public ArrayList<OSMNode> allNodes(){
        ArrayList temp = new ArrayList<>();
        for (OSMNode n : adj.keySet()){
            temp.add(n);
        }
        return temp;
    }
}

