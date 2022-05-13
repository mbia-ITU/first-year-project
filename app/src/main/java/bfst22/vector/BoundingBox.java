package bfst22.vector;

import java.io.Serializable;
import java.util.*;

/**
 * The class {@code BoundingBox} represents a BoundingBox of a drawable element
 * like {@code Polyline} or {@code MultiPolygon}.
 */
public class BoundingBox implements Serializable {
    float minX, maxX, minY, maxY;
    List<OSMWay> list;

    /**
     * Initializes a BoundingBox from a set of coordinates.
     * 
     * @param minX x-coord lowerright corner
     * @param maxX x-coord topleft corner
     * @param minY y-coord lowerright corner
     * @param maxY x-coord topleft corner
     */
    public BoundingBox(float minX, float maxX, float minY, float maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    /**
     * Initializes a BoundingBox from a list of drawable nodes.
     * 
     * @param drawables List of drawable nodes.
     */
    public BoundingBox(List<Drawable> drawables) {
        minX = Float.POSITIVE_INFINITY;
        maxX = Float.NEGATIVE_INFINITY;
        minY = Float.POSITIVE_INFINITY;
        maxY = Float.NEGATIVE_INFINITY;
        for (Drawable node : drawables) {
            BoundingBox bb = node.getBoundingBox();
            minX = minX > bb.minX ? bb.minX : minX;
            maxX = maxX > bb.maxX ? maxX : bb.maxX;
            minY = minY > bb.minY ? bb.minY : minY;
            maxY = maxY > bb.maxY ? maxY : bb.maxY;

        }
    }

    /**
     * Returns center of BoundingBox on the x-axis.
     * 
     * @return center of BoundingBox on the x-axis.
     */
    public float getCenterX() {
        return (minX + maxX) / 2;
    }

    /**
     * Returns center of BoundingBox on the y-axis.
     * 
     * @return center of BoundingBox on the y-axis.
     */
    public float getCenterY() {
        return (minY + maxY) / 2;
    }

    /**
     * Checks if two BoundingBoxes intersect eachother.
     * 
     * @param box BoundingBox you want to check if intersects
     * @return {@code true} If the BoundingBoxes intersect;
     *         {@code false} otherwise
     */
    public boolean intersect(BoundingBox box) {
        if (this.maxX >= box.minX && this.maxY >= box.minY && box.maxX >= this.minX && box.maxY >= this.minY) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns a new BoudningBox with combined area of two BoundingBoxes.
     * 
     * @param box BoundingBox you want to combine with.
     * @return new BoudningBox with combined area of the two BoundingBoxes.
     */
    public BoundingBox combineWith(BoundingBox box) {
        float minX = this.minX > box.minX ? box.minX : this.minX;
        float maxX = this.maxX > box.maxX ? this.maxX : box.maxX;
        float minY = this.minY > box.minY ? box.minY : this.minY;
        float maxY = this.maxY > box.maxY ? this.maxY : box.maxY;

        return new BoundingBox(minX, maxX, minY, maxY);
    }

    /**
     * Takes a list of drawable nodes and creates BoundingBoxes for each node.
     * Then is returns a new BoundingBox with the total area of all BoundingBoxes
     * created.
     * 
     * @param list List of drawable nodes.
     * @return new BoundingBox with area of all combined nodes' BoundingBoxes.
     */
    public BoundingBox combineMany(List<Drawable> list) {
        float minX = this.minX;
        float maxX = this.maxX;
        float minY = this.minY;
        float maxY = this.maxY;
        for (Drawable node : list) {
            BoundingBox bb = node.getBoundingBox();
            minX = minX > bb.minX ? bb.minX : minX;
            maxX = maxX > bb.maxX ? maxX : bb.maxX;
            minY = minY > bb.minY ? bb.minY : minY;
            maxY = maxY > bb.maxY ? maxY : bb.maxY;

        }
        return new BoundingBox(minX, maxX, minY, maxY);
    }
}
