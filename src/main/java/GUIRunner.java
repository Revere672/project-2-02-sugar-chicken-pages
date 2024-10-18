import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

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

    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage the primary stage for this application
     * @throws Exception if an error occurs during application startup
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        scenes = new HashMap<>();
        build = new GUIBuilder();

        // Load the initial scene
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        scenes.put("login", new Scene(root));
        
        stage = primaryStage;
        stage.setScene(scenes.get("login"));
        stage.show();
    }

    /**
     * The main method to start the application.
     *
     * @param args the command line arguments
     * @throws ClassNotFoundException if the JDBC driver class is not found
     * @throws SQLException if a database access error occurs
     * @throws IOException if an I/O error occurs
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        Properties properties = new Properties();
        try (InputStream input = GUIRunner.class.getResourceAsStream("/gradle.properties")) {
            //if ran through the jar
            properties.load(input);
        }
        catch(Exception e) {
            //if ran locally through gradle run command
            InputStream input = new FileInputStream("gradle.properties");
            properties.load(input);
        }

        String host = properties.getProperty("PGHOST");
        String user = properties.getProperty("PGUSER");
        String password = properties.getProperty("PGPASSWORD");

        DBUtil.dbConnect(host, user, user, password);
        
        launch();
    }

    /**
     * Changes the scene to the specified scene name.
     *
     * @param sceneName the name of the scene to switch to
     * @return the new scene
     */
    public static Scene changeScene(String sceneName) {
        Scene newScene = scenes.get(sceneName);
        
        if (newScene != null) {
            // Store the current width and height of the stage
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            // Set the new scene
            stage.setScene(newScene);
            stage.show();

            // Restore the original width and height
            stage.setWidth(currentWidth);
            stage.setHeight(currentHeight);

            // Optional: Update button states for cashier scenes
            if (sceneName.equals("cashier1") || sceneName.equals("cashier2")) {
                CashierController cashierController = (CashierController) newScene.getUserData();
                if (cashierController != null) {
                    cashierController.updateButtonStates();
                }
            }
        }

        return newScene;
    }
}
