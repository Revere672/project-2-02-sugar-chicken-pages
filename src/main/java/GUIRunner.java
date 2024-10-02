
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIRunner extends Application {

    public static boolean isManager;
    public static int currentUser;
    public static HashMap<String, Scene> scenes;
    public static Stage stage;
    public static GUIBuilder build;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        scenes.put("login", new Scene(root));
        GUIRunner.stage = stage;
        stage.setScene(scenes.get("login"));
        stage.show();
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        DBUtil.dbConnect(args[0], args[1], args[1], args[2]);
        scenes = new HashMap<>();
        build = new GUIBuilder(args);
        launch();
    }

    public static Scene changeScene(String scene_Name) {
        Scene newScene = scenes.get(scene_Name);
        stage.setScene(scenes.get(scene_Name));
        stage.show();

        if (scene_Name.equals("cashier1") || scene_Name.equals("cashier2")) {
            CashierController cashierController = (CashierController) newScene.getUserData();
            if (cashierController != null) {
                cashierController.updateButtonStates();
            }
        }
        return newScene;
    }
}
