package bfst22.vector;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.CheckBox;


import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
/**
 * the controller of the program, which hold all the methods using ActionListeners, taking in the userinput and calling the various methods of classes
 * which are used by the buttons and inputfields in the ui
 */

public class Controller {
    private Point2D lastMouse;
    private Point2D currentMouse;
    private ArrayList<Address> match = new ArrayList<>();
    private Model model;
    private Address start;
    private Address destination;

    //private Button btn;

    @FXML
    private MapCanvas canvas;
    @FXML
    private Label percentText;
    @FXML
    private Text desc;
    @FXML
    private CheckBox highlighter;
    @FXML
    private Button result1;
    @FXML
    private Button result2;
    @FXML
    private TextField searching;
    @FXML
    private TextField searching1;
    @FXML
    private Label warning;

    /**
     * initialized the controller such that it can call methods in the pane and in the {@code MapCanvas} class
     * @param model an {@code Model} object used to initialize the {@code MapCanvas} and taken in to get values parsed from the data file chosen.
     */
    public void init(Model model) {
        this.model=model;
        canvas.init(model);
        searching.setEditable(true);
        result1.setVisible(false);
        searching1.setEditable(true);
        result2.setVisible(false);
    //Searchbar for from listener
        searching.textProperty().addListener((observable, oldValue, newValue) -> {
            String input = newValue;

            start = getMatches(input,model).get(0);
            if(newValue.isEmpty()){
                start = null;
                result1.setVisible(false);
            }else if (!newValue.equals(oldValue)){

                start = getMatches(input,model).get(0);
                    result1.setText(start.getAdress());
                    if(start==null){
                        result1.setVisible(false);
                    }else {
                        result1.setVisible(true);
                    }
            }
        });
        searching1.textProperty().addListener((observable, oldValue, newValue) -> {
            String input1 = newValue;
            destination = getMatches(input1,model).get(0);
            if(newValue.isEmpty()){
                destination = null;
                result2.setVisible(false);
            }else if (!newValue.equals(oldValue)){
                destination = getMatches(input1,model).get(0);
                result2.setText(destination.getAdress());
                if(destination==null){
                    result2.setVisible(false);
                }else {
                    result2.setVisible(true);
                }

            }
        });
    }

    /**
     * returns an arraylist of addresses that fit the searchcriteria provided by the regex and written in the textfield by the user
     * @param searchStr the string written in the textfield by the user
     * @param model {@code Model} object used to get the full list of addresses
     * @return a list of addresses that fit the criteria found through binarysearch
     */
    private ArrayList<Address> getMatches(String searchStr, Model model) {
        int pos = 0;
        match.clear();
        var addr = Address.parse(searchStr);

        Comparator<Address> c = new Comparator<Address>() {
            public int compare(Address a1, Address a2) {

                //System.out.println("post: x" + a2.getCity() + "x");
                if (a2.getHousenumber().equals("") && a2.getPostcode() == null) {
                    //searches streets
                    return a1.getStreet().toLowerCase().compareTo(a2.getStreet().toLowerCase());
                } else if(a2.getPostcode() == null && a2.getCity().length()>=1 ){
                    //searches streets + housenumber + city
                    return (a1.getStreet().toLowerCase() + a1.getHousenumber() + a1.getCity().toLowerCase()).compareTo(a2.getStreet().toLowerCase() + a2.getHousenumber() + a2.getCity().toLowerCase());
                }else if (a2.getPostcode() == null){
                    //searches streets + housenumber
                    return (a1.getStreet().toLowerCase() + a1.getHousenumber()).compareTo(a2.getStreet().toLowerCase() + a2.getHousenumber());
                }else if(a2.getCity().equals("")){
                    //searches streets + housenumber + postcode
                    return (a1.getStreet().toLowerCase() + a1.getHousenumber() + a1.getPostcode()).compareTo(a2.getStreet().toLowerCase() + a2.getHousenumber() + a2.getPostcode());
                }
                    //searches the entire address
                return a1.getAdress().toLowerCase().compareTo(a2.getAdress().toLowerCase());
            }
        };
        //binary search

        pos = Collections.binarySearch(model.getAddresses(),new Address(addr.getStreet(), addr.getHousenumber(), addr.getPostcode(), addr.getCity(), null), c);
        if(pos >= 0 && pos <= model.getAddresses().size()-1){
            match.add(model.getAddresses().get(pos));
        }else{

            int first = 0;
            int last = model.getAddresses().size() - 1;
            int mid = (first + last) / 2;
            //binarysearch for incomplete streetnames
            while (first <= last) {
                if (model.getAddresses().get(mid).getStreet().compareTo(addr.getStreet()) > 0) {
                    first = mid + 1;
                } else if (model.getAddresses().get(mid).getStreet().contains(addr.getStreet())) {
                    match.add(model.getAddresses().get(mid));
                    //doTheThing
                    break;
                } else {
                    last = mid - 1;
                }
                mid = (first + last) / 2;
            }
            if (first > last) {
                match.add(model.getAddresses().get(mid));
            }
        }


        return match;
    }

