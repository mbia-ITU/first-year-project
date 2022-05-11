package bfst22.vector;

import java.lang.Object;

public class TestAssertions {
    
    public static void DijkstraAssertions(EdgeWeightedDigraph G, DijkstraSP dij){
        // check optimality conditions:
        // (i) for all edges e:            distTo[e.to()] <= distTo[e.from()] + e.weight()
        // (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.weight()
        assert dij.check(G, G.indexNode.get(0)) == true : "graph has mistakes in disjkstra";



    }

}
