package bfst22.vector;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public enum Type {
    WATER,BUILDING,GRASS,FOREST,UNKNOWN;

    public static Paint getColor(Type type){
        switch(type){
            case WATER:
            return Color.valueOf("0000ff");

            case BUILDING:
            return Color.valueOf("ff0000");

            case GRASS:
            return Color.valueOf("7CFC00");

            case FOREST:
            return Color.valueOf("228B22");

            default:
            return Color.BLACK;
        }
    }    
}
