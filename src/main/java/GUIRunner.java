
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
        ResultSet lastZ= DBUtil.dbExecuteQuery("SELECT * FROM z_report");
        lastZ.next();
        Timestamp lastZReport=lastZ.getTimestamp(1);

        ResultSet currentDate =DBUtil.dbExecuteQuery("SELECT CURRENT_TIMESTAMP");
        currentDate.next();

        ResultSet rs= DBUtil.dbExecuteQuery("WITH last_day (order_id) AS ( SELECT order_id FROM order_history WHERE order_time > '"+lastZReport+"')" + 
                        "SELECT Item_Name, SUM(Item_Cost) FROM Order_Items JOIN last_day ON Order_Items.Order_Id = last_day.order_id GROUP BY Item_Name;");
                        
        ResultSet rsTotal =DBUtil.dbExecuteQuery("SELECT SUM(Total_Price) FROM order_history WHERE order_time>'"+lastZReport+"'");
        rsTotal.next();
        ArrayList<String> result=new ArrayList<>();
        
        
        result.add(lastZReport.toString());
        result.add(currentDate.getTimestamp(1).toString());
        result.add(Double.toString(rsTotal.getDouble(1)));

        while(rs.next()){
            result.add(Double.toString(rs.getDouble(2)));
        }
        
        return result;
    }
    public static ArrayList<String> zReport() throws ClassNotFoundException, SQLException{
        ResultSet lastZ= DBUtil.dbExecuteQuery("SELECT * FROM z_report");
        lastZ.next();
        Timestamp lastZReport=lastZ.getTimestamp(1);

        ResultSet currentDate =DBUtil.dbExecuteQuery("SELECT CURRENT_TIMESTAMP");
        currentDate.next();

        ResultSet rs= DBUtil.dbExecuteQuery("WITH last_day (order_id) AS ( SELECT order_id FROM order_history WHERE order_time > '"+lastZReport+"')" + 
                        "SELECT Item_Name, SUM(Item_Cost) FROM Order_Items JOIN last_day ON Order_Items.Order_Id = last_day.order_id GROUP BY Item_Name;");
                        
        ResultSet rsTotal =DBUtil.dbExecuteQuery("SELECT SUM(Total_Price) FROM order_history WHERE order_time>'"+lastZReport+"'");
        rsTotal.next();
        ArrayList<String> result=new ArrayList<>();
        
        
        result.add(lastZReport.toString());
        result.add(currentDate.getTimestamp(1).toString());
        result.add(Double.toString(rsTotal.getDouble(1)));

        while(rs.next()){
            result.add(Double.toString(rs.getDouble(2)));
        }
        
        DBUtil.dbExecuteUpdate("UPDATE z_report SET last_report = \'"+result.get(1)+"\'");

        return result;
    }
}
