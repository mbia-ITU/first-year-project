package bfst22.vector;

import javafx.scene.paint.Color;

public enum WayType {
    UNKNOWN, LAKE, GRASS, BUILDING, RESIDENTIAL, FOREST, FARMLAND, PITCH, 
    SOCCER, PARKING, INDUSTRIAL, FARMYARD, GOLFCOURSE, PROTECTEDAREA, HEATH, 
    BEACH, RESERVE, RESORT, COASTLINE, CEMETERY, HOSPITAL, RIVER, WETLAND, 
    SAND, SCRUB, TERTIARY, MEADOW, HELIPAD, AIRPORT, PRIMARYHIGHWAY, RACEWAY, 
    RACE, APRON, VINEYARD;
   

    public Color color(){
        if(WayType.UNKNOWN == COASTLINE){
            return Color.PINK;
        }
        if(WayType.UNKNOWN == FOREST){
            return Color.DARKGREEN;
        }
        if(WayType.UNKNOWN == FARMLAND){
            return Color.KHAKI;
        }
        if(WayType.UNKNOWN == RESIDENTIAL){
            return Color.LIGHTGRAY;
        }        
        if(WayType.UNKNOWN == HEATH){
            return Color.TAN;
        }
        if(WayType.UNKNOWN == BEACH){
            return Color.MOCCASIN;
        }
        if(WayType.UNKNOWN == SAND){
            return Color.OLDLACE;
        }
        if(WayType.UNKNOWN == WETLAND){
            return Color.TEAL;
        }
        if(WayType.UNKNOWN == FARMYARD){
            return Color.PEACHPUFF;
        }
        if(WayType.UNKNOWN == INDUSTRIAL){
            return Color.PLUM;
        }
        if(WayType.UNKNOWN == GOLFCOURSE){
            return Color.HONEYDEW;
        }

        else
        return Color.WHITE;
    }
    
}