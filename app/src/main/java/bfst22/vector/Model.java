package bfst22.vector;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static java.util.stream.Collectors.toList;

public class Model implements Iterable<Drawable> {
    float minlat, minlon, maxlat, maxlon;
    List<Drawable> lines = new ArrayList<>();
    List<Drawable> line = new ArrayList<>();
    List<Runnable> observers = new ArrayList<>();

    public Model(String filename) throws IOException, XMLStreamException, FactoryConfigurationError {
        if (filename.endsWith(".osm")) {
            loadOSM(filename);
        } else {
            lines = Files.lines(Paths.get(filename))
                .map(Line::new)
                .collect(toList());
        }
    }

    private void loadOSM(String filename) throws FileNotFoundException, XMLStreamException, FactoryConfigurationError {
        var reader = XMLInputFactory.newInstance().createXMLStreamReader(new BufferedInputStream(new FileInputStream(filename)));
        var id2node = new HashMap<Long, OSMNode>();
        var nodes = new ArrayList<OSMNode>();
        Type type = Type.UNKNOWN;
        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    var name = reader.getLocalName();
                    switch (name) {
                        case "bounds":
                            maxlat = -Float.parseFloat(reader.getAttributeValue(null, "minlat"));
                            minlon = 0.56f * Float.parseFloat(reader.getAttributeValue(null, "minlon"));
                            minlat = -Float.parseFloat(reader.getAttributeValue(null, "maxlat"));
                            maxlon = 0.56f * Float.parseFloat(reader.getAttributeValue(null, "maxlon"));
                            break;
                        case "node":
                            var id = Long.parseLong(reader.getAttributeValue(null, "id"));
                            var lat = Float.parseFloat(reader.getAttributeValue(null, "lat"));
                            var lon = Float.parseFloat(reader.getAttributeValue(null, "lon"));
                            id2node.put(id, new OSMNode(0.56f * lon, -lat));
                            break;
                        case "nd":
                            var ref = Long.parseLong(reader.getAttributeValue(null, "ref"));
                            nodes.add(id2node.get(ref));
                            break;
                        case "tag":
                            var k = reader.getAttributeValue(null, "k");
                            var v = reader.getAttributeValue(null, "v");
                            switch(k){
                                case "natural":
                                 switch(v){
                                    case "water":
                                        type = Type.WATER;
                                    }
                                break;

                                case "building":
                                    type = Type.BUILDING;

                                case "landuse":
                                    switch(v){
                                        case "grass":
                                            type = Type.GRASS;

                                        case "forest":
                                            type = Type.FOREST;
                                        
                                    }
                                break;
                            }
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    switch (reader.getLocalName()) {
                        case "way":
                            // if (!nodes.isEmpty()) {
                                var way = new OSMWay(nodes, type);
                                line.add(way);
                                nodes.clear();
                                type = Type.UNKNOWN;
                            // }
                            break;
                    }
                    break;
            }
        }
        kdTree(0, line);

        
    }

    public void addObserver(Runnable observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
        for (var observer : observers) {
            observer.run();
        }
    }

    @Override
    public Iterator<Drawable> iterator() {
        return lines.iterator();
    }

    public void add(Line line) {
        lines.add(line);
        notifyObservers();
    }
    
    public List<Drawable> kdTree(int depth, List<Drawable> points){
        
        if(points.size() <= 1000){
            for (Drawable drawable : points) {
                lines.add(drawable);
            }
        }
        else {
            List<Drawable> sortedList;
            List<Drawable> leftTree = new ArrayList<>();
            List<Drawable> rightTree = new ArrayList<>();
            
           // if(depth % 2 == 0){    
                
                sortedList = shittySort(depth, points);

                for (int i = 0; i < points.size(); i++) {
                    if(i < Math.round(points.size() / 2)){
                        leftTree.add(points.get(i));
                    }
                    else{
                        rightTree.add(points.get(i));
                    }
                }
            
                kdTree(depth + 1, leftTree);
                //}    
           
           
           
           
            kdTree(depth + 1, leftTree);
                
            
        }
        return null;
        
    }
    
    //THIS NEED TO BE FIXED ASAP - Tim (The guy who wrote this shit shitty sort)
    private List<Drawable> shittySort(int depth, List<Drawable> unsortedList){
        int currentLowest = 0;
        float current = 0;
        List<Drawable> sortedList = new ArrayList<>();

        if(depth % 2 == 0){
            

            for (int i = 0; i < unsortedList.size(); i++) {
                current = unsortedList.get(0).getLat();
                currentLowest = 0;
                for (int j = 0; j < unsortedList.size(); j++) {
                    if(current > unsortedList.get(j).getLat()){
                        current = unsortedList.get(j).getLat();
                        currentLowest = j; 
                    }
                }
                sortedList.add(unsortedList.get(currentLowest));
                unsortedList.remove(currentLowest);
            }
            return sortedList;
        }
        else{
            

            for (int i = 0; i < unsortedList.size(); i++) {
                current = unsortedList.get(0).getLon();
                currentLowest = 0;
                for (int j = 0; j < unsortedList.size(); j++) {
                    if(current > unsortedList.get(j).getLon()){
                        current = unsortedList.get(j).getLon();
                        currentLowest = j; 
                    }
                }
                sortedList.add(unsortedList.get(currentLowest));
                unsortedList.remove(currentLowest);
                
            }
            
            return sortedList;
        }
        



        
    }



}
