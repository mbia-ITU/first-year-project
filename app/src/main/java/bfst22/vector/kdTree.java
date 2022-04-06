package bfst22.vector;

import java.util.*;

public class kdTree {
    float leftNode, rightNode, root;
    int currentDepth;
    int maxDepth;

    List<OSMWay> list;
    List<BoundingBox> listOfBoundingBoxes;

    public kdTree() {
        maxDepth = list.size();

    }

    public void buildKdTree() {
        for (currentDepth = 0; currentDepth < maxDepth; currentDepth++) {
            if (currentDepth % 2 == 0) {
                /*
                 * split on x
                 */

            } else if (currentDepth % 2 == 1) {
                /*
                 * split on y
                 */
            } else if (currentDepth == maxDepth) {
                /*
                 * return listOfBoundingBoxes;
                 */
            }
            /*
             * BoundingBox.combineMany(list);
             * root = BoundingBox.getCenterX();
             * 
             * /*
             * kdNode(List<OSMWay> boolean splitonx){
             * BoundingBox.combineMany(list)
             * if (list.size < 1000){
             * elem = list;
             * }
             * 
             * else{
             * 
             * }
             * 
             * 
             * }
             */
        }

    }
}
