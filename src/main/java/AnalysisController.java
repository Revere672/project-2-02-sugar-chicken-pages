import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Arrays;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

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
        
        
        result.add(lastZReport.toString());
        result.add(currentDate.getTimestamp(1).toString());
        result.add(Integer.toString(rsTotal.getInt(2)));
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
        
        
        result.add(lastZReport.toString());
        result.add(currentDate.getTimestamp(1).toString());
        result.add(Integer.toString(rsTotal.getInt(2)));
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
        ((TextField)scene.lookup("#TotalSales")).setText(values.get(3));

        ((TextField)scene.lookup("#BowlSales")).setText(values.get(4));
        ((TextField)scene.lookup("#PlateSales")).setText(values.get(5));
        ((TextField)scene.lookup("#BiggerPlateSales")).setText(values.get(6));
        String addedValues=Double.toString(Double.parseDouble(values.get(7))+Double.parseDouble(values.get(8)));
        ((TextField)scene.lookup("#OtherSales")).setText(addedValues);
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
        ((TextField)scene.lookup("#TotalSales")).setText(values.get(3));

        ((TextField)scene.lookup("#BowlSales")).setText(values.get(4));
        ((TextField)scene.lookup("#PlateSales")).setText(values.get(5));
        ((TextField)scene.lookup("#BiggerPlateSales")).setText(values.get(6));
        String addedValues=Double.toString(Double.parseDouble(values.get(7))+Double.parseDouble(values.get(8)));
        ((TextField)scene.lookup("#OtherSales")).setText(addedValues);
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
    private void logOut(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
        Scene login = new Scene(FXMLLoader.load(getClass().getResource("/fxml/login.fxml")));
        System.out.println("Logged Out");
        GUIRunner.stage.setScene(login);
        GUIRunner.stage.show();
    }


    //////SALES REPORT

    @FXML
    private void Sales_Report(ActionEvent actionEvent)throws SQLException, ClassNotFoundException, IOException{
        String q = "SELECT menu_name FROM menu;";
        ResultSet rs = DBUtil.dbExecuteQuery(q);

        ObservableList<String> menuItemsqueered = FXCollections.observableArrayList();
        while(rs.next()){
            menuItemsqueered.add(rs.getString("menu_name"));
        }

        choose_items.setItems(menuItemsqueered);
        // String start = startTime.getText();
        // String end = endTime.getText();

        saleReport_pane.setVisible(true);
    }

    // @FXML
    // private void Sales_Report(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException{
        
    //     saleReport_pane.setVisible(true);
    // }

    @FXML
    private void Confirm_Button(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException{
        
        confirm_pane.setVisible(true);
        confirm_pane.toFront();

        Timestamp start = Timestamp.valueOf(startTime.getValue().atStartOfDay());
        //long s =start.getTime()/3600000;
        Timestamp end = Timestamp.valueOf(endTime.getValue().plusDays(1).atStartOfDay());
        //long e =end.getTime()/3600000;
        
        result_text.setText("Sales Report from " + start.toLocalDateTime().toLocalDate().toString() + " - " + end.toLocalDateTime().toLocalDate().toString() + "\n\n");
        result_text.setText(result_text.getText() + "Total Counts of Selected Items:\n\n");
        //String qq = "SELECT ORD* FROM order_history";
        String[] arr = sales_textArea.getText().split(",");
        for(String item : arr){
            String qq = "SELECT\r\n" + //
                        "        m.Menu_ID,\r\n" + //
                        "        m.Menu_Name,\r\n" + //
                        "        COUNT(m.Menu_Name)\r\n" + //
                        "    FROM\r\n" + //
                        "        Menu m\r\n" + //
                        "    LEFT JOIN Order_Items oi ON m.Menu_Name = oi.Side_1 OR\r\n" + //
                        "                                m.Menu_Name = oi.Side_2 OR\r\n" + //
                        "                                m.Menu_Name = oi.Protein_1 OR\r\n" + //
                        "                                m.Menu_Name = oi.Protein_2 OR\r\n" + //
                        "                                m.Menu_Name = oi.Protein_3 OR\r\n" + //
                        "                                m.Menu_Name = oi.Misc_Item\r\n" + //
                        "    JOIN Order_History o ON oi.Order_ID = o.Order_ID\r\n" + //
                        "    WHERE\r\n" + //
                        "        m.Menu_Name = '"+item+"' AND  \r\n" + //
                        "        o.Order_Time >= '"+start.toString()+"' AND  \r\n" + //
                        "        o.Order_Time < '"+end.toString()+"'  \r\n" + //
                        "    GROUP BY\r\n" + //
                        "        m.Menu_Name;";
                
            ResultSet rs = DBUtil.dbExecuteQuery(qq);
            if (rs.next()) {
                result_text.setText(result_text.getText() + rs.getString("Menu_Name") + ": " + rs.getString("COUNT") + "\n\n");
            }
        }

        result_text.setText(result_text.getText() + "Counts of Selected Items by hour:\n\n");
        for (String item : arr) {
            String qq =        "SELECT\r\n" + //
                        "        m.Menu_ID,\r\n" + //
                        "        m.Menu_Name,\r\n" + //
                        "        DATE_TRUNC('hour', o.Order_Time) AS Order_Hour,\r\n" + //
                        "        COUNT(m.Menu_Name)\r\n" + //
                        "    FROM\r\n" + //
                        "        Menu m\r\n" + //
                        "    LEFT JOIN Order_Items oi ON m.Menu_Name = oi.Side_1 OR\r\n" + //
                        "                                m.Menu_Name = oi.Side_2 OR\r\n" + //
                        "                                m.Menu_Name = oi.Protein_1 OR\r\n" + //
                        "                                m.Menu_Name = oi.Protein_2 OR\r\n" + //
                        "                                m.Menu_Name = oi.Protein_3 OR\r\n" + //
                        "                                m.Menu_Name = oi.Misc_Item\r\n" + //
                        "    JOIN Order_History o ON oi.Order_ID = o.Order_ID\r\n" + //
                        "    WHERE\r\n" + //
                        "        m.Menu_Name = '"+item+"' AND  \r\n" + //
                        "        o.Order_Time >= '"+start.toString()+"' AND  \r\n" + //
                        "        o.Order_Time < '"+end.toString()+"'  \r\n" + //
                        "    GROUP BY\r\n" + //
                        "        m.Menu_Name, DATE_TRUNC('hour', o.Order_Time);";
            
            result_text.setText(result_text.getText() + item + " by hour:\n");
            ResultSet rs = DBUtil.dbExecuteQuery(qq);
            LocalDate currentDate = start.toLocalDateTime().toLocalDate();
            result_text.setText(result_text.getText() + "\t" + currentDate.toString() + ":\n");
            while (rs.next()) {
                LocalDate thisDate = rs.getDate("Order_Hour").toLocalDate();
                if (!thisDate.equals(currentDate)) {
                    currentDate = thisDate;
                    result_text.setText(result_text.getText() + "\t" + currentDate.toString() + ":\n");
                }

                LocalTime thisTime = rs.getTimestamp("Order_Hour").toLocalDateTime().toLocalTime();
                int count = rs.getInt("COUNT");
                result_text.setText(result_text.getText() + "\t\t" + thisTime + ": " + count + "\n");
            }
        }
    }

    @FXML
    private void Cancel_Button(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException{
        
        //get rid of the subscreen on analysis
        saleReport_pane.setVisible(false);
    }

    @FXML
    private void close_sale_report(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException{
        
        //get rid of the subscreen on analysis
        confirm_pane.setVisible(false);
    }

    @FXML
    private void clear_selected_items(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException{
        
        //get rid of the subscreen on analysis
        sales_textArea.setText("");
    }

    @FXML
    private void SelectItems(ActionEvent actionEvent)throws SQLException, ClassNotFoundException, IOException{
        String items = sales_textArea.getText();
        
        
        if(items.length()>0){
            items=items+","+choose_items.getValue().toString();
        }
        else{
            items=choose_items.getValue().toString();
        }

        sales_textArea.setText(items);
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

    
    ////////////////////SALES REPORT
    @FXML
    private Button sale_report_button;
    @FXML
    private Pane saleReport_pane;
    @FXML
    private ComboBox<String> choose_items;
    @FXML
    private DatePicker startTime;
    @FXML
    private DatePicker endTime;
    @FXML
    private Button confirm_button;
    @FXML
    private Button cancel_button;

    @FXML
    private Pane confirm_pane;
    @FXML
    private TextArea result_text;

    @FXML
    private TextArea sales_textArea;
}
