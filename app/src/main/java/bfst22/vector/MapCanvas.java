package bfst22.vector;

import java.util.*;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light.Point;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

import java.awt.*;
import java.util.List;

public class MapCanvas extends Canvas {
    Model model;
    Affine trans = new Affine();
    double initialZoomLevel;
    double currentZoomLevel;
    double zoomPercentage = 112;
    int drawLevel = 0;
    double zp;
    Boolean debug = false;
    BoundingBox mousebox = null;
    GraphicsContext gc = getGraphicsContext2D();
    public DijkstraSP sp;
    OSMNode targetStart;
    double smallestDist = 420;

    int drawType = 0;

    void init(Model model) {
        this.model = model;
        pan(-model.minlon, -model.minlat);
        zoom(640 / (model.maxlon - model.minlon), 0, 0);
        model.addObserver(this::repaint);
        initialZoomLevel = trans.getMxx();
        repaint();

    }

    void repaint() {
        gc.setTransform(new Affine());
        if (drawType == 0) {
            gc.setFill(Color.DEEPSKYBLUE);
            gc.fillRect(0, 0, getWidth(), getHeight());
            gc.setTransform(trans);
            colorMap();
        } else if (drawType == 1) {
            gc.setFill(Color.WHITE);
            gc.setStroke(Color.BLACK);
            gc.fillRect(0, 0, getWidth(), getHeight());
            gc.setTransform(trans);
            drawLineMap();
        }


        gc.setLineWidth(1.7 / Math.sqrt(trans.determinant()));
        gc.setStroke(Color.RED);
        //gc.setLineWidth(0.01);
        for (var line : model.getDrawablesFromTypeInBB(WayType.PATHTO, BoundingBoxFromScreen())) {
            line.draw(gc);
        }

        for (var line : model.getDrawablesFromTypeInBB(WayType.DESTINATION, BoundingBoxFromScreen())) {
            gc.setFill(Color.RED);
            line.draw(gc);
            gc.setFill(Color.WHITE);
        }

        for (var line : model.getDrawablesFromTypeInBB(WayType.STARTPOINT, BoundingBoxFromScreen())) {
            gc.setFill(Color.ORANGE);
            line.draw(gc);
            gc.setFill(Color.WHITE);
        }
    }

    //This method returns the current user view as a bounding box
    BoundingBox BoundingBoxFromScreen() {
        Point2D p1_xy = new Point2D(0, 0);
        Point2D p2_xy = new Point2D(getWidth(), getHeight());

        if (debug) {
            p1_xy = new Point2D(300, 250);
            p2_xy = new Point2D(getWidth()-300, getHeight()-250);
        }
        Point2D p1_latlon = mouseToModel(p1_xy);
        Point2D p2_latlon = mouseToModel(p2_xy);

        float f_p1_lon = (float) p1_latlon.getX();
        float f_p2_lon = (float) p2_latlon.getX();
        float f_p1_lat = (float) p1_latlon.getY();
        float f_p2_lat = (float) p2_latlon.getY();
        //System.out.println(f_p1_lon + " " + f_p2_lon + " " + f_p1_lat + " " + f_p2_lat);

        return new BoundingBox(f_p1_lon, f_p2_lon, f_p1_lat, f_p2_lat);


    }

