
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DisplayReceipt {

    public static ArrayList<Entry> entries=new ArrayList<>();
    public static double subtotal;
    public static int orderNumber;
    public static boolean loaded=false;

    public static void load() throws SQLException, ClassNotFoundException{
        if(loaded)
            return;
        
        ResultSet rs=DBUtil.dbExecuteQuery("SELECT MAX(order_id) FROM order_history");
        rs.next();
        orderNumber = rs.getInt(1);
        
        entries.add(new Entry("drink","WaterBottle",1.0));

        loaded=true;
    }

    public static ArrayList<Entry> getEntries() {
        return entries;
    }

    public static void addEntry(Entry entry) {
        DisplayReceipt.entries.add(entry);
    }

    public static void updateRecipt(Scene scene){
        try {
            load();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        TextArea receipt = (TextArea) scene.lookup("#ReceiptText");
        TextField sub = (TextField) scene.lookup("#subtotal");
        TextField total = (TextField) scene.lookup("#total");
        // for (javafx.scene.Node node : scene.getRoot().getChildrenUnmodifiable()) {
        //     System.out.println(node.getId());
        // }
        String text="Order #"+orderNumber+"\n";
        for(Entry e:entries){
            text+=e.formatedString();
        }
        receipt.setText(text);
        sub.setText(Double.toString(subtotal));
        total.setText(Double.toString(subtotal*1.875));
    }



    
}
