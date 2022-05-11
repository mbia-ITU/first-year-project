package bfst22.vector;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;
import java.util.*;

public interface Drawable extends Serializable {
    static Comparator<Drawable> DrawableComparatorX = Comparator.comparing(list -> list.getBoundingBox().getCenterX());
    static Comparator<Drawable> DrawableComparatorY = Comparator.comparing(list -> list.getBoundingBox().getCenterY());

    default void draw(GraphicsContext gc) {
        gc.beginPath();
        trace(gc);
        gc.stroke();
    }

    default void fill(GraphicsContext gc) {
        gc.beginPath();
        trace(gc);
        gc.fill();
    }

    void trace(GraphicsContext gc);
    void resize(double zoomlevel);

    BoundingBox getBoundingBox();

    public static List<List<Drawable>> splitOnX(List<Drawable> list){
        List<Drawable> leftList;
        List<Drawable> rightList;

        Collections.sort(list, DrawableComparatorX);
        leftList = new ArrayList<>(list.subList(0, list.size() / 2));
        rightList = new ArrayList<>(list.subList(list.size() / 2, list.size()));
        List<List<Drawable>> listOfOSMWays = new ArrayList<List<Drawable>>();
        listOfOSMWays.add(leftList);
        listOfOSMWays.add(rightList);

        return listOfOSMWays; 
    
    }

    public static List<List<Drawable>> splitOnY(List<Drawable> list){
        List<Drawable> leftList;
        List<Drawable> rightList;

            Collections.sort(list, DrawableComparatorY);
            leftList = new ArrayList<>(list.subList(0, list.size() / 2));
            rightList = new ArrayList<>(list.subList(list.size() / 2, list.size()));
            List<List<Drawable>> listOfOSMWays = new ArrayList<List<Drawable>>();
            listOfOSMWays.add(leftList);
            listOfOSMWays.add(rightList);

            return listOfOSMWays; 
        
    }
}
