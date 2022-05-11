package bfst22.vector;

import java.io.Serializable;
import java.util.*;

public class BoundingBox implements Serializable {
    float minX, maxX, minY, maxY;
    List<OSMWay> list;

    public BoundingBox(float minX, float maxX, float minY, float maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public BoundingBox (List<Drawable> drawables) {
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

    public float getCenterX() {
        return (minX + maxX) / 2;
    }

    public float getCenterY() {
        return (minY + maxY) / 2;
    }

    public boolean intersect(BoundingBox box) {
        if (this.maxX >= box.minX && this.maxY >= box.minY && box.maxX >= this.minX && box.maxY >= this.minY) {
            return true;
        } else {
            return false;
        }
    }

    public BoundingBox combineWith(BoundingBox box) {
        float minX = this.minX > box.minX ? box.minX : this.minX;
        float maxX = this.maxX > box.maxX ? this.maxX : box.maxX;
        float minY = this.minY > box.minY ? box.minY : this.minY;
        float maxY = this.maxY > box.maxY ? this.maxY : box.maxY;

        return new BoundingBox(minX, maxX, minY, maxY);
    }

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
