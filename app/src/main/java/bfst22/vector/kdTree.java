package bfst22.vector;

import java.util.*;

public class kdTree {
    int maxDepth;
    BoundingBox box;

    List<OSMWay> ways;

    public kdTree(List<OSMWay> ways, int maxDepth) {
        this.maxDepth = maxDepth;
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

    public kdNode searchTree(List<OSMWay> ways, BoundingBox box) {
        kdNode leftNode = new kdNode(ways);
        kdNode rightNode = new kdNode(ways);

        if(leftNode.getPlotBounds().intersect(box)){
            //search left
        }
        if(rightNode.getPlotBounds().intersect(box)){
            //search right
        }

        return null;
    }

}
