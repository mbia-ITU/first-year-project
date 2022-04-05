package bfst22.vector;

public class AdressParse {
    public static class Builder {
        private String street, house, floor, side, postcode, city;

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

        public Address build() {
            return new Address(street, house, floor, side, postcode, city);
        }
    }
}
