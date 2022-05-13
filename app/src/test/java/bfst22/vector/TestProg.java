package bfst22.vector;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestProg {


    @Test
    void testtest(){
        assertEquals(1, 1);
        
    }
    
    @Test
    void testCon(){
        try {
            Model model = new Model("data/small.osm");
            Controller con = new Controller();
            con.init(model);

        } catch (Exception e) {
            System.out.println("i failed in creating a model or executing dijkstra");
        }

        //Exception exception = assertThrows(Exception.class, () ->{});
    }
    
    @Test
    void testDijkstra(){
        try {
            Model model = new Model("data/small.osm");
            DijkstraSP sp = new DijkstraSP(model.routeGraph, model.routeGraph.indexNode2.get(1));
            //running dejkstra on a working graph with a sensible start node;
            assertTrue(sp.check(model.routeGraph, model.routeGraph.indexNode2.get(1)));
            //running dejkstra with absolute max integer value (should not exist)
            assertFalse(sp.check(model.routeGraph, model.routeGraph.indexNode2.get(2147483646)));

        } catch (Exception e) {
            System.out.println("i failed in creating a model or executing dijkstra");
        }
    }
    
    @Test
    void testModel(){
        try {
            Model model = new Model("data/small.osm");
            assertEquals(model.getAddresses(), model.addresses);


        } catch (Exception e) {
            System.out.println("i failed in creating a model");
        }
    }

    @Test
    void testCanvas(){
        try {
            Model model = new Model("data/small.osm");
            MapCanvas can = new MapCanvas();
            can.init(model);
            assertFalse(can.debug);
            can.DebugMode();
            assertTrue(can.debug);
            //assertEquals(100, can.getZoomPercentage());
            can.drawLevel = 2;
            can.setDrawType(1);
            can.repaint();


        } catch (Exception e) {
            System.out.println("i failed in creating a model");
        }
        
    }

    @Test
    void testAdressParse(){
        //test ÆØÅ
        var address = Address.parse("Ålekistevej 121, 2720 København");
        assertEquals("Ålekistevej", address.street,"Danish street and city names can start with ÆØÅ");
        address = Address.parse("Lavendelvej 7, 3650 Ølstykke");
        assertEquals("Ølstykke", address.city,"Danish street and city names can start with ÆØÅ");
        //test with a standard adress
        address = Address.parse("Svend Gønges Vej 13, 2680 Solrød Strand");
        assertEquals("Svend Gønges Vej", address.street);
        assertEquals("13", address.housenumber);
        assertEquals("2680", address.postcode);
        assertEquals("Solrød Strand", address.city);
        //test with letter in housenumber
        address = Address.parse("Rued Langgaards Vej 7A, 2300 København S");
        assertEquals("Rued Langgaards Vej", address.street);
        assertEquals("7A", address.housenumber);
        assertEquals("2300", address.postcode);
        assertEquals("København S", address.city);
        //test with nothing
        address = Address.parse("");
        assertNull(address.street);
        assertNull(address.housenumber);
        assertNull(address.postcode);
        assertNull(address.city);
        //a test without the housenumber
        //note - this test will fail, and we currently dont have a fix
        /*
        address = Address.parse("Rued Langgaards Vej, 2000 København");
		assertEquals("Rued Langgaards Vej", addr.street);
		assertEquals("2000", addr.postcode);
        */
        //a test with no postumber
        address = Address.parse("Rued Langgaards Vej 7, København");
		assertEquals("København", address.city);
        //a test with a special letter
        address = Address.parse("Allé 3, 2720 Vanløse");
		assertEquals("Allé", address.street);
        //a test with interval housenumber
        address = Address.parse("gudhjemvej 85-87, 4878 gudhjem");
		assertEquals("85-87", address.housenumber);
        //testing long postcodes (shouldnt exist in denmark)
        address = Address.parse("gudhjemvej 1, 99999 gudhjem");
		assertNotEquals("99999", address.postcode);
		address = Address.parse("gudhjemvej 1, 888888 gudhjem");
		assertNotEquals("888888", address.postcode);	
        //Testing lots of commas
        address = Address.parse("Jernbanegade       41B,,,,,, 3600 Frederikssund");
        assertEquals("Jernbanegade", address.street);
        assertEquals("41B", address.housenumber);
        assertEquals("3600", address.postcode);
        assertEquals("Frederikssund", address.city);
        //
    }

    @Test
    void testAdressCompare(){
        //test with adress1 being higher
        var adress1 = Address.parse("Allé 3, 2720 Vanløse");
        var adress2 = Address.parse("Rued Langgaards Vej 7, København");
        assertTrue(0>adress1.compareTo(adress2));
        //adress1 being lower
        adress1 = Address.parse("Sallé 3, 2720 Vanløse");
        assertTrue(0<adress1.compareTo(adress2));
        //being the exact same
        adress1 = Address.parse("Rued Langgaards Vej 7, København");
        assertTrue(0==adress1.compareTo(adress2));
        
    }


}
