package bfst22.vector;

import java.io.Serializable;
import java.util.*;


/**
 * the class {@code EdgeWeightedDigraph} represents an edge weighted digraph of vertices (OSMNode objects)
 * and edges (DirectedEdge objects).
 * every vertex is listed in a HashMap as a key, with the value being an Arraylist of DirectedEdge objects, mapping said vertex to other 
 * vertextes in the graph
 */
public class EdgeWeightedDigraph implements Serializable {
    public Map<OSMNode, ArrayList<DirectedEdge>> adj;
    public ArrayList<OSMNode> indexNode2;

    /**
     * constructor, which initializes the adjacency map for the nodes used by {@code DijkstraSP} and the index array used for the {@code IndexMinPQ} 
     */
    public EdgeWeightedDigraph() {
        adj = new HashMap<>();
        indexNode2 = new ArrayList<>();
    }

    /**
     * returns the amount of verices in the digraph
     * @return full amount of vertices in the digraph
     */
    public int V() {
        return adj.size();
    }

    /**
     * returns an iterable ArrayList of all DirectedEdges in the digraph
     * @return an iterable ArrayList of all DirectedEdges in the digraph
     */
    public Iterable<DirectedEdge> edges(){
        ArrayList<DirectedEdge> allEdges = new ArrayList<DirectedEdge>();
        for (List<DirectedEdge> es : adj.values()){
            for (DirectedEdge e : es){
                allEdges.add(e);
            }
        }
        return allEdges;
    }

    /**
     * adds an edge "e" from the vertex "n" 
     * @param n the vertex node "n" which the edge "e" goes out from
     * @param e the edge "e" from node "n"
     */
    public void addEdge(OSMNode n, DirectedEdge e){
        if (adj.containsKey(n)){
            adj.get(n).add(e);
        }
        else{
            ArrayList<DirectedEdge> temp = new ArrayList<>();
            temp.add(e);
            adj.put(n, temp);
            indexNode2.add(n);
        }
        
    }

    /**
     * returns an ArrayList containing all vertices in the digraph
     * @return returns an ArrayList<OSMNode> of all vertices in the digraph
     */
    public ArrayList<OSMNode> allNodes(){
        ArrayList<OSMNode> temp = new ArrayList<>();
        for (OSMNode n : adj.keySet()){
            temp.add(n);
        }
        return temp;
    }
}

