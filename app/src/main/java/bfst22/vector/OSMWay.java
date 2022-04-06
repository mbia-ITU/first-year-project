package bfst22.vector;

import java.io.Serializable;
import java.util.*;

import javax.management.RuntimeErrorException;

import javafx.scene.canvas.GraphicsContext;

public class OSMWay implements Serializable {
    public static final long serialVersionUID = 42;
    List<OSMNode> nodes;
    float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, maxX = Float.MIN_VALUE, maxY = Float.MIN_VALUE;
    Comparator<OSMWay> OSMWayComparatorX = Comparator.comparing(list -> list.getPlotBounds().getCenterX());
    Comparator<OSMWay> OSMWayComparatorY = Comparator.comparing(list -> list.getPlotBounds().getCenterY());
    List<OSMWay> listOfOSMWays;
    
    public OSMWay(List<OSMNode> nodes) {
        this.nodes = new ArrayList<>(nodes);
    }
    
    public int size() {
        return nodes.size();
    }

    public BoundingBox getPlotBounds() {
        if(nodes.size() == 0){
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

    public List<List<OSMWay>> splitOnX(List<OSMWay> list){
        List<OSMWay> leftList;
        List<OSMWay> rightList;

        if (list.size() < 1000) {
            this.listOfOSMWays = list;
            List<List<OSMWay>> listOfOSMWays = new ArrayList<List<OSMWay>>();
            listOfOSMWays.add(this.listOfOSMWays);
            return listOfOSMWays;
        } else {
            Collections.sort(list, OSMWayComparatorX);
            leftList = list.subList(0, list.size() / 2);
            rightList = list.subList(list.size() / 2, list.size());
            List<List<OSMWay>> listOfOSMWays = new ArrayList<List<OSMWay>>();
            listOfOSMWays.add(leftList);
            listOfOSMWays.add(rightList);

            return listOfOSMWays; 
        }
    
    }

    public List<List<OSMWay>> splitOnY(List<OSMWay> list){
        List<OSMWay> leftList;
        List<OSMWay> rightList;

        if (list.size() < 1000) {
            this.listOfOSMWays = list;
            List<List<OSMWay>> listOfOSMWays = new ArrayList<List<OSMWay>>();
            listOfOSMWays.add(this.listOfOSMWays);
            return listOfOSMWays;
        } else {
            Collections.sort(list, OSMWayComparatorY);
            leftList = list.subList(0, list.size() / 2);
            rightList = list.subList(list.size() / 2, list.size());
            List<List<OSMWay>> listOfOSMWays = new ArrayList<List<OSMWay>>();
            listOfOSMWays.add(leftList);
            listOfOSMWays.add(rightList);

            return listOfOSMWays; 
        }
    
    }
}
