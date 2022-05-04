package bfst22.vector;

import java.io.Serializable;
import java.util.*;

public class KdTree implements Serializable {
    BoundingBox box;
    KdNode root;
    List<OSMWay> totalWays;

    public KdTree(List<OSMWay> ways) {
        this.totalWays = ways;
        root = buildKdTree(totalWays, 0);
        box = root.box;
    }

    public KdNode buildKdTree(List<OSMWay> ways, int currentDepth) {

        KdNode node = new KdNode(ways);

        if (ways.size() > 500) {
            List<List<OSMWay>> lists;
            if (currentDepth % 2 == 0) {
                lists = OSMWay.splitOnX(ways);
            } else {
                lists = OSMWay.splitOnY(ways);
            }
            //System.out.println(ways.size());
            var leftList = lists.get(0);
            var rightList = lists.get(1);
            //System.out.println("l: " + leftList.size() + " r: " + rightList.size());
            node.left = buildKdTree(leftList, currentDepth + 1);
            node.right = buildKdTree(rightList, currentDepth + 1);

        } else {
            node.wayList = ways;
        }
        return node;

    }

    public List<OSMWay> searchTree(BoundingBox box) {
        //kald næste searchtree med en ny arrayliste
        //giv den arrayliste med hver gang den anden searchtree bliver kaldt
        return searchTree(box, root);
    }

    private List<OSMWay> searchTree(BoundingBox box, KdNode node) {

        if(node.box.intersect(box)){
            List<OSMWay> results = new ArrayList<>();
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
