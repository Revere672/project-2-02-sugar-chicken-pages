import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AnalysisController {
    private Scene scene;    
    enum QueryLevel {
        ONE,
        SIX,
        DAY
    }


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
    private void openGraph(ActionEvent event) throws SQLException, ClassNotFoundException{
        scene=((Node)event.getSource()).getScene();
        scene.lookup("#GraphPage").setDisable(false);
        scene.lookup("#GraphPage").setOpacity(1);
        scene.lookup("#GraphPage").setVisible(true);
        ChoiceBox box=((ChoiceBox)scene.lookup("#InventoryBox"));

        if(box.getItems().isEmpty()){
            ResultSet rs=DBUtil.dbExecuteQuery("SELECT * FROM inventory;");
            while(rs.next()){
                box.getItems().add(rs.getString(2));
            }
        }
    }

    private ResultSet runGraphQuery(Timestamp start,Timestamp end,String productName,QueryLevel ql)throws SQLException, ClassNotFoundException{
        String query="WITH Hour_Range AS ("+
    "SELECT generate_series("+
        "'"+start.toString()+"'::timestamp,  "+
      "  '"+end.toString()+"'::timestamp,   "+
       " INTERVAL '1 hour'"+
   " ) AS Order_Hour"+
"),"+
"Hourly_Usage AS ("+
   " SELECT "+
      "  i.Inventory_ID, "+
      "  i.Product_Name, "+
      "  DATE_TRUNC('hour', o.Order_Time) AS Order_Hour, "+
      "  SUM(ing.Quantity_Needed) AS Total_Quantity_Used "+
    "FROM "+
       " Inventory i "+
   " LEFT JOIN Ingredients_Needed ing ON i.Inventory_ID = ing.Inventory_ID "+
    "LEFT JOIN Order_Items oi ON ing.Menu_Name = oi.Side_1 OR "+
                               " ing.Menu_Name = oi.Side_2 OR "+
                                "ing.Menu_Name = oi.Protein_1 OR "+
                               " ing.Menu_Name = oi.Protein_2 OR "+
                                "ing.Menu_Name = oi.Protein_3 OR "+
                                "ing.Menu_Name = oi.Misc_Item "+
    "JOIN Order_History o ON oi.Order_ID = o.Order_ID "+
    "WHERE "+
        "i.product_name = '"+productName+"' AND   "+
        "o.Order_Time >= '"+start.toString()+"'::timestamp AND  "+
        "o.Order_Time < ('"+end.toString()+"'::timestamp + INTERVAL '1 hour')   "+
    "GROUP BY "+
        "i.Inventory_ID, i.Product_Name, DATE_TRUNC('hour', o.Order_Time) "+
") "+
"SELECT "+
    "hr.Order_Hour, "+
    "i.Inventory_ID, "+
    "i.Product_Name, "+
    "COALESCE(hu.Total_Quantity_Used, 0) AS Total_Quantity_Used "+
"FROM "+
    "Hour_Range hr "+
"LEFT JOIN Hourly_Usage hu ON hr.Order_Hour = hu.Order_Hour "+
"LEFT JOIN Inventory i ON hu.Inventory_ID = i.Inventory_ID "+
"ORDER BY "+
    "hr.Order_Hour;";

        if(ql==QueryLevel.SIX){
            query=query.replaceAll("1 hour","6 hours");
        }
        if(ql==QueryLevel.DAY){
            query=query.replaceAll("hour","day");
        }


       return DBUtil.dbExecuteQuery(query);
    }

    @FXML
    private void displayGraph(ActionEvent event)throws SQLException, ClassNotFoundException {
        scene=((Node)event.getSource()).getScene();
        scene.lookup("#GraphPage").setDisable(true);
        scene.lookup("#GraphPage").setOpacity(0);
        scene.lookup("#GraphPane").setDisable(false);
        scene.lookup("#GraphPane").setOpacity(1);
        LocalDate s=((DatePicker)scene.lookup("#StartTimeGraph")).getValue();
        LocalDate e=((DatePicker)scene.lookup("#EndTimeGraph")).getValue();
        if(s==null||e==null){
            return;
        }
        Timestamp start = Timestamp.valueOf(s.atStartOfDay());
        long startTime =start.getTime()/3600000;
        Timestamp end = Timestamp.valueOf(e.plusDays(1).atStartOfDay());
        long endTime =end.getTime()/3600000;

        QueryLevel ql=QueryLevel.ONE;

        if(endTime-startTime>72){
            ql=QueryLevel.SIX;
        }
        if(endTime-startTime>24*8){
            ql=QueryLevel.DAY;
            
        }

        String[] items=((TextArea)scene.lookup("#AllItems")).getText().split(",");

        ScatterChart<Number,Number> scatter=((ScatterChart<Number,Number>)scene.lookup("#GraphActual"));
        scatter.getXAxis().setLabel("Hours since start");
        scatter.getYAxis().setLabel("Quantity used");
        for(String item:items){
            XYChart.Series series = new XYChart.Series();
            series.setName(item);

            ResultSet rs=runGraphQuery(start,end,item,ql);
            while(rs.next()){
                series.getData().add(new XYChart.Data((double)Math.toIntExact(rs.getTimestamp(1).getTime()/3600000-startTime),rs.getDouble(4)));
            }
            scatter.getData().addAll(series);
        }
        
        
    }

    @FXML
    private void choiceBoxed(ActionEvent event){
        scene=((Node)event.getSource()).getScene();
        String items=((TextArea)scene.lookup("#AllItems")).getText();
        ChoiceBox box=((ChoiceBox)scene.lookup("#InventoryBox"));
        if(items.length()>0){
            items=items+","+box.getValue().toString();
        }
        else{
            items=box.getValue().toString();
        }
        ((TextArea)scene.lookup("#AllItems")).setText(items);
    }

    @FXML
    private void closeReport(ActionEvent event){
        scene=((Node)event.getSource()).getScene();
        scene.lookup("#ReportPage").setDisable(true);
        scene.lookup("#ReportPage").setOpacity(0);
    }

    @FXML
    private void closeGraph(ActionEvent event){
        scene=((Node)event.getSource()).getScene();
        scene.lookup("#GraphPage").setDisable(true);
        scene.lookup("#GraphPage").setOpacity(0);
        scene.lookup("#GraphPane").setDisable(true);
        scene.lookup("#GraphPane").setOpacity(0);
        ((ScatterChart<Number,Number>)scene.lookup("#GraphActual")).getData().clear();
        ((TextArea)scene.lookup("#AllItems")).clear();
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
    private void logOut(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
        Scene login = new Scene(FXMLLoader.load(getClass().getResource("/fxml/login.fxml")));
        System.out.println("Logged Out");
        GUIRunner.stage.setScene(login);
        GUIRunner.stage.show();
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
    @FXML
    private Button log_out_button;
}