    void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        repaint();
    }

    public double getCurrentZoomLevel() {
        return currentZoomLevel;
    }

    void zoom(double factor, double x, double y) {
        trans.prependTranslation(-x, -y);
        trans.prependScale(factor, factor);
        trans.prependTranslation(x, y);
        currentZoomLevel = trans.getMxx();
        zoomPercentage = 100 / (initialZoomLevel / currentZoomLevel);
        if (zoomPercentage < 200) drawLevel = 0;
        if (zoomPercentage > 300 && zoomPercentage < 500) drawLevel = 1;
        //if (zoomPercentage > 500) drawLevel = 2;
        if (currentZoomLevel == initialZoomLevel) drawLevel = 0;
        repaint();
    }

    public Point2D mouseToModel(Point2D point) {
        try {
            return trans.inverseTransform(point);
        } catch (NonInvertibleTransformException e) {
            throw new RuntimeException(e);
        }
    }

    public double getZoomPercentage() {
        zp = zoomPercentage;
        double percent1 = zp * 100;
        int percent2 = (int) percent1;
        return (double) percent2 / 100;
    }


    private void colorMap() {


        gc.setLineWidth(1 / Math.sqrt(trans.determinant()));
        gc.setFill(Color.BEIGE);
        for (var line : model.getDrawablesFromTypeInBB(WayType.PLACEHOLDER, BoundingBoxFromScreen())) {
            line.fill(gc);
        }

        gc.setFill(Color.PINK);
        for (var line : model.getDrawablesFromTypeInBB(WayType.COASTLINE, BoundingBoxFromScreen())) {
            line.trace(gc);
            line.fill(gc);
        }
        gc.setFill(Color.DARKGREEN);
        for (var line : model.getDrawablesFromTypeInBB(WayType.FOREST, BoundingBoxFromScreen())) {
            line.fill(gc);
        }
        gc.setFill(Color.KHAKI);
        for (var line : model.getDrawablesFromTypeInBB(WayType.FARMLAND, BoundingBoxFromScreen())) {
            line.fill(gc);
        }

        gc.setFill(Color.LIGHTGREY);
        for (var line : model.getDrawablesFromTypeInBB(WayType.RESIDENTIAL, BoundingBoxFromScreen())) {
            line.fill(gc);
        }

        gc.setFill(Color.TAN);
        for (var line : model.getDrawablesFromTypeInBB(WayType.HEATH, BoundingBoxFromScreen())) {
            line.fill(gc);
        }
        gc.setFill(Color.MOCCASIN);
        for (var line : model.getDrawablesFromTypeInBB(WayType.BEACH, BoundingBoxFromScreen())) {
            line.fill(gc);
        }
        gc.setFill(Color.OLDLACE);
        for (var line : model.getDrawablesFromTypeInBB(WayType.SAND, BoundingBoxFromScreen())) {
            line.fill(gc);
        }
        gc.setFill(Color.TEAL);
        for (var line : model.getDrawablesFromTypeInBB(WayType.WETLAND, BoundingBoxFromScreen())) {
            line.fill(gc);
        }
        gc.setFill(Color.DARKSEAGREEN);
        for (var line : model.getDrawablesFromTypeInBB(WayType.SCRUB, BoundingBoxFromScreen())) {
            line.fill(gc);
        }
        gc.setFill(Color.PALEGREEN);
        for (var line : model.getDrawablesFromTypeInBB(WayType.MEADOW, BoundingBoxFromScreen())) {
            line.fill(gc);
        }
        gc.setFill(Color.PEACHPUFF);
        for (var line : model.getDrawablesFromTypeInBB(WayType.FARMYARD, BoundingBoxFromScreen())) {
            line.fill(gc);
        }
        gc.setFill(Color.PLUM);
        for (var line : model.getDrawablesFromTypeInBB(WayType.INDUSTRIAL, BoundingBoxFromScreen())) {
            line.fill(gc);
        }
        gc.setFill(Color.LIGHTGREEN);
        for (var line : model.getDrawablesFromTypeInBB(WayType.GRASS, BoundingBoxFromScreen())) {
            line.fill(gc);
        }
        if (drawLevel == 1) {
            gc.setFill(Color.SEASHELL);
            for (var line : model.getDrawablesFromTypeInBB(WayType.RESORT, BoundingBoxFromScreen())) {
                line.fill(gc);
            }
        }
        if (drawLevel == 2) {
            gc.setFill(Color.LIGHTYELLOW);
            for (var line : model.getDrawablesFromTypeInBB(WayType.HOSPITAL, BoundingBoxFromScreen())) {
                line.fill(gc);
            }
        }
        if (drawLevel == 2) {
            gc.setFill(Color.MEDIUMPURPLE);
            for (var line : model.getDrawablesFromTypeInBB(WayType.HELIPAD, BoundingBoxFromScreen())) {
                line.fill(gc);
            }
        }
        gc.setFill(Color.LIGHTBLUE);
        for (var line : model.getDrawablesFromTypeInBB(WayType.LAKE, BoundingBoxFromScreen())) {
            line.fill(gc);
        }
        /*
        gc.setStroke(Color.LIGHTBLUE);
        for (var line : model.iterable(WayType.RIVER)) {
            line.draw(gc);
        }
        gc.setStroke(Color.LIGHTBLUE);
        for (var line : model.iterable(WayType.TERTIARY)) {
            line.draw(gc);
        }
        */
        if (drawLevel == 2) {
            gc.setFill(Color.DARKTURQUOISE);
            for (var line : model.getDrawablesFromTypeInBB(WayType.PITCH, BoundingBoxFromScreen())) {
                line.fill(gc);
            }
        }
        if (drawLevel == 2) {
            gc.setFill(Color.DARKTURQUOISE);
            for (var line : model.getDrawablesFromTypeInBB(WayType.SOCCER, BoundingBoxFromScreen())) {
                line.fill(gc);
            }
        }
        if (drawLevel == 2) {
            gc.setFill(Color.LAVENDER);
            for (var line : model.getDrawablesFromTypeInBB(WayType.PARKING, BoundingBoxFromScreen())) {
                line.fill(gc);
            }
        }
        if (drawLevel == 2) {
            gc.setFill(Color.HONEYDEW);
            for (var line : model.getDrawablesFromTypeInBB(WayType.GOLFCOURSE, BoundingBoxFromScreen())) {
                line.fill(gc);
            }
        }
        if (drawLevel >= 1) {
            gc.setFill(Color.GOLDENROD);
            for (var line : model.getDrawablesFromTypeInBB(WayType.PROTECTEDAREA, BoundingBoxFromScreen())) {
                line.fill(gc);
            }
        }
        if (drawLevel >= 1) {
            gc.setFill(Color.SEASHELL);
            for (var line : model.getDrawablesFromTypeInBB(WayType.RESERVE, BoundingBoxFromScreen())) {
                line.fill(gc);
            }
        }
        if (drawLevel == 2) {
            gc.setFill(Color.MEDIUMAQUAMARINE);
            for (var line : model.getDrawablesFromTypeInBB(WayType.CEMETERY, BoundingBoxFromScreen())) {
                line.fill(gc);
            }
        }

        if (drawLevel == 2) {
            gc.setFill(Color.LIGHTSTEELBLUE);
            for (var line : model.getDrawablesFromTypeInBB(WayType.RACE, BoundingBoxFromScreen())) {
                line.fill(gc);
            }
        }
        if (drawLevel == 2) {
            gc.setFill(Color.RED);
            for (var line : model.getDrawablesFromTypeInBB(WayType.RACEWAY, BoundingBoxFromScreen())) {
                line.fill(gc);
            }
        }
        if (drawLevel == 2) {
            gc.setFill(Color.GRAY);
            for (var line : model.getDrawablesFromTypeInBB(WayType.BUILDING, BoundingBoxFromScreen())) {
                line.fill(gc);
            }
        }


        if (drawLevel == 2) {
            gc.setLineWidth(0.00008);
            gc.setStroke(Color.DARKGRAY);
            for (var line : model.getDrawablesFromTypeInBB(WayType.RESIDENTIALWAY, BoundingBoxFromScreen())) {
                line.draw(gc);
            }
        }

        gc.setLineWidth(0.00013);
        gc.setStroke(Color.ORANGE);
        for (var line : model.getDrawablesFromTypeInBB(WayType.PRIMARYHIGHWAY, BoundingBoxFromScreen())) {
            line.draw(gc);
        }

        if(mousebox != null){
            gc.setStroke(Color.BLUE);
            for (var line : model.getDrawablesFromTypeInBB(WayType.RESIDENTIALWAY, mousebox)) {
                line.draw(gc);

                for(OSMNode node : ((PolyLine) line).getListOfNodes()){
                    double dist = Math.sqrt(Math.pow((mousebox.getCenterY() - node.getLon()),2)+Math.pow((mousebox.getCenterX() - node.getLat()),2));
                    if(dist < smallestDist){
                        smallestDist = dist;
                        targetStart = node;
                    }
                }
            }
            List<OSMNode> toNN = new ArrayList<>();
            toNN.add(new OSMNode(1,mousebox.getCenterX(), mousebox.getCenterY()));
            toNN.add(targetStart);
            PolyLine toNNs = new PolyLine(toNN);
            gc.setStroke(Color.GREEN);
            toNNs.draw(gc);
        }


        for (var line : model.getDrawablesFromTypeInBB(WayType.DESTINATION, BoundingBoxFromScreen())) {
            line.resize(currentZoomLevel);
            //line.draw(gc);
        }

        for (var line : model.getDrawablesFromTypeInBB(WayType.STARTPOINT, BoundingBoxFromScreen())) {
            line.resize(currentZoomLevel);
            //line.draw(gc);
        }

    }

    private void drawLineMap() {
        gc.setLineWidth(1 / Math.sqrt(trans.determinant()));
        for (var line : model.getDrawablesFromTypeInBB(WayType.BEACH, BoundingBoxFromScreen())) {
            line.draw(gc);
        }
        for (var line : model.getDrawablesFromTypeInBB(WayType.COASTLINE, BoundingBoxFromScreen())) {
            line.draw(gc);
        }
        if (drawLevel >= 1) {
            for (var line : model.getDrawablesFromTypeInBB(WayType.BUILDING, BoundingBoxFromScreen())) {
                line.draw(gc);
            }
            for (var line : model.getDrawablesFromTypeInBB(WayType.FOREST, BoundingBoxFromScreen())) {
                line.draw(gc);
            }
            for (var line : model.getDrawablesFromTypeInBB(WayType.INDUSTRIAL, BoundingBoxFromScreen())) {
                line.draw(gc);
            }
            for (var line : model.getDrawablesFromTypeInBB(WayType.RESIDENTIAL, BoundingBoxFromScreen())) {
                line.draw(gc);
            }
        }
        if (drawLevel == 2) {
            for (var line : model.getDrawablesFromTypeInBB(WayType.CEMETERY, BoundingBoxFromScreen())) {
                line.draw(gc);
            }
            /*for (var line : model.getDrawablesFromTypeInBB(WayType.FARMLAND, BoundingBoxFromScreen())) {
                line.draw(gc);
            }
            for (var line : model.getDrawablesFromTypeInBB(WayType.FARMYARD, BoundingBoxFromScreen())) {
                line.draw(gc);
            }*/

            for (var line : model.getDrawablesFromTypeInBB(WayType.GOLFCOURSE, BoundingBoxFromScreen())) {
                line.draw(gc);
            }

            for (var line : model.getDrawablesFromTypeInBB(WayType.GRASS, BoundingBoxFromScreen())) {
                line.draw(gc);
            }
            for (var line : model.getDrawablesFromTypeInBB(WayType.HEATH, BoundingBoxFromScreen())) {
                line.draw(gc);
            }
            for (var line : model.getDrawablesFromTypeInBB(WayType.HELIPAD, BoundingBoxFromScreen())) {
                line.draw(gc);
            }

            for (var line : model.getDrawablesFromTypeInBB(WayType.HOSPITAL, BoundingBoxFromScreen())) {
                line.draw(gc);
            }


            for (var line : model.getDrawablesFromTypeInBB(WayType.LAKE, BoundingBoxFromScreen())) {
                line.draw(gc);
            }
            for (var line : model.getDrawablesFromTypeInBB(WayType.MEADOW, BoundingBoxFromScreen())) {
                line.draw(gc);
            }

            for (var line : model.getDrawablesFromTypeInBB(WayType.PARKING, BoundingBoxFromScreen())) {
                line.draw(gc);
            }

            for (var line : model.getDrawablesFromTypeInBB(WayType.PITCH, BoundingBoxFromScreen())) {
                line.draw(gc);
            }

            for (var line : model.getDrawablesFromTypeInBB(WayType.PROTECTEDAREA, BoundingBoxFromScreen())) {
                line.draw(gc);
            }
            for (var line : model.getDrawablesFromTypeInBB(WayType.RACE, BoundingBoxFromScreen())) {
                line.draw(gc);
            }
            for (var line : model.getDrawablesFromTypeInBB(WayType.RACEWAY, BoundingBoxFromScreen())) {
                line.draw(gc);
            }
            for (var line : model.getDrawablesFromTypeInBB(WayType.RESERVE, BoundingBoxFromScreen())) {
                line.draw(gc);
            }

            for (var line : model.getDrawablesFromTypeInBB(WayType.RESORT, BoundingBoxFromScreen())) {
                line.draw(gc);
            }
            for (var line : model.getDrawablesFromTypeInBB(WayType.RIVER, BoundingBoxFromScreen())) {
                line.draw(gc);
            }
            for (var line : model.getDrawablesFromTypeInBB(WayType.SAND, BoundingBoxFromScreen())) {
                line.draw(gc);
            }
            for (var line : model.getDrawablesFromTypeInBB(WayType.SCRUB, BoundingBoxFromScreen())) {
                line.draw(gc);
            }
            for (var line : model.getDrawablesFromTypeInBB(WayType.SOCCER, BoundingBoxFromScreen())) {
                line.draw(gc);
            }
            for (var line : model.getDrawablesFromTypeInBB(WayType.TERTIARY, BoundingBoxFromScreen())) {
                line.draw(gc);
            }
            for (var line : model.getDrawablesFromTypeInBB(WayType.WETLAND, BoundingBoxFromScreen())) {
                line.draw(gc);
            }
        }

        //Segment for roads
       gc.setLineWidth(0.00008);


        gc.setStroke(Color.LIGHTGREY);
        for (var line : model.getDrawablesFromTypeInBB(WayType.RESIDENTIALWAY, BoundingBoxFromScreen())) {
            line.draw(gc);
        }
        
        gc.setLineWidth(0.00013);
        gc.setStroke(Color.DARKGRAY);
        for (var line : model.getDrawablesFromTypeInBB(WayType.PRIMARYHIGHWAY, BoundingBoxFromScreen())) {
            line.draw(gc);
        }

        //for inspection/highlighting of ways
        if(mousebox != null){
            gc.setStroke(Color.BLUE);
            for (var line : model.getDrawablesFromTypeInBB(WayType.RESIDENTIALWAY, mousebox)) {
                line.draw(gc);

                for(OSMNode node : ((PolyLine) line).getListOfNodes()){
                    double dist = Math.sqrt(Math.pow((mousebox.getCenterY() - node.getLon()),2)+Math.pow((mousebox.getCenterX() - node.getLat()),2));
                    if(dist < smallestDist){
                        smallestDist = dist;
                        targetStart = node;
                    }
                }
            }
            List<OSMNode> toNN = new ArrayList<>();
            toNN.add(new OSMNode(1,mousebox.getCenterX(), mousebox.getCenterY()));
            toNN.add(targetStart);
            PolyLine toNNs = new PolyLine(toNN);
            gc.setStroke(Color.GREEN);
            toNNs.draw(gc);
        }


        gc.setLineWidth(1.5 / Math.sqrt(trans.determinant()));


        //for searched addresses
        for (var line : model.getDrawablesFromTypeInBB(WayType.DESTINATION, BoundingBoxFromScreen())) {
            line.resize(currentZoomLevel);
            //line.draw(gc);
        }
        for (var line : model.getDrawablesFromTypeInBB(WayType.STARTPOINT, BoundingBoxFromScreen())) {
            line.resize(currentZoomLevel);
            //line.draw(gc);
        }
        if(toNNs!=null){
            toNNs.draw(gc);
        }

    }

    public void setDrawType(int l) {
        drawType = l;
        repaint();
    }

    public void DebugMode() {
        if (!debug) {
            debug = true;
        } else {
            debug = false;
        }
    }

    public void mouseBox(BoundingBox mouseBox){
        mousebox = mouseBox;
    }

    public void nullBox(){
        mousebox = null;
    }
    List<OSMNode> toNN = new ArrayList<>();
    PolyLine toNNs;
    public OSMNode nearestRoad(BoundingBox bb){
        smallestDist = 400;
        for (var line : model.getDrawablesFromTypeInBB(WayType.RESIDENTIALWAY, bb)) {
            for(OSMNode node : ((PolyLine) line).getListOfNodes()){
                double dist = Math.sqrt(Math.pow((mousebox.getCenterY() - node.getLon()),2)+Math.pow((mousebox.getCenterX() - node.getLat()),2));
                if(dist < smallestDist){
                    smallestDist = dist;
                    targetStart = node;
                }
            }
        }
        return targetStart;
    }

    public OSMNode nearestWay(BoundingBox addressBB1){
            smallestDist = 400;
            for (var line : model.getDrawablesFromTypeInBB(WayType.RESIDENTIALWAY, addressBB1)) {
                for(OSMNode node : ((PolyLine) line).getListOfNodes()){
                    double dist = Math.sqrt(Math.pow((addressBB1.getCenterY() - node.getLon()),2)+Math.pow((addressBB1.getCenterX() - node.getLat()),2));
                    if(dist < smallestDist){
                        smallestDist = dist;
                        targetStart = node;
                    }
                }
            }
            return targetStart;
        }

        public void drawRoute(OSMNode from, OSMNode to){
        sp = new DijkstraSP(model.routeGraph, from);
        boolean hasfirst = false;
        ArrayList<OSMNode> routePath = new ArrayList<>();
        for (DirectedEdge ed : sp.pathTo(to,model.routeGraph)) {

            if (!hasfirst) {
                routePath.add(ed.to());
            }
            hasfirst = true;
            routePath.add(ed.from());
        }
        model.addRoute(routePath);
        }

}
