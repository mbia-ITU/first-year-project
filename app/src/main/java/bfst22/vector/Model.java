package bfst22.vector;
import java.util.*;
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

/**
 * The Model Class is used for initialization and maintenance of objects that will be used
 * across all classes. the fields {minlat,minlon,maxlat,maxlon} represent the maximum and minimum coordinates
 * of the map.
 *
 * {@code addresses} is the collection of all addresses from the parsed file
 * {@code addrNode} is a placeholder for creating address objects that will be added to {@code addresses} along with
 * the fields {@code housenumber, street, postcode, city}
 *
 * {@code routeGraph} is an edgeweighted digraph that will be used to compute the shortest path between any two addresses
 *
 * {@code totalDrawables} is an Arraylist used to contain all drawable objects.
 *
 * {@code lines} is a map that maps all WayTypes to a list of drawable objects
 *
 * {@code mapOfKdTrees} is a map that maps all waytypes to a Kd-Tree
 *
 * {@code route} is an arraylist of {@code Polyline} that are used to draw the shortest path between addresses
 *
 */
public class Model {
    float minlat, minlon, maxlat, maxlon;
    ArrayList<Address> addresses = new ArrayList<>();
    OSMNode addrNode;
    String housenumber, street, postcode, city;

    EdgeWeightedDigraph routeGraph = new EdgeWeightedDigraph();

    static ArrayList<Drawable> totalDrawables = new ArrayList<>();
    Map<WayType,List<Drawable>> lines = new EnumMap<>(WayType.class); {
        for (var type : WayType.values()) lines.put(type, new ArrayList<>());
    }
    EnumMap<WayType, KdTree> mapOfKdTrees = new EnumMap<>(WayType.class);
    List<Runnable> observers = new ArrayList<>();
    ArrayList<Drawable> path = new ArrayList<>();