    /**
     * zooms in or out on the canvas when the mouse is scrolled
     * @param e scrollevent on the mouse
     */
    @FXML
    private void onScroll(ScrollEvent e) {
        var factor = e.getDeltaY();
            canvas.zoom(Math.pow(1.003, factor), e.getX(), e.getY());
            percentText.setText("Zoom: "+ canvas.getZoomPercentage() + "%");

    }

    /**
     * drags the map around on the map when the left mousebutton is held and the mouse is moved
     * @param e mouse hold and drag
     */
    @FXML
    private void onMouseDragged(MouseEvent e) {
        var dx = e.getX() - lastMouse.getX();
        var dy = e.getY() - lastMouse.getY();
        canvas.pan(dx, dy);
        lastMouse = new Point2D(e.getX(), e.getY());
    }

    /**
     * sets the last point point in which the mouse was pressed on the map
     * @param e left mouse click
     */
    @FXML
    private void onMousePressed(MouseEvent e) {
        lastMouse = new Point2D(e.getX(), e.getY());
    }

    /**
     * loads the default file provided and initializes the model using the file
     * @param e pressing the button
     * @throws Exception is thrown if it fails to load the file
     */
    @FXML
    private void onPress(ActionEvent e)throws Exception {
        File firstBootup = new File("data/Bornholm.zip.obj");
        if(firstBootup.exists()){
            var model = new Model("data/Bornholm.zip.obj");
            Stage mapStage = new Stage();
            new MapView(model, mapStage,"UI.fxml");
        }else{
            var model = new Model("data/Bornholm.zip");
            Stage mapStage = new Stage();
            new MapView(model, mapStage,"UI.fxml");
        }
        View.exitMenu();
    }

    /**
     * lets the user chose a file to load, and then loads set file the same way as onPress()
     * @param e pressing the button and choosing the file
     * @throws Exception is thrown if the file fails to load
     */
    @FXML
    private void onCustomPress(ActionEvent e) throws Exception {
        String dir= "";
        JFrame JF = new JFrame();
        FileDialog fd = new FileDialog(JF);
        fd.setVisible(true);
        File[] f = fd.getFiles();
        if(f.length > 0){
            dir = fd.getFiles()[0].getAbsolutePath();
        }
        JF.dispose();
        try {
            var model = new Model(dir);
            Stage mapStage = new Stage();
            new MapView(model, mapStage, "UI.fxml");
            View.exitMenu();
        }catch (IOException ex){
        desc.setText("The chosen file was not supported (try .osm)");
        desc.setFill(Color.RED);
        }
        View.exitMenu();
    }


