package bfst22.vector;

import java.io.Serializable;
import java.util.*;

public class KdTree implements Serializable {
    BoundingBox bb;
    KdNode root;
    List<Drawable> totalWays;

    public KdTree(List<Drawable> ways) {
        this.totalWays = ways;
        root = buildKdTree(totalWays, 0);
        bb = root.bb;
    }

    public KdNode buildKdTree(List<Drawable> ways, int currentDepth) {

        KdNode node = new KdNode(ways);

        if (ways.size() > 50) {
            List<List<Drawable>> lists;
            if (currentDepth % 2 == 0) {
                lists = Drawable.splitOnX(ways);
            } else {
                lists = Drawable.splitOnY(ways);
            }

            var leftList = lists.get(0);
            var rightList = lists.get(1);
            //THIS IS WHY YOU ARE NOT SERIALIZABLE
            node.left = buildKdTree(leftList, currentDepth + 1);
            node.right = buildKdTree(rightList, currentDepth + 1);
            //
        } else {
            node.wayList = ways;
        }
        return node;

    }

    public List<Drawable> searchTree(BoundingBox bb) {
        //kald næste searchtree med en ny arrayliste
        //giv den arrayliste med hver gang den anden searchtree bliver kaldt
        var results = searchTree(bb, root);
        //System.out.println(results.size());
        return results;
    }

    private List<Drawable> searchTree(BoundingBox searchbb, KdNode node) {

        if(node.bb.intersect(searchbb)){
            List<Drawable> results = new ArrayList<>();
            if(node.wayList == null){
                results.addAll(searchTree(searchbb, node.left));
                results.addAll(searchTree(searchbb, node.right));
                    
            } else {
                results.addAll(node.wayList);
            }

            return results;
        } else
        return new ArrayList<>();
    }

}