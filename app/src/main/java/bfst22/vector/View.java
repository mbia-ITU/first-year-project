package bfst22.vector;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class View {
    private static Stage stage1;

    public View() throws IOException {
        View.stage1 = new Stage(); //maybe change back to this.stage1
        CreateMenu();
        stage1.setResizable(false);
        stage1.show();
    }

    public Stage CreateMenu() throws IOException {
        var loader1 = new FXMLLoader(getClass().getResource("Menu.fxml"));
        AnchorPane root = (AnchorPane) loader1.load();
        Scene scene1 = new Scene(root);
        stage1.setScene(scene1);
        stage1.setTitle("Menu");

        return stage1;
    }

    public static void exitMenu() {
        stage1.close();
    }
}

class MapView {
    public MapView(Model model, Stage mapStage, String name) throws IOException {
        mapStage.setWidth(800);
        mapStage.setHeight(400);

        mapStage.show();
        var loader = new FXMLLoader(getClass().getResource(name));
        mapStage.setScene(loader.load());
        Controller controller = loader.getController();
        controller.init(model);
        mapStage.setTitle("Map");
    }
}
