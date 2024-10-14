import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AnalysisController {
    private Scene scene;    


    public static ArrayList<String> xReportDB() throws ClassNotFoundException, SQLException{
        ResultSet lastZ= DBUtil.dbExecuteQuery("SELECT * FROM z_report");
        lastZ.next();
        Timestamp lastZReport=lastZ.getTimestamp(1);

        ResultSet currentDate =DBUtil.dbExecuteQuery("SELECT CURRENT_TIMESTAMP");
        currentDate.next();

        ResultSet rs= DBUtil.dbExecuteQuery("WITH last_day (order_id) AS ( SELECT order_id FROM order_history WHERE order_time > '"+lastZReport+"')" + 
                        "SELECT Item_Name, SUM(Item_Cost) FROM Order_Items JOIN last_day ON Order_Items.Order_Id = last_day.order_id GROUP BY Item_Name;");
                        
        ResultSet rsTotal =DBUtil.dbExecuteQuery("SELECT SUM(Total_Price),Count(*) FROM order_history WHERE order_time>'"+lastZReport+"'");
        rsTotal.next();
        ArrayList<String> result=new ArrayList<>();
        
        double differenceInHours = (currentDate.getTimestamp(1).getTime() - lastZReport.getTime())/60000/60;
        
        result.add(lastZReport.toString());
        result.add(currentDate.getTimestamp(1).toString());
        result.add(Integer.toString(rsTotal.getInt(2)));
        result.add(Double.toString(Math.round((rsTotal.getInt(2)/differenceInHours) * 100) / 100));
        result.add(Double.toString(rsTotal.getDouble(1)));

        boolean flag=true;
        for(int x=0;x<5;x++){
            if(flag&&rs.next()){
                result.add(Double.toString(rs.getDouble(2)));
            }
            else{
                result.add("0.0");
                flag=false;
            }
        }
        
        return result;
    }
    public static ArrayList<String> zReportDB() throws ClassNotFoundException, SQLException{
        ArrayList<String> result=xReportDB();
        
        DBUtil.dbExecuteUpdate("UPDATE z_report SET last_report = \'"+result.get(1)+"\'");

        return result;
    }
    
    @FXML
    private void xReport(ActionEvent event) throws SQLException, ClassNotFoundException {
        scene=((Node)event.getSource()).getScene();

        ((TextField)scene.lookup("#Title")).setText("X Report");

        scene.lookup("#ReportPage").setDisable(false);
        scene.lookup("#ReportPage").setOpacity(1);
        ArrayList<String> values = xReportDB();
        ((TextField)scene.lookup("#StartTime")).setText(values.get(0).replaceAll("\\..*",""));
        ((TextField)scene.lookup("#EndTime")).setText(values.get(1).replaceAll("\\..*",""));

        ((TextField)scene.lookup("#OrdersPlaced")).setText(values.get(2));
        ((TextField)scene.lookup("#TotalSales")).setText("$"+values.get(4));
        ((TextField)scene.lookup("#SalesPerHour")).setText(values.get(3));

        ((TextField)scene.lookup("#BowlSales")).setText("$"+values.get(7));
        ((TextField)scene.lookup("#PlateSales")).setText("$"+values.get(9));
        ((TextField)scene.lookup("#BiggerPlateSales")).setText("$"+values.get(6));
        String addedValues=Double.toString(Double.parseDouble(values.get(8))+Double.parseDouble(values.get(5)));
        ((TextField)scene.lookup("#OtherSales")).setText("$"+addedValues);
    }
    @FXML
    private void zReport(ActionEvent event) throws SQLException, ClassNotFoundException {
        scene=((Node)event.getSource()).getScene();

        ((TextField)scene.lookup("#Title")).setText("Z Report");

        scene.lookup("#ReportPage").setDisable(false);
        scene.lookup("#ReportPage").setOpacity(1);
        ArrayList<String> values = zReportDB();
        ((TextField)scene.lookup("#StartTime")).setText(values.get(0).replaceAll("\\..*",""));
        ((TextField)scene.lookup("#EndTime")).setText(values.get(1).replaceAll("\\..*",""));

        ((TextField)scene.lookup("#OrdersPlaced")).setText(values.get(2));
        ((TextField)scene.lookup("#TotalSales")).setText("$"+values.get(4));
        ((TextField)scene.lookup("#SalesPerHour")).setText(values.get(3));

        ((TextField)scene.lookup("#BowlSales")).setText("$"+values.get(7));
        ((TextField)scene.lookup("#PlateSales")).setText("$"+values.get(9));
        ((TextField)scene.lookup("#BiggerPlateSales")).setText("$"+values.get(6));
        String addedValues=Double.toString(Double.parseDouble(values.get(8))+Double.parseDouble(values.get(5)));
        ((TextField)scene.lookup("#OtherSales")).setText("$"+addedValues);
    }

    @FXML
    private void closeReport(ActionEvent event){
        scene=((Node)event.getSource()).getScene();
        scene.lookup("#ReportPage").setDisable(true);
        scene.lookup("#ReportPage").setOpacity(0);
    }


    @FXML
    private void changeToEmployees(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("employees");
    }
    @FXML
    private void changeToInventory(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("inventory");
    }
    @FXML
    private void changeToOrderHistory(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
         GUIRunner.changeScene("order_history");
    }
    @FXML
    private void changeToCashier(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("cashier1");
   }

    @FXML
    private Button cashier;
    @FXML
    private Button analysis;
    @FXML
    private Button inventory;
    @FXML
    private Button employees;
    @FXML
    private Button order_history;
}
