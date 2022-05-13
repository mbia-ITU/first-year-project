
package bfst22.vector;
import java.util.*;
/*
    This code was heavily inspired by the DijkstraSP class written by Robert Sedgewick and Kevin Wayne,
    but was rewritten to fit the programs use of nodes and edges, instead of the usual integer to integer 
    style, which used a pseudo graph.
    below you will find the original doc for the class ->
*/
/**
 *  The {@code DijkstraSP} class represents a data type for solving the
 *  single-source shortest paths problem in edge-weighted digraphs
 *  where the edge weights are non-negative.
 *  <p>
 *  This implementation uses <em>Dijkstra's algorithm</em> with a
 *  <em>binary heap</em>. The constructor takes
 *  &Theta;(<em>E</em> log <em>V</em>) time in the worst case,
 *  where <em>V</em> is the number of vertices and <em>E</em> is
 *  the number of edges. Each instance method takes &Theta;(1) time.
 *  It uses &Theta;(<em>V</em>) extra space (not including the
 *  edge-weighted digraph).
 *  <p>
 *  This correctly computes shortest paths if all arithmetic performed is
 *  without floating-point rounding error or arithmetic overflow.
 *  This is the case if all edge weights are integers and if none of the
 *  intermediate results exceeds 2<sup>52</sup>. Since all intermediate
 *  results are sums of edge weights, they are bounded by <em>V C</em>,
 *  where <em>V</em> is the number of vertices and <em>C</em> is the maximum
 *  weight of any edge.
 *  <p>
 *  For additional documentation,    
 *  see <a href="https://algs4.cs.princeton.edu/44sp">Section 4.4</a> of    
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne. 
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public class DijkstraSP {
    private Map<OSMNode, Double> distTo;          // distTo[v] = distance  of shortest s->v path
    private Map<OSMNode, DirectedEdge> edgeTo;      // edgeTo = last edge on shortest s->v path  
    private IndexMinPQ<Double> pq;    // priority queue of vertices

    /**
     * Computes a shortest-paths tree from the source vertex {@code s} to every other
     * vertex in the edge-weighted digraph {@code G}.
     *
     * @param  G the edge-weighted digraph
     * @param  s the source vertex
     * @throws IllegalArgumentException if an edge weight is negative
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
    public DijkstraSP(EdgeWeightedDigraph G, OSMNode s) {
        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }

        //init maps 
        distTo = new HashMap<OSMNode, Double>();
        edgeTo = new HashMap<OSMNode, DirectedEdge>();

        //checks that the source vertex is applicable 
        validateVertex(s, G);

        //sets all nodes to max lenght from "s", this is to isolate nodes that might not be connected
        for (OSMNode n : G.allNodes()) {
            distTo.put(n, Double.POSITIVE_INFINITY);
        }
        distTo.put(s, 0.0);

        

        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(G.V());
        int tempIn = G.indexNode2.indexOf(s);
        pq.insert(tempIn, distTo.get(s));
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            OSMNode w = G.indexNode2.get(v);
            for (DirectedEdge e : G.adj.get(w))
                relax(e, G);
        }

        // check optimality conditions
        assert check(G, s);
    }

    // relax edge e and update pq if changed
    private void relax(DirectedEdge e, EdgeWeightedDigraph G) {
        OSMNode v = e.from(), w = e.to();
        if (distTo.get(w) > distTo.get(v) + e.weight()) {
            distTo.put(w, distTo.get(v) + e.weight());
            edgeTo.put(w, e);
            int tempIndex = G.indexNode2.indexOf(w);
            if (pq.contains(tempIndex)) pq.decreaseKey(tempIndex, distTo.get(w));
            else                pq.insert(tempIndex, distTo.get(w));
        }
    }

    /**
     * Returns the length of a shortest path from the source vertex {@code s} to vertex {@code v}.
     * @param  v the destination vertex
     * @return the length of a shortest path from the source vertex {@code s} to vertex {@code v};
     *         {@code Double.POSITIVE_INFINITY} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public double distTo(OSMNode v, EdgeWeightedDigraph G) {
        validateVertex(v, G);
        return distTo.get(v);
    }

    /**
     * Returns true if there is a path from the source vertex {@code s} to vertex {@code v}.
     *
     * @param  v the destination vertex
     * @return {@code true} if there is a path from the source vertex
     *         {@code s} to vertex {@code v}; {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean hasPathTo(OSMNode v, EdgeWeightedDigraph G) {
        validateVertex(v, G);
        return distTo.get(v) < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path from the source vertex {@code s} to vertex {@code v}.
     *
     * @param  v the destination vertex
     * @return a shortest path from the source vertex {@code s} to vertex {@code v}
     *         as an iterable of edges, and {@code null} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<DirectedEdge> pathTo(OSMNode v, EdgeWeightedDigraph G) {
        validateVertex(v, G);
        if (!hasPathTo(v, G)) return null;
        Stack<DirectedEdge> path = new Stack<>();
        for (DirectedEdge e = edgeTo.get(v); e != null; e = edgeTo.get(e.from())) {
            path.push(e);
        }
        return path;
    }


    // check optimality conditions:
    // (i) for all edges e:            distTo[e.to()] <= distTo[e.from()] + e.weight()
    // (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
    public boolean check(EdgeWeightedDigraph G, OSMNode s) {

        // check that edge weights are non-negative
        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0) {
                System.err.println("negative edge weight detected");
                return false;
            }
        }

        // check that distTo[v] and edgeTo[v] are consistent
        if (distTo.get(s) != 0.0 || edgeTo.get(s) != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (OSMNode v : G.allNodes()) {
            if (v.equals(s)) continue;
            if (edgeTo.get(v) == null && distTo.get(v) != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return false;
            }
        }

        // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.weight()
        for (OSMNode v: G.allNodes()) {
            for (DirectedEdge e : G.adj.get(v)) {
                OSMNode w = e.to();
                if (distTo.get(v) + e.weight() < distTo.get(w)) {
                    System.err.println("edge " + e + " not relaxed");
                    return false;
                }
            }
        }

        // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.weight()
        for (OSMNode w : G.allNodes()) {
            if (edgeTo.get(w) == null) continue;
            DirectedEdge e = edgeTo.get(w);
            OSMNode v = e.from();
            if (w != e.to()) return false;
            if (distTo.get(v) + e.weight() != distTo.get(w)) {
                System.err.println("edge " + e + " on shortest path not tight");
                return false;
            }
        }
        return true;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(OSMNode v, EdgeWeightedDigraph G) {
        if (!G.allNodes().contains(v)){
            throw new IllegalArgumentException("Node " + v + " does not exist");
        }
    }

}
