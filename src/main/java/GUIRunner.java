import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.io.InputStream;
import java.util.Properties;
import java.io.FileInputStream;

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
        scenes = new HashMap<>();
        build = new GUIBuilder();

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        scenes.put("login", new Scene(root));
        GUIRunner.stage = stage;
        stage.setScene(scenes.get("login"));
        stage.show();
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        Properties properties = new Properties();
        try (InputStream input = GUIRunner.class.getResourceAsStream("/gradle.properties")) {
            properties.load(input);
        }

        String host = properties.getProperty("PGHOST");
        String user = properties.getProperty("PGUSER");
        String password = properties.getProperty("PGPASSWORD");

        DBUtil.dbConnect(host, user, user, password);
        
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
