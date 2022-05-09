
package bfst22.vector;
import java.util.*;


public class DijkstraSP {
    private Map<OSMNode, Double> distTo;          // distTo[v] = distance  of shortest s->v path
    private Map<OSMNode, DirectedEdge> edgeTo;
    //private DirectedEdge[] edgeTo;    // edgeTo[v] = last edge on shortest s->v path
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


        distTo = new HashMap<OSMNode, Double>();
        edgeTo = new HashMap<OSMNode, DirectedEdge>();
        //edgeTo = new DirectedEdge[G.V()];

        validateVertex(s, G);

        for (OSMNode n : G.allNodes()) {
            distTo.put(n, Double.POSITIVE_INFINITY);
        }
        distTo.put(s, 0.0);

        

        // relax vertices in order of distance from s
        /*
        pq = new IndexMinPQ<Double>(G.V());
        int tempIn = G.index.get(s);
        pq.insert(tempIn, distTo.get(s));
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            OSMNode w = G.indexNode.get(v);
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
            int tempIndex = G.index.get(w);
            if (pq.contains(tempIndex)) pq.decreaseKey(tempIndex, distTo.get(w));
            else                pq.insert(tempIndex, distTo.get(w));
        }*/
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
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for (DirectedEdge e = edgeTo.get(v); e != null; e = edgeTo.get(e.from())) {
            path.push(e);
        }
        return path;
    }


    // check optimality conditions:
    // (i) for all edges e:            distTo[e.to()] <= distTo[e.from()] + e.weight()
    // (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
    private boolean check(EdgeWeightedDigraph G, OSMNode s) {

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

    /**
     * Unit tests the {@code DijkstraSP} data type.
     *
     * @param args the command-line arguments
     */

}
