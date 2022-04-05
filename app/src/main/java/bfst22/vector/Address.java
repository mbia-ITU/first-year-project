package bfst22.vector;

public class Address{
    public final String street, house, floor, side, postcode, city;

    public Address(
            String _street, String _house, String _floor, String _side,
            String _postcode, String _city) {
        street = _street;
        house = _house;
        floor = _floor;
        side = _side;
        postcode = _postcode;
        city = _city;
    }

    public String street(){
        return street;
    }

    public String house() {
        return house;
    }

    public String toString() {
        return street + " " + house + ", " + floor + " " + side + " "
                + postcode + " " + city;
    }
}
