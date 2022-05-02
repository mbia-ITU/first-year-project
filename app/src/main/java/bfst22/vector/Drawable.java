package bfst22.vector;

import javafx.scene.canvas.GraphicsContext;

import java.util.*;

public interface Drawable {
    static float minX = Float.MAX_VALUE;
    static float minY = Float.MAX_VALUE;
    static float maxX = Float.MIN_VALUE;
    static float maxY = Float.MIN_VALUE;
    static Comparator<Drawable> DrawableComparatorX = Comparator.comparing(list -> list.getPlotBounds().getCenterX());
    static Comparator<Drawable> DrawableComparatorY = Comparator.comparing(list -> list.getPlotBounds().getCenterY());
    List<OSMNode> nodes;

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

    default BoundingBox getBoundingBox() {
        return null;
    }

    public default BoundingBox getPlotBounds() {
        if (nodes.size() == 0) {
            throw new RuntimeException("OSMWay does not have any nodes");
        }

        for (OSMNode node : nodes) {
            minX = Math.min(minX, node.getLon());
            minY = Math.min(minY, node.getLat());
            maxX = Math.max(maxX, node.getLon());
            maxY = Math.max(maxY, node.getLat());
        }

        return new BoundingBox(minX, maxX, minY, maxY);
    }

    public static List<List<Drawable>> splitOnX(List<Drawable> list){
        List<Drawable> leftList;
        List<Drawable> rightList;

        Collections.sort(list, DrawableComparatorX);
        leftList = list.subList(0, list.size() / 2);
        rightList = list.subList(list.size() / 2, list.size());
        List<List<Drawable>> listOfDrawables = new ArrayList<List<Drawable>>();
        listOfDrawables.add(leftList);
        listOfDrawables.add(rightList);

        return listOfDrawables; 
    
    }

    public static List<List<Drawable>> splitOnY(List<Drawable> list){
        List<Drawable> leftList;
        List<Drawable> rightList;

            Collections.sort(list, DrawableComparatorY);
            leftList = list.subList(0, list.size() / 2);
            rightList = list.subList(list.size() / 2, list.size());
            List<List<Drawable>> listOfDrawables = new ArrayList<List<Drawable>>();
            listOfDrawables.add(leftList);
            listOfDrawables.add(rightList);

            return listOfDrawables; 
        
    }
}
