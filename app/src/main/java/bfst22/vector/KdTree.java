package bfst22.vector;

import java.io.Serializable;
import java.util.*;

/**
 * The class {@code KdTree} represents a KdTree build up of KdNodes.
 * It contains three methods <em>buldKdTree</em>, <em>searchKdTree (public)</em>
 * and <em>searchKdTree (private)</em>.
 */
public class KdTree implements Serializable {
    BoundingBox bb; // BoundingBox for KdTree.
    KdNode root; // KdNode at the root of KdTree.
    List<Drawable> totalWays; // List of total OSMWays.

    /**
     * The constructor for KdTree. Initializes a KdTree with
     * {@code currentDepth = 0}.
     * Then builds the KdTree with the {@code buildKdTree} method.
     * 
     * @param ways A list of drawable ways.
     * @param root The root of a KdTree. It will build the KdTree from this node.
     * @param bb   Boundingbox for a KdTree. The BoundingBox is equal to the roots.
     *             BoundingBox since the root contains the list of drawable ways.
     */
    public KdTree(List<Drawable> ways) {
        this.totalWays = ways;
        root = buildKdTree(totalWays, 0);
        bb = root.bb;
    }

    /**
     * Returns KdNode(s) to add to KdTree.
     * 
     * @param ways         List of drawable ways.
     * @param currentDepth Current depth in the KdTree.
     * @return KdNode(s) to add to the KdTree.
     */
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
            node.left = buildKdTree(leftList, currentDepth + 1);
            node.right = buildKdTree(rightList, currentDepth + 1);
        } else {
            node.wayList = ways;
        }
        return node;

    }

    /**
     * Returns list of nodes within a certain BoundingBox.
     * Does this by calling the {@code private searchTree} method with the selected
     * BoundingBox
     * and the root node of the KdTree which the method called on.
     * 
     * @param bb BoundingBox you want nodes to be within.
     * @return List of nodes that intersect with BoundingBox bb.
     */
    public List<Drawable> searchTree(BoundingBox bb) {
        var results = searchTree(bb, root);
        return results;
    }

    /**
     * Returns list of nodes within a certain BoundingBox.
     * 
     * @param searchbb BoundingBox to search within.
     * @param node     Root of KdTree to start search from.
     * @return List of nodes that intersect with BoundingBox searchbb.
     */
    private List<Drawable> searchTree(BoundingBox searchbb, KdNode node) {

        if (node.bb.intersect(searchbb)) {
            List<Drawable> results = new ArrayList<>();
            if (node.wayList == null) {
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