package bfst22.vector;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.Comparator;
import java.util.regex.Pattern;

public class Address implements Comparable<Address>, Comparator<Address>, Drawable, Serializable {
    public String street;
    public String city;
    public String postcode;
    public String housenumber;
    public OSMNode node;

    public Address(String street, String housenumber, String postcode, String city, OSMNode node) {
        this.street = street;
        this.housenumber = housenumber;
        this.postcode = postcode;
        this.city = city;
        this.node = node;
    }

    public OSMNode getNode() {
        return node;
    }

    public String getAdress() {
        return getStreet() + " " + getHousenumber() + " " + getPostcode() + " " + getCity();
    }

    public String getStreet(){
        return this.street;
    }

    public String getCity(){
        return this.city;
    }

    public String getHousenumber(){
        return this.housenumber;
    }

    public String getPostcode(){
        return this.postcode;
    }

    private final static String REGEX = "^(?<street>[A-ZÆØÅÉa-zæøåé ]+)(?<house>[0-9A-Z-]*)[ ,]* ?((?<floor>[0-9])?[,. ]* ?(?<side>[a-zæøå.,]+)??)?[ ]*(?<postcode>[0-9]{4})?[ ]*(?<city>[A-ZÆØÅa-zæøå ]*?)?$";
    private final static Pattern PATTERN = Pattern.compile(REGEX);

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

    @Override
    public int compare(Address a1, Address a2)
    {
        return a1.getAdress().compareTo(a2.getAdress());
    }

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

    @Override
    public void resize(double zoomlevel) {

    }

    @Override
    public BoundingBox getBoundingBox() {
        return node.getBoundingBox();
    }


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

