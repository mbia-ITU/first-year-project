package bfst22.vector;

import java.io.Serializable;
import java.util.*;

public class KdTree implements Serializable {
    BoundingBox box;
    KdNode root;
    List<Drawable> totalWays;

    public KdTree(List<Drawable> ways) {
        this.totalWays = ways;
        root = buildKdTree(totalWays, 0);
        box = root.box;
    }

    public KdNode buildKdTree(List<Drawable> ways, int currentDepth) {

        KdNode node = new KdNode(ways);

        if (ways.size() > 500) {
            List<List<Drawable>> lists;
            if (currentDepth % 2 == 0) {
                lists = Drawable.splitOnX(ways);
            } else {
                lists = Drawable.splitOnY(ways);
            }

            var leftList = lists.get(0);
            var rightList = lists.get(1);
            node.left = buildKdTree(leftList, currentDepth + 1);
            node.right = buildKdTree(rightList, currentDepth + 1);

        } else {
            node.wayList = ways;
        }
        return node;

    }

    public List<Drawable> searchTree(BoundingBox box) {
        //kald næste searchtree med en ny arrayliste
        //giv den arrayliste med hver gang den anden searchtree bliver kaldt
        return searchTree(box, root);
    }

    private List<Drawable> searchTree(BoundingBox box, KdNode node) {

        if(node.box.intersect(box)){
            List<Drawable> results = new ArrayList<>();
            if(node.wayList == null){
                results.addAll(searchTree(box, node.left));
                results.addAll(searchTree(box, node.right));
                    
            }
            return results;
        }
        else
        return new ArrayList<>();
        
    }
}
