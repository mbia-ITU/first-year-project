package bfst22.vector;

import java.util.*;

public class kdTree {
    float leftNode, rightNode, root;
    int currentDepth;
    int maxDepth;

    List<OSMWay> ways;

    public kdTree(List<OSMWay> ways, int maxDepth) {
        root = buildKdTree(ways);
        this.maxDepth = maxDepth;
        currentDepth = 0;
    }

    public kdNode buildKdTree(List<OSMWay> ways) {
        
        kdNode node = new kdNode(ways);

        for (int i = 0; i < maxDepth; i++) {
            if (ways.size() < 1000) {
                node.left = buildKdTree(leftNode, OSMWay.splitOnX(ways, maxDepth));
                node.right = buildKdTree(rightNode, OSMWay.splitOnY(ways, maxDepth));
                return node;
                
            }
            else if (currentDepth % 2 == 0) {
                currentDepth++;
            } else if (currentDepth % 2 == 1) {
                currentDepth++;
            }
        }
        return null;
        
    }
}
