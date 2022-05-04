package bfst22.vector;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.MenuItem;
import javafx.scene.control.CheckBox;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.regex.Pattern;


public class Controller {



    private Point2D lastMouse;
    private Point2D currentMouse;
    private ArrayList<Address> match = new ArrayList<>();
    MenuItem item1 = new MenuItem();
    private Model model;
    private Address start;
    private Address destination;

    @FXML
    private MenuItem color;
    @FXML
    private MenuItem line;
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
    private Button addToFavorites;



    private final static String REGEX = "^(?<street>[A-ZÆØÅÉa-zæøåé ]+)(?<house>[0-9A-Z-]*)[ ,]* ?((?<floor>[0-9])?[,. ]* ?(?<side>[a-zæøå.,]+)??)?[ ]*(?<postcode>[0-9]{4})?[ ]*(?<city>[A-ZÆØÅa-zæøå ]*?)?$";
    private final static Pattern PATTERN = Pattern.compile(REGEX);

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

                //sear.setText(result.get(0).getAdress());
            }
        });
    }

    //returns an arraylist so we may keep earlier searches another time
    private ArrayList<Address> getMatches(String searchStr, Model model) {
        int pos = 0;
        match.clear();
        var addr = Address.parse(searchStr);

        Comparator<Address> c = new Comparator<Address>() {
            public int compare(Address a1, Address a2) {

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


    @FXML
    private void onScroll(ScrollEvent e) {
        var factor = e.getDeltaY();
        if(((factor > 0 && canvas.getZoomPercentage() < 2600)||(factor > 0 && canvas.getZoomPercentage()>100000))||(factor < 0 && canvas.getZoomPercentage()>40)) {
            canvas.zoom(Math.pow(1.003, factor), e.getX(), e.getY());
            percentText.setText(String.valueOf("Zoom: "+ canvas.getZoomPercentage() + "%"));
        }

    }

    @FXML
    private void onMouseDragged(MouseEvent e) {
        var dx = e.getX() - lastMouse.getX();
        var dy = e.getY() - lastMouse.getY();
        canvas.pan(dx, dy);
        lastMouse = new Point2D(e.getX(), e.getY());
    }

    @FXML
    private void onMousePressed(MouseEvent e) {
        lastMouse = new Point2D(e.getX(), e.getY());
    }

    //loads DK map
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

    //loads custom map from .zip or .osm
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
        }catch (Exception ex){
        desc.setText("The chosen file was not supported (try .osm)");
        desc.setFill(Color.RED);
        }
        View.exitMenu();
    }

    @FXML
    private void onAddressPress1(ActionEvent e){
        //panning needs kd tree
       // var dx = match.get(0).getNode().getLat();
        //var dy = match.get(0).getNode().getLon();
            //canvas.pan(-dx, dy);
        if(model.getStart().size()>0){
            model.clearStart();
        }
        model.addStart(start.getNode());
    }
    @FXML
    private void onAddressPress2(ActionEvent e){
        if(model.getDestination().size()>0){
            model.clearDestination();
        }
        model.addDestination(destination.getNode());
    }

    @FXML
    private void onLine(ActionEvent e){
        canvas.setDrawType(1);
    }
    @FXML
    private void onColor(ActionEvent e){
        canvas.setDrawType(0);
    }

    @FXML
    private void onMouseMoved(MouseEvent e){
        if(highlighter.isSelected()){
            currentMouse = new Point2D(e.getX(),e.getY());
            System.out.println(canvas.mouseToModel(currentMouse).toString());
        }
    }

    @FXML
    private void addToFavorites(ActionEvent e){
        //add destination

    }
}
