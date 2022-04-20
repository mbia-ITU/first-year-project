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
    ArrayList<String> addresses = new ArrayList<>();
    String housenumber = "";
    String street = "";
    String postcode = "";
    String city = "";
    String floor = "";
    String side = "";
    static ArrayList<OSMWay> rel = new ArrayList<OSMWay>();

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
                            //if (k.equals("waterway") && v.equals("river")) type = WayType.RIVER;
                            if (k.equals("type") && v.equals("waterway")) type = WayType.RIVER;
                            if (k.equals("natural") && v.equals("heath")) type = WayType.HEATH;
                            if (k.equals("natural") && v.equals("beach")) type = WayType.BEACH;
                            if (k.equals("natural") && v.equals("wetland")) type = WayType.WETLAND;
                            if (k.equals("natural") && v.equals("sand")) type = WayType.SAND;
                            if (k.equals("natural") && v.equals("scrub")) type = WayType.SCRUB;
                            if (k.equals("natural") && v.equals("grassland")) type = WayType.GRASS;
                            //if (k.equals("natural") && v.equals("coastline")) type = WayType.COASTLINE;
                            if (k.equals("natural") && v.equals("wood")) type = WayType.FOREST;
                            if (k.equals("leisure") && v.equals("park")) type = WayType.GRASS;
                            if (k.equals("leisure") && v.equals("pitch")) type = WayType.PITCH;
                            if (k.equals("leisure") && v.equals("golf_course")) type = WayType.GOLFCOURSE;
                            if (k.equals("leisure") && v.equals("miniature_golf")) type = WayType.GOLFCOURSE;
                            if (k.equals("leisure") && v.equals("nature_reserve")) type = WayType.RESERVE;
                            if (k.equals("leisure") && v.equals("sports_centre")) type = WayType.RACE;
                            if (k.equals("leisure") && v.equals("playground")) type = WayType.RACE;
                            if (k.equals("building") && v.equals("yes")) type = WayType.BUILDING;
                            if (k.equals("building") && v.equals("retail")) type = WayType.BUILDING;
                            if (k.equals("building") && v.equals("industrial")) type = WayType.BUILDING;
                            if (k.equals("building") && v.equals("public")) type = WayType.BUILDING;
                            if (k.equals("building") && v.equals("school")) type = WayType.BUILDING;
                            if (k.equals("building") && v.equals("office")) type = WayType.BUILDING;
                            if (k.equals("building") && v.equals("detached")) type = WayType.BUILDING;
                            if (k.equals("building") && v.equals("bungalow")) type = WayType.BUILDING;
                            if (k.equals("landuse") && v.equals("forest")) type = WayType.FOREST;
                            if (k.equals("landuse") && v.equals("farmyard")) type = WayType.FARMYARD;
                            if (k.equals("landuse") && v.equals("farmland")) type = WayType.FARMLAND;
                            if (k.equals("landuse") && v.equals("residential")) type = WayType.RESIDENTIAL;
                            if (k.equals("landuse") && v.equals("industrial")) type = WayType.INDUSTRIAL;
                            if (k.equals("landuse") && v.equals("grass")) type = WayType.GRASS;
                            if (k.equals("landuse") && v.equals("meadow")) type = WayType.MEADOW;
                            if (k.equals("landuse") && v.equals("scrub")) type = WayType.SCRUB;
                            if (k.equals("landuse") && v.equals("cemetery")) type = WayType.CEMETERY;
                            if (k.equals("landuse") && v.equals("quarry")) type = WayType.INDUSTRIAL;
                            if (k.equals("landuse") && v.equals("vineyard")) type = WayType.VINEYARD;
                            if (k.equals("highway") && v.equals("tertiary")) type = WayType.TERTIARY;
                            if (k.equals("highway") && v.equals("raceway")) type = WayType.RACEWAY;
                            //if (k.equals("highway") && v.equals("primary")) type = WayType.PRIMARYHIGHWAY;
                            if (k.equals("sport") && v.equals("soccer")) type = WayType.SOCCER;
                            if (k.equals("amenity") && v.equals("parking")) type = WayType.PARKING;
                            if (k.equals("amenity") && v.equals("hospital")) type = WayType.HOSPITAL;
                            if (k.equals("amenity") && v.equals("kindergarten")) type = WayType.HOSPITAL;
                            if (k.equals("amenity") && v.equals("grave_yard")) type = WayType.CEMETERY;
                            if (k.equals("aeroway") && v.equals("helipad")) type = WayType.HELIPAD;
                            if (k.equals("aeroway") && v.equals("apron")) type = WayType.APRON;
                            if (k.equals("aerodrome") && v.equals("public")) type = WayType.AIRPORT;
                            if (k.equals("area") && v.equals("yes")) type = WayType.GRASS;
                            if (k.equals("boundary") && v.equals("protected_area")) type = WayType.PROTECTEDAREA;
                            if (k.equals("tourism") && v.equals("resort")) type = WayType.RESORT;
                            if (k.equals("power") && v.equals("generator")) type = WayType.INDUSTRIAL;
                            if (k.equals("caravans") && v.equals("yes")) type = WayType.GOLFCOURSE;
                            if (k.equals("man_made") && v.equals("wastewater_plant")) type = WayType.INDUSTRIAL;

                            if (k.equals("addr:housenumber")){housenumber = v;}
                            if (k.equals("addr:city")){city=v;}
                            if (k.equals("addr:postcode")){postcode=v;}
                            if (k.equals("addr:street")){street=v;

                                addresses.add(street + " " +housenumber + " " +postcode + " " + city);
                                street="";
                                city="";
                                postcode="";
                                housenumber="";
                            }
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
                            if (type == WayType.GOLFCOURSE && !rel.isEmpty()) {
                            lines.get(type).add(new MultiPolygon(rel));
                            }
                            if (type == WayType.FARMLAND && !rel.isEmpty()) {
                                lines.get(type).add(new MultiPolygon(rel));
                            }
                            if (type == WayType.FARMYARD && !rel.isEmpty()) {
                                lines.get(type).add(new MultiPolygon(rel));
                            }
                            
                            if (type == WayType.LAKE && !rel.isEmpty()) {
                                lines.get(type).add(new MultiPolygon(rel));
                            }
                            if (type == WayType.MEADOW && !rel.isEmpty()) {
                                lines.get(type).add(new MultiPolygon(rel));
                            }
                            /*
                            if (type == WayType.GRASS && !rel.isEmpty()) {
                                lines.get(type).add(new MultiPolygon(rel));
                            }
                            if (type == WayType.BUILDING && !rel.isEmpty()) {
                                lines.get(type).add(new MultiPolygon(rel));
                            }
                            if (type == WayType.BEACH && !rel.isEmpty()) {
                                lines.get(type).add(new MultiPolygon(rel));
                            }
                            */
                            if (type == WayType.SAND && !rel.isEmpty()) {
                                lines.get(type).add(new MultiPolygon(rel));
                            }
                            if (type == WayType.WETLAND && !rel.isEmpty()) {
                                lines.get(type).add(new MultiPolygon(rel));
                            }
                            if (type == WayType.FOREST && !rel.isEmpty()) {
                                lines.get(type).add(new MultiPolygon(rel));
                            }
                            
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

    public ArrayList<String> getAddresses(){
        return addresses;
    }

    public static List<OSMWay> getNodes(){
        return rel;
    }

}
