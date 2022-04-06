package bfst22.vector;

import java.util.*;

public class BoundingBox {
    float minX, maxX, minY, maxY;
    List<OSMWay> list;

    public BoundingBox(float minX, float maxX, float minY, float maxY){
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public float getCenterX(){
        return (minX + maxX)/2;
    }

    public float getCenterY(){
        return (minY + maxY)/2;
    }
    
    public boolean intersect(BoundingBox box){
        if(this.maxX >= box.minX && this.maxY >= box.minY && box.maxX >= this.minX && box.maxY >= this.minY){
            return true;
        }
        else{
        return false;
        }
    }

    public BoundingBox combineWith(BoundingBox box){
            float minX = this.minX > box.minX ? box.minX : this.minX;
            float maxX = this.maxX > box.maxX ? this.maxX : box.maxX;
            float minY = this.minY > box.minY ? box.minY : this.minY;
            float maxY = this.maxY > box.maxY ? this.maxY : box.maxY;

            return new BoundingBox(minX, maxX, minY, maxY);
    }

    public BoundingBox combineMany(List<OSMWay> list){
        float minX = this.minX;
        float maxX = this.maxX;
        float minY = this.minY;
        float maxY = this.maxY;
        for(OSMWay node : list){
            minX = minX > node.minX ? node.minX : minX;
            maxX = maxX > node.maxX ? maxX : node.maxX;
            minY = minY > node.minY ? node.minY : minY;
            maxY = maxY > node.maxY ? maxY : node.maxY;

        }
        return new BoundingBox(minX, maxX, minY, maxY);
    }
}
