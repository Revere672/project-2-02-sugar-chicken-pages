
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    public static Date lastZReport;

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

    public static ArrayList<String> xReport() throws ClassNotFoundException, SQLException{
        ResultSet rs= DBUtil.dbExecuteQuery("WITH last_Day AS ( SELECT Total_Price,order_time FROM order_history WHERE order_time>'"+lastZReport+"'"
                                                +"SELECT EXTRACT(HOUR FROM Order_Time) AS Hour_of_Day, COUNT(*) AS Order_Count, SUM(Total_Price) AS Total_Sales FROM last_Day GROUP BY EXTRACT (HOUR FROM Order_Time) ORDER BY Hour_of_Day;");

        ArrayList<String> result=new ArrayList<>();
        
        while(rs.next()){
            result.add(rs.getString(0));
        }
        
        return result;
    }
    public static ArrayList<String> zReport() throws ClassNotFoundException, SQLException{
        ResultSet rs= DBUtil.dbExecuteQuery("WITH last_Day AS ( SELECT Total_Price,order_time FROM order_history WHERE order_time>'"+lastZReport+"'"
                                                +"SELECT EXTRACT(HOUR FROM Order_Time) AS Hour_of_Day, COUNT(*) AS Order_Count, SUM(Total_Price) AS Total_Sales FROM last_Day GROUP BY EXTRACT (HOUR FROM Order_Time) ORDER BY Hour_of_Day;");

        ArrayList<String> result=new ArrayList<>();
        
        while(rs.next()){
            result.add(rs.getString(0));
        }
        lastZReport = new Date(new java.util.Date().getTime());
        return result;
    }
}
