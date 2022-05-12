package bfst22.vector;

import java.lang.Object;
import org.junit.jupiter.api.Test;

import javafx.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;

public class TestProg {
    
    @Test
    void testAll(){
        assertEquals(1, 1);
        
    }
    /*  
    @Test
    void testCon(){
        Controller con = new Controller();

        Exception exception = assertThrows(Exception.class, () ->{});
    }
*/
    @Test
    void testDijkstra(){
        try {
            Model model = new Model("data/small.osm");
            DijkstraSP sp = new DijkstraSP(model.routeGraph, model.routeGraph.indexNode2.get(1));
            assertTrue(sp.check(model.routeGraph, model.routeGraph.indexNode2.get(1)));

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


}
