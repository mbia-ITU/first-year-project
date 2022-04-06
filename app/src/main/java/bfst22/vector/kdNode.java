package bfst22.vector;

import java.util.*;

public class kdNode {
    float split;
    kdNode left, right;
    BoundingBox box;
    
    public kdNode(List<OSMWay> list) {
        box = box.combineMany(list);
    }
<<<<<<< HEAD

    public List<List<OSMWay>> splitOnX(List<OSMWay> list) {
        List<OSMWay> leftList;
        List<OSMWay> rightList;

        if (list.size() < 500) {
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

    public List<List<OSMWay>> splitOnY(List<OSMWay> list) {
        List<OSMWay> leftList;
        List<OSMWay> rightList;

        if (list.size() < 500) {
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
=======
>>>>>>> 8955565162f8c30bba6af4a06a1f775a91f5365e
}
 