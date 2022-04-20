package bfst22.vector;

import java.util.*;

public class kdTree {
    BoundingBox box;
    kdNode root;

    public kdTree(List<OSMWay> ways) {
        root = buildKdTree(ways, 0);
        box = root.box;
    }

    public kdNode buildKdTree(List<OSMWay> ways, int currentDepth) {

        kdNode node = new kdNode(ways);

        if (ways.size() > 500) {
            List<List<OSMWay>> lists;
            if (currentDepth % 2 == 0) {
                lists = OSMWay.splitOnX(ways);
            } else {
                lists = OSMWay.splitOnY(ways);
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

    public List<OSMWay> searchTree(BoundingBox box, kdNode node) {

        if(node.box.intersect(box)){
            List<OSMWay> results = new ArrayList<>();
            if(node.wayList == null){
                results.addAll(searchTree(box, node.left));
                results.addAll(searchTree(box, node.right));
                    
            }
            return results;
        }
        else
        return new ArrayList<>(null);
        
    }

}
