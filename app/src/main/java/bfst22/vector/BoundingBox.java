package bfst22.vector;

public class BoundingBox {
    float minX, maxX, minY, maxY;

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

    
}
