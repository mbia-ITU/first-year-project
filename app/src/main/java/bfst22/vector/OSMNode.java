package bfst22.vector;

public class OSMNode {
    float lat, lon;

    public OSMNode(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public float getLat(){
         return lat;
    }

    public float getLon(){
        return lon;
   }
}
