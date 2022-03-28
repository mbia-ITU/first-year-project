package bfst22.vector;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class View {
    private Model model;
    private static Stage stage1;

    public View() throws IOException {
        this.stage1=new Stage();
        CreateMenu();
        stage1.show();
    }

    public Stage CreateMenu() throws IOException{
        var loader1 = new FXMLLoader(getClass().getResource("Menu.fxml"));
        AnchorPane root = (AnchorPane) loader1.load();
        Scene scene1 = new Scene(root);
        stage1.setScene(scene1);
        stage1.setTitle("Menu");

        return stage1;
    }

    public static void exitMenu(){
        stage1.close();
    }
}

class MapView {
    Stage stage2;
    Model model;
    public MapView(Model model, String name)throws IOException{
        this.stage2=new Stage();
        this.model=model;
        var loader = new FXMLLoader(getClass().getResource(name));
        stage2.setScene(loader.load());
        Controller controller = loader.getController();
        controller.init(model);
        stage2.setTitle("Map");
        stage2.show();

    }
}

