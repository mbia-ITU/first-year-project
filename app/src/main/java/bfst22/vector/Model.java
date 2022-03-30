package bfst22.vector;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static java.util.stream.Collectors.toList;

public class Model {
    float minlat, minlon, maxlat, maxlon;
    Map<WayType,List<Drawable>> lines = new EnumMap<>(WayType.class); {
        for (var type : WayType.values()) lines.put(type, new ArrayList<>());
    }
    List<Runnable> observers = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public Model(String filename) throws IOException, XMLStreamException, FactoryConfigurationError, ClassNotFoundException {
        var time = -System.nanoTime();
        if (filename.endsWith(".zip")) {
            var zip = new ZipInputStream(new FileInputStream(filename));
            zip.getNextEntry();
            loadOSM(zip);
        } else if (filename.endsWith(".osm")) {
            loadOSM(new FileInputStream(filename));
        } else if (filename.endsWith(".obj")) {
            try (var input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
                minlat = input.readFloat();
                minlon = input.readFloat();
                maxlat = input.readFloat();
                maxlon = input.readFloat();
                lines = (Map<WayType,List<Drawable>>) input.readObject();
            }
        } else {
            lines.put(WayType.UNKNOWN, Files.lines(Paths.get(filename))
                .map(Line::new)
                .collect(toList()));
        }
        time += System.nanoTime();
        System.out.println("Load time: " + (long)(time / 1e6) + " ms");
        if (!filename.endsWith(".obj")) save(filename);
    }

    public void save(String basename) throws FileNotFoundException, IOException {
        try (var out = new ObjectOutputStream(new FileOutputStream(basename + ".obj"))) {
            out.writeFloat(minlat);
            out.writeFloat(minlon);
            out.writeFloat(maxlat);
            out.writeFloat(maxlon);
            out.writeObject(lines);
        }
    }

    private void loadOSM(InputStream input) throws XMLStreamException, FactoryConfigurationError {
        var reader = XMLInputFactory.newInstance().createXMLStreamReader(new BufferedInputStream(input));
        var id2node = new NodeMap();
        var id2way = new HashMap<Long, OSMWay>();
        var nodes = new ArrayList<OSMNode>();
        var rel = new ArrayList<OSMWay>();
        long relID = 0;
        var type = WayType.UNKNOWN;
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
                            id2node.add(new OSMNode(id, 0.56f * lon, -lat));
                            break;
                        case "nd":
                            var ref = Long.parseLong(reader.getAttributeValue(null, "ref"));
                            nodes.add(id2node.get(ref));
                            break;
                        case "way":
                            relID = Long.parseLong(reader.getAttributeValue(null, "id"));
                            type = WayType.UNKNOWN;
                            break;
                        case "tag":
                            var k = reader.getAttributeValue(null, "k");
                            var v = reader.getAttributeValue(null, "v");
                            if (k.equals("natural") && v.equals("water")) type = WayType.LAKE;
                            if (k.equals("leisure") && v.equals("park")) type = WayType.GRASS;
                            if (k.equals("building") && v.equals("yes")) type = WayType.BUILDING;
                            if (k.equals("landuse") && v.equals("forest")) type = WayType.FOREST;
                            break;
                        case "member":
                            ref = Long.parseLong(reader.getAttributeValue(null, "ref"));
                            var elm = id2way.get(ref);
                            if (elm != null) rel.add(elm);
                            break;
                        case "relation":
                            id = Long.parseLong(reader.getAttributeValue(null, "id"));
                            if (id == 1305702) {
                                System.out.println("Done");
                            }
                            break;

                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    switch (reader.getLocalName()) {
                        case "way":
                            var way = new PolyLine(nodes);
                            id2way.put(relID, new OSMWay(nodes));
                            lines.get(type).add(way);
                            nodes.clear();
                            break;
                        case "relation":
                            /*if (type == WayType.LAKE && !rel.isEmpty()) {
                                lines.get(type).add(new MultiPolygon(rel));
                            }
                            if (type == WayType.GRASS && !rel.isEmpty()) {
                                lines.get(type).add(new MultiPolygon(rel));
                            }
                            if (type == WayType.BUILDING && !rel.isEmpty()) {
                                lines.get(type).add(new MultiPolygon(rel));
                            }
                            if (type == WayType.FOREST && !rel.isEmpty()) {
                                lines.get(type).add(new MultiPolygon(rel));
                            }*/
                            rel.clear();
                            break;
                            
                    }
                    break;
            }
        }
        System.out.println("Done");
    }

    public void addObserver(Runnable observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
        for (var observer : observers) {
            observer.run();
        }
    }

    public Iterable<Drawable> iterable(WayType type) {
        return lines.get(type);
    }
}
