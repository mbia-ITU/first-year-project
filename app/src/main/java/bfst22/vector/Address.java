package bfst22.vector;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.Comparator;
import java.util.regex.Pattern;

/**
 * the class{@code Address} represesnts House and building adresses on the map, each address
 * has a streetname, a housenumber, a postalcode, a city and a {@code OSMNode} which represents the
 * location of the address. the strings of information allows us to search through a collection of addresses
 * and utilize Binary search to find the location of the address on the map, therefore we implement
 * {Comparable<>} & {Comparator<>}
 *
 * the class also implements {Serializable<>} such that the project can save the Address object into a .obj file
 *
 * the class also implements drawable, such that addresses can be marked on the map visibly.
 */

public class Address implements Comparable<Address>, Comparator<Address>, Drawable {
    public String street;
    public String city;
    public String postcode;
    public String housenumber;
    public OSMNode node;


    /**
     *
     * @param street is the name of the street associated with the address.
     * @param housenumber is the number of the house/apartment associated with the address.
     * @param postcode is the name of the street associated with the address.
     * @param city city is the name of the street associated with the address.
     * @param node is the location of the address on the map
     */
    public Address(String street, String housenumber, String postcode, String city, OSMNode node) {
        this.street = street;
        this.housenumber = housenumber;
        this.postcode = postcode;
        this.city = city;
        this.node = node;
    }

    /**
     *
     * @return the node of the address
     */
    public OSMNode getNode() {
        return node;
    }

    /**
     *
     * @return the combined address as commonly written in Danish
     */
    public String getAdress() {
        return getStreet() + " " + getHousenumber() + " " + getPostcode() + " " + getCity();
    }

    /**
     *
     * @return the name of the street
     */
    public String getStreet(){
        return this.street;
    }

    /**
     *
     * @return the name of the city
     */
    public String getCity(){
        return this.city;
    }

    /**
     *
     * @return the number of the house/apartment
     */
    public String getHousenumber(){
        return this.housenumber;
    }

    /**
     *
     * @return the postalcode of the address
     */
    public String getPostcode(){
        return this.postcode;
    }

    /**
     * Regular expression to create temporary address objects based on the input from
     * {@code Controller} with the purpose of finding a correctly associated Address
     * the {@code REGEX} distinguishes between the fields; {@code street, house, floor, side, postcode, city}
     * the fields {@code floor, side} are not used in this project due to the database not distinguishing between
     * the location, but if they were to be entered the Binary search should still find the desired address.
     */
    private final static String REGEX = "^(?<street>[A-ZÆØÅÉa-zæøåé ]+)(?<house>[0-9A-Z-]*)[ ,]* ?((?<floor>[0-9])?[,. ]* ?(?<side>[a-zæøå.,]+)??)?[ ]*(?<postcode>[0-9]{4})?[ ]*(?<city>[A-ZÆØÅa-zæøå ]*?)?$";
    private final static Pattern PATTERN = Pattern.compile(REGEX);

    /**
     *
     * @param input from searchfields in the {@code Controller class}
     * @return a temporarily built object to search through the total number of addresses
     */
    public static Address parse(String input) {
        var builder = new Builder();
        var matcher = PATTERN.matcher(input);
        if (matcher.matches()) {
            builder.street(matcher.group("street").trim());
            builder.house(matcher.group("house"));
            builder.postcode(matcher.group("postcode"));
            builder.city(matcher.group("city"));
            builder.floor(matcher.group("floor"));
            builder.side(matcher.group("side"));
        }
        return builder.build();
    }

    /**
     *
     * @param o the address derived from {@code parse} which will compare the address {o} to {this.Address}
     *
     * Compares incomplete address searches from {@code controller} based on the least amount of given information.
     *
     * @return an integer i, where i > 0 if the {Address o} has a higher lexicographical value than the selected address,
     * i < 0 if the {Address o} has a higher lexicographical value than the selected address,
     * and i = 0 if the strings are equal to one another
     *
     */
    @Override
    public int compareTo(Address o) {
        if(o.getHousenumber() == null){
            return (getStreet()).compareTo(o.getStreet());
        }else if(o.getPostcode() == null){
            return (getStreet() + " " + getHousenumber()).compareTo(o.getStreet() + " " + o.getHousenumber());
        }else if(o.getCity() == null){
            return (getStreet() + " " + getHousenumber() + " " + getPostcode()).compareTo(o.getStreet() + " " + o.getHousenumber() + " " + o.getPostcode());
        }

        return (getAdress()).compareTo(o.getAdress());
    }

    /**
     *
     * @param a1 Address
     * @param a2 Address
     * @return an integer i, where i > 0 if the {Address o} has a higher lexicographical value than the selected address,
     *      * i < 0 if the {Address o} has a higher lexicographical value than the selected address,
     *      * and i = 0 if the strings are equal to one another
     */
    @Override
    public int compare(Address a1, Address a2)
    {
        return a1.getAdress().compareTo(a2.getAdress());
    }


    /**
     * Inherits methods from the interface {@code Drawable}
     * @param gc Graphical context from {@code MapCanvas}
     */
    @Override
    public void draw(GraphicsContext gc) {
        Drawable.super.draw(gc);
    }

    @Override
    public void fill(GraphicsContext gc) {
        gc.setFill(Color.ORANGE);
        gc.fillOval(node.getLat(), node.getLon() ,4000,4000);
    }

    @Override
    public void trace(GraphicsContext gc) {

    }

    /**
     * Resizes the drawn Address based on zoomlevel
     * @param zoomlevel the current zoomlevel of the {@code MapCanvas}
     */
    @Override
    public void resize(double zoomlevel) {

    }

    /**
     *
     * @return the Boundingbox from the Address
     */
    @Override
    public BoundingBox getBoundingBox() {
        return node.getBoundingBox();
    }

    /**
     * The Builder Class serves to build temporary Address objects from parsed information
     */
    public static class Builder {
        private String street, house, floor, side, postcode, city;
        private OSMNode node;

        public Builder street(String _street) {
            street = _street;
            return this;
        }

        public Builder house(String _house) {
            house = _house;
            return this;
        }

        public Builder floor(String _floor) {
            floor = _floor;
            return this;
        }

        public Builder side(String _side) {
            side = _side;
            return this;
        }

        public Builder postcode(String _postcode) {
            postcode = _postcode;
            return this;
        }

        public Builder city(String _city) {
            city = _city;
            return this;
        }

        public Builder node(OSMNode _node) {
            node = _node;
            return this;
        }

        public Address build() {
            return new Address(street, house, postcode, city, node);
        }
    }

}