    /**
     * Stores the address data such that it can be used to conpute the shortest path between it and the second chosen address
     * also draws a point on the address visible to the user
     * @param e pressing the button on the ui
     */
    @FXML
    private void onAddressPress1(ActionEvent e){
        //mark the address with a cirkle
        if(model.getStart().size()>0){
            model.clearStart();
        }
        model.addStart(start.getNode());

        //find the nearest road within boundingbox
        Point2D startAddress = new Point2D(start.getNode().lat,start.getNode().getLon());
        BoundingBox startAddrBox = new BoundingBox((float)(startAddress.getX()-0.00009), (float) (startAddress.getX()+0.00009), (float) (startAddress.getY()-0.00009), (float) (startAddress.getY()+0.00009));

        //add the edge to the Dijkstra graph
        model.routeGraph.addEdge(start.getNode(),new DirectedEdge(start.getNode(),canvas.nearestWay(startAddrBox)));
        model.routeGraph.addEdge(canvas.nearestWay(startAddrBox),new DirectedEdge(canvas.nearestWay(startAddrBox),start.getNode()));
        try {
            if (start.getNode() != null && destination.getNode() != null) {
                canvas.drawShortestPath(start.getNode(), destination.getNode());
            }
            warning.setStyle("-fx-text-fill:#ECECEC");
            warning.setText("");
        }catch (NullPointerException exc){
            warning.setText("No path found");
            warning.setStyle("-fx-text-fill:#ff0000");
            exc.printStackTrace();
        }
        canvas.repaint();
    }

    /**
     * Stores the address data such that it can be used to conpute the shortest path between it and the first chosen address
     * also draws a point on the address visible to the user
     * @param e pressing the button on the ui
     */
    @FXML
    private void onAddressPress2(ActionEvent e){
        //mark the address with a cirkle
        if(model.getDestination().size()>0){
            model.clearDestination();
        }
        model.addDestination(destination.getNode());

        //find the nearest road within boundingbox
        Point2D endAddress = new Point2D(destination.getNode().lat,destination.getNode().getLon());
        BoundingBox endAddrBox = new BoundingBox((float)(endAddress.getX()-0.00009), (float) (endAddress.getX()+0.00009), (float) (endAddress.getY()-0.00009), (float) (endAddress.getY()+0.00009));

        model.routeGraph.addEdge(destination.getNode(),new DirectedEdge(destination.getNode(),canvas.nearestWay(endAddrBox)));
        model.routeGraph.addEdge(canvas.nearestWay(endAddrBox),new DirectedEdge(canvas.nearestWay(endAddrBox),destination.getNode()));

        try {
            if(start.getNode() != null && destination.getNode() != null){
                canvas.drawShortestPath(start.getNode(),destination.getNode());
            }
            warning.setStyle("-fx-text-fill:#ECECEC");
            warning.setText("");
        }catch (NullPointerException exc){
            warning.setText("No path found");
            warning.setStyle("-fx-text-fill:#ff0000");
            exc.printStackTrace();
        }
        canvas.repaint();
    }

    /**
     * sets the drawtype to 1 when the Line mode is pressed by the user
     * @param e pressing the line mode button
     */
    @FXML
    private void onLine(ActionEvent e){
        canvas.setDrawType(1);
    }

    /**
     * sets the drawtype to 0 when the color mode is pressed by the user
     * @param e pressing the color mode button
     */
    @FXML
    private void onColor(ActionEvent e){
        canvas.setDrawType(0);
    }
    
    /**
     * turns on the debugmode, reducing the size of the screen bounding box so that the kd-tree implementation is visible 
     * @param e pressing the toggle debug-mode button
     */
    @FXML
    private void onDebugMode(ActionEvent e){
        canvas.DebugMode();
    }

    /**
     * if the highlighter box is checked, this will highlight at show the roads in which the mouse is close to and is searching between to find the nearest one,
     * just like how addresses find the nearest road node to them for dijkstra
     * @param e moving the mouse
     */
    @FXML
    private void onMouseMoved(MouseEvent e){
        if(highlighter.isSelected()){
            currentMouse = new Point2D(e.getX(),e.getY());
            Point2D currMouse = canvas.mouseToModel(currentMouse);
            BoundingBox mouseBox = new BoundingBox((float)(currMouse.getX()-0.0009), (float) (currMouse.getX()+0.0009), (float) (currMouse.getY()-0.0009), (float) (currMouse.getY()+0.0009));
            canvas.setMouseBox(mouseBox);
        }else{
            canvas.nullBox();
        }
        canvas.repaint();

    }


}
