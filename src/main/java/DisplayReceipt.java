
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DisplayReceipt {

    public static ArrayList<Entry> entries=new ArrayList<>();
    public static double subtotal;
    public static int orderId;
    public static boolean loaded=false;
    public static ArrayList<String> extraCostName;
    public static ArrayList<Double> extraCostPrice;
    public static HashMap<String,Double> overarchingCosts;
    public static final DecimalFormat df = new DecimalFormat("$0.00");
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void load() throws SQLException, ClassNotFoundException{
        if(loaded)
            return;
        
        ResultSet rs=DBUtil.dbExecuteQuery("SELECT MAX(order_id) FROM order_history;");
        rs.next();
        orderId = rs.getInt(1)+1;

        ResultSet rscost=DBUtil.dbExecuteQuery("SELECT Menu_Name,extra_cost FROM menu where extra_cost>0;");
        extraCostName=new ArrayList<>();
        extraCostPrice=new ArrayList<>();
        while(rscost.next()){
            extraCostName.add(rscost.getString(1));
            extraCostPrice.add(rscost.getDouble(2));
        }

        ResultSet overarchResultSet=DBUtil.dbExecuteQuery("SELECT item_name,item_cost FROM overarching_items");
        overarchingCosts=new HashMap<>();
        while(overarchResultSet.next()){
            overarchingCosts.put(overarchResultSet.getString(1),overarchResultSet.getDouble(2));
        }

        loaded=true;
    }

    public static ArrayList<Entry> getEntries() {
        return entries;
    }

    public static void addEntry(Entry entry) {
        DisplayReceipt.entries.add(entry);
        subtotal+=entry.price;
    }

    public static void updateRecipt(Scene scene){
        try {
            load();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        TextArea receipt = (TextArea) scene.lookup("#ReceiptText");
        TextArea priceArea = (TextArea) scene.lookup("#PriceArea");
        TextField sub = (TextField) scene.lookup("#SubtotalValue");
        TextField total = (TextField) scene.lookup("#TotalValue");
        // for (javafx.scene.Node node : scene.getRoot().getChildrenUnmodifiable()) {
        //     System.out.println(node.getId());
        // }
        String text="Order #"+orderId+"\n"+"\n";
        String priceText = "\n"+"\n";
        for(Entry e:entries){
            text+=e.formatedString();
            priceText+=e.getPrice();
        }
        receipt.setText(text);
        priceArea.setText(priceText);
        sub.setText(df.format(subtotal));
        total.setText(df.format(subtotal*1.0875));
    }

}
