package bfst22.vector;

import java.io.Serializable;
import java.util.*;

public class EdgeWeightedDigraph implements Serializable {
    public Map<OSMNode, ArrayList<DirectedEdge>> adj;
    //public Map<OSMNode, Integer> index;
    //public Map<Integer, OSMNode> indexNode;
    public ArrayList<OSMNode> indexNode2;
    //private int[] indegree;
    //int indexCounter;

    public EdgeWeightedDigraph() {
        adj = new HashMap<>();
        //index = new HashMap<>();
        //indexNode = new HashMap<>();
        indexNode2 = new ArrayList<>();
        //indexCounter = 0;
    }

    public int V() {
        return adj.size();
    }

    public Iterable<DirectedEdge> edges(){
        ArrayList<DirectedEdge> allEdges = new ArrayList<DirectedEdge>();
        for (List<DirectedEdge> es : adj.values()){
            for (DirectedEdge e : es){
                allEdges.add(e);
            }
        }
        return allEdges;
    }

    public void addEdge(OSMNode n, DirectedEdge e){
        if (adj.containsKey(n)){
            adj.get(n).add(e);
        }
        else{
            ArrayList<DirectedEdge> temp = new ArrayList<>();
            temp.add(e);
            adj.put(n, temp);
            indexNode2.add(n);
            //index.put(n, indexCounter);
            //indexNode.put(indexCounter, n);
            //indexCounter++;
        }
        
    }

    public ArrayList<OSMNode> allNodes(){
        ArrayList<OSMNode> temp = new ArrayList<>();
        for (OSMNode n : adj.keySet()){
            temp.add(n);
        }
        return temp;
    }
}