    /**
     * Initiates model with a file, the file must be of the type ".osm" and either be zipped or parsed as an ".obj" file
     * if the file is of the type ".osm" or ".zip" the {@code loadOSM()} method will be run to instantiate all the necessary objects
     *
     * if the file is of the toy ".obj" the registered objects will be read and instantiated much faster
     *
     * @param filename The name of the file to be read.
     * @throws IOException Throws InputOutputException in case the file can't be found or is invalid
     * @throws XMLStreamException Throws Exception in case the parsing has gone wrong
     * @throws FactoryConfigurationError Thrown when a problem with configuration with the Parser Factories exists
     * @throws ClassNotFoundException Throws Exception in case the class can't be found
     */
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
                addresses = (ArrayList<Address>) input.readObject();
                routeGraph = (EdgeWeightedDigraph) input.readObject();
                mapOfKdTrees = (EnumMap<WayType,KdTree>) input.readObject();
            }
        }
        time += System.nanoTime();
        System.out.println("Load time: " + (long)(time / 1e6) + " ms");
        if (!filename.endsWith(".obj")) save(filename);
    }

    /**
     * This method is run on startup and parses all objects used into a file of the type ".obj"
     * with the puropse of faster parsing on future startups
     *
     * @param basename the name of the original file that is parsed
     * @throws FileNotFoundException Thrown if the file is not found
     * @throws IOException Thrown if the objects can't be saved
     */
    public void save(String basename) throws FileNotFoundException, IOException {
        try (var out = new ObjectOutputStream(new FileOutputStream(basename + ".obj"))) {
            out.writeFloat(minlat);
            out.writeFloat(minlon);
            out.writeFloat(maxlat);
            out.writeFloat(maxlon);
            out.writeObject(lines);
            out.writeObject(addresses);
            out.writeObject(routeGraph);
            out.writeObject(mapOfKdTrees);
        }catch (IOException e){
            for(var s : mapOfKdTrees.entrySet()){
                System.out.println(s.toString());
            }
            e.printStackTrace();
        }
    }

    /**
     * this method runs through all OSM data in the input and initializes objects for the map
     *
     *
     * @param input inputStream from the selected file
     * @throws XMLStreamException Throws Exception in case the parsing has gone wrong
     * @throws FactoryConfigurationError Thrown when a problem with configuration with the Parser Factories exists
     */
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
                            addrNode = new OSMNode(id, 0.56f * lon, -lat);
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

                            //if(k.equals("name")) type = WayType.FOREST;
                            if (k.equals("natural") && v.equals("coastline")){ type = WayType.COASTLINE;

                            }

                            //if(k.equals("natural")) type =WayType.FOREST;

                            if (k.equals("natural") && v.equals("water")) type = WayType.LAKE;


                            if (k.equals("type") && v.equals("waterway")) type = WayType.RIVER;
                            if (k.equals("natural") && v.equals("heath")) type = WayType.HEATH;
                            if (k.equals("natural") && v.equals("beach")) type = WayType.BEACH;
                            if (k.equals("natural") && v.equals("wetland")) type = WayType.WETLAND;
                            if (k.equals("natural") && v.equals("sand")) type = WayType.SAND;
                            if (k.equals("natural") && v.equals("scrub")) type = WayType.SCRUB;
                            if (k.equals("natural") && v.equals("grassland")) type = WayType.GRASS;

                            //if (k.equals("natural") && v.equals("wood")) type = WayType.FOREST;





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


                            //roads
                            if(k.equals("highway")){ type = WayType.RESIDENTIALWAY;
                                nodesToGraph(nodes);
                            }

                            if (k.equals("highway") && v.equals("primary")) {
                                type = WayType.PRIMARYHIGHWAY;
                                nodesToGraph(nodes);
                            }

                            if (k.equals("highway") && v.equals("track")) {
                                type = WayType.MOTORWAY;
                                nodesToGraph(nodes);
                            }
                            //Roundabout:
                            if(k.equals("junction") && v.equals("roundabout")) {
                                type = WayType.RESIDENTIALWAY;
                                if(nodes.size()>0) {
                                    for (int i = 0; i < nodes.size() - 1; i++) {
                                        routeGraph.addEdge(nodes.get(i + 1), new DirectedEdge(nodes.get(i + 1), nodes.get(i)));
                                    }
                                    routeGraph.addEdge(nodes.get(0), new DirectedEdge(nodes.get(0), nodes.get(nodes.size() - 1)));
                                }
                            }


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
                                addresses.add(new Address(street,housenumber, postcode,city,addrNode));
                                street="";
                                city="";
                                postcode="";
                                housenumber="";
                            }

                            //irrelevant lines/nodes
                            if(k.equals("route") && v.equals("ferry")) type = WayType.UNKNOWN;
                            if(k.equals("type") && v.equals(("boundary"))) type = WayType.UNKNOWN;
                            if(k.equals("boundary") && v.equals("administrative")) type = WayType.UNKNOWN;
                            if(k.equals("highway") && v.equals("footway")) type = WayType.UNKNOWN;
                            if(k.equals("route") && v.equals("hiking")) type = WayType.UNKNOWN;
                            if(k.equals("cables")) type = WayType.UNKNOWN;
                            if(k.equals("boundary") && v.equals("administrative")) type = WayType.UNKNOWN;
                            if(k.equals("type") && v.equals("boundary")) type = WayType.UNKNOWN;
                            if(k.equals("maritime")) type = WayType.UNKNOWN;
                            if(k.equals("proposed") && v.equals("tunnel")) type = WayType.UNKNOWN;

                            break;
                        case "member":
                            ref = Long.parseLong(reader.getAttributeValue(null, "ref"));
                            var role = reader.getAttributeValue(null, "role");

                            var elm = id2way.get(ref);

                            if (elm != null)
                            {

                                rel.add(elm);
                            }

                            break;
                        case "relation":
                            id = Long.parseLong(reader.getAttributeValue(null, "id"));

                            break;

                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    switch (reader.getLocalName()) {
                        case "way":
                            var polyline = new PolyLine(nodes);

                            if(type == WayType.RESIDENTIALWAY){
                                lines.get(WayType.RESIDENTIALWAY).add(polyline);
                            }else if(type == WayType.PRIMARYHIGHWAY){
                                lines.get(WayType.PRIMARYHIGHWAY).add(polyline);
                                lines.get(WayType.RESIDENTIALWAY).add(polyline);
                            }else{
                                lines.get(type).add(polyline);
                            }
                            id2way.put(relID, new OSMWay(nodes, type));
                            lines.get(type).add(polyline);
                            totalDrawables.add(polyline);
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
                            if(type == WayType.PLACEHOLDER && !rel.isEmpty()){
                                lines.get(type).add(new MultiPolygon(rel));
                            }

                            rel.clear();
                            break;
                            
                    }
                    break;
            }
        }

        Collections.sort(addresses,Comparator.comparing(Address::getAdress));
        for (var entry : lines.entrySet()) {
            mapOfKdTrees.put(entry.getKey(), new KdTree(entry.getValue()));
        }

    }
    /**
     * 
     * @param observer
     */
    public void addObserver(Runnable observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
        for (var observer : observers) {
            observer.run();
        }
    }
    public void addStart(OSMNode node){
        var type = WayType.STARTPOINT;
        ArrayList<Drawable> start = new ArrayList<>();
        start.add(new AddressCircle(node));
        mapOfKdTrees.put(type, new KdTree(start));
    }

    public List<Drawable> getStart(){
        return lines.get(WayType.STARTPOINT);
    }

    public void clearStart(){
        lines.get(WayType.STARTPOINT).clear();
        notifyObservers();
    }

    public void addDestination(OSMNode node){
        var type = WayType.DESTINATION;
        ArrayList<Drawable> destination = new ArrayList<>();
        destination.add(new AddressCircle(node));
        mapOfKdTrees.put(type, new KdTree(destination));
    }

    public void clearDestination(){
        lines.get(WayType.DESTINATION).clear();
        notifyObservers();
    }

    public List<Drawable> getDestination(){
        return lines.get(WayType.DESTINATION);
    }

    /**
     * Returns lists of Drawables in a KdNode from a KdTree.
     * @param type Waytype of the KdTree to be searched.
     * @param bb BoundingBox to search within.
     * @return lists of Drawables.
     */
    public List<Drawable> getDrawablesFromTypeInBB(WayType type, BoundingBox bb) {
        return mapOfKdTrees.get(type).searchTree(bb);
    }

    /**
     * Returns Addresses.
     * @return list of addresses.
     */
    public ArrayList<Address> getAddresses(){
        return addresses;
    }

    /**
     * Returns list of all Drawables.
     * @return list of all Drawables.
     */
    public static List<Drawable> getDrawables(){
        return totalDrawables;
    }

    /**
     * Creates the route found by Dijkstra as a drawable Polyline
     * @param vertexes list of OSMNodes found by Dijkstra shortestpath algorithm.
     */
    public void addRoute(ArrayList<OSMNode> vertexes){
        if(mapOfKdTrees.containsKey(WayType.PATHTO)){
            mapOfKdTrees.remove(WayType.PATHTO);
            path.clear();
        }
        path.add(new PolyLine(vertexes));
        mapOfKdTrees.put(WayType.PATHTO, new KdTree(path));
    }

    /**
     * Adds the searched addresses to the graph to be used in the Shortest path algorithm
     *
     * @param nodes nodes from the shortest path that has to be drawn from the Dijkstra shortestpath algorithm
     *
     */
    public void nodesToGraph(List<OSMNode> nodes){
        for(int i = 0; i < nodes.size()-1; i++){
            routeGraph.addEdge(nodes.get(i), new DirectedEdge(nodes.get(i), nodes.get(i+1)));
            routeGraph.addEdge(nodes.get(i+1), new DirectedEdge(nodes.get(i+1), nodes.get(i)));
        }
    }



}
