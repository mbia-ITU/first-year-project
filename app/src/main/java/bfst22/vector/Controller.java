package bfst22.vector;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.*;
import javax.xml.crypto.NodeSetData;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class Controller {
    private Point2D lastMouse;
    private ArrayList<String> match = new ArrayList<>();

    @FXML
    private MapCanvas canvas;
    @FXML
    private Label percentText;
    @FXML
    private Text desc;

    @FXML
    private ComboBox searching;
    @FXML
    private Label sear;
    @FXML
    private Label sear1;
    @FXML
    private Label sear2;

    public void init(Model model) {
        canvas.init(model);
        searching.setEditable(true);
//Searchbar listener
        searching.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            String input = newValue;
            ArrayList<String> results = getMatches(input,model);
            if(newValue.isEmpty()){

                results.clear();
                sear.setText("");
                sear1.setText("");
                sear2.setText("");
            }else if (!newValue.equals(oldValue)){
                //var builder = new AdressParse.Builder();
                // var matcher = PATTERN.matcher(newValue);
                results = getMatches(input,model);

                sear.setText(results.get(0));
                sear1.setText(results.get(1));
                sear2.setText(results.get(2));
                        /*if (matcher.matches()) {
                            builder.street(matcher.group("street"));
                            builder.house(matcher.group("house"));
                            builder.postcode(matcher.group("postcode"));
                            builder.city(matcher.group("city"));
                            builder.floor(matcher.group("floor"));
                            builder.side(matcher.group("side"));

                            sear.setText(builder.build().toString());
                            sear1.setText(matcher.group());
                        }*/
            }
        });
    }

    private ArrayList<String> getMatches(String st, Model model) {
        String REGEX = "^(?<street>[A-ZÆØÅÉa-zæøåé ]*?) +(?<house>[0-9A-Z/-]+)[ ,]* ?((?<floor>[0-9])?[,.]* ?(?<side>[a-zæøå.]+)?,?)?[ ]*(?<postcode>[0-9]{4})?[ ]*(?<city>[A-ZÆØÅa-zæøå ]*)?$";
        Pattern PATTERN = Pattern.compile(REGEX);
        match.clear();
        ArrayList<String> list = model.getAddresses();
        var matcher = PATTERN.matcher(st);
        for(String string: list){
            if(string.toLowerCase().contains(st.toLowerCase())){
                match.add(string);
            }
            if(match.size()>3){
                break;
            }
        }
        return match;
    }

    @FXML
    private void onScroll(ScrollEvent e) {
        var factor = e.getDeltaY();
        canvas.zoom(Math.pow(1.003, factor), e.getX(), e.getY());
        percentText.setText(String.valueOf("Zoom: "+canvas.getZoomPercentage() + "%"));
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
        var model = new Model("data/Bornholm.zip");
        Stage mapStage = new Stage();
        new MapView(model, mapStage,"UI.fxml");
        new KdTree(Model.getNodes());

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
    }
}
