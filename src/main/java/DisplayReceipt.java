
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Pair;

public class DisplayReceipt {

    public static ArrayList<Entry> entries=new ArrayList<>();
    public static double subtotal;
    public static int orderId;
    public static boolean loaded=false;
    public static ArrayList<String> extraCostName;
    public static ArrayList<Double> extraCostPrice;
    public static HashMap<String,Double> overarchingCosts;
    public static HashMap<String,Integer> numberOfSides;
    public static HashMap<String,Integer> numberOfProteins;
    public static HashMap<String,ArrayList<Pair<Double, Integer>>> ingredients;
    public static final DecimalFormat df = new DecimalFormat("$0.00");
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void load() throws SQLException, ClassNotFoundException{
        if(loaded)
            return;
        
        ResultSet rs=DBUtil.dbExecuteQuery("SELECT MAX(order_id) FROM order_history;");
        rs.next();
        orderId = rs.getInt(1)+1;

        ResultSet rsin=DBUtil.dbExecuteQuery("SELECT * FROM ingredients_needed;");
        ingredients=new HashMap<>();
        while(rsin.next()){
            Pair<Double,Integer> values=new Pair<>(rsin.getDouble(3),rsin.getInt(2));
            if(ingredients.get(rsin.getString(1))==null){
                ingredients.put(rsin.getString(1), new ArrayList<Pair<Double,Integer>>());
            }
            ingredients.get(rsin.getString(1)).add(values);
        }


        ResultSet rscost=DBUtil.dbExecuteQuery("SELECT Menu_Name,extra_cost FROM menu;");
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

        ResultSet sidesResultSet=DBUtil.dbExecuteQuery("SELECT item_name,Side_Count,Protein_count FROM overarching_items");
        numberOfSides=new HashMap<>();
        numberOfProteins=new HashMap<>();
        while(sidesResultSet.next()){
            numberOfSides.put(sidesResultSet.getString(1),sidesResultSet.getInt(2));
            numberOfProteins.put(sidesResultSet.getString(1),sidesResultSet.getInt(3));
        }

        loaded=true;
    }

    public static void removeEntry(Entry entry) {
        DisplayReceipt.entries.remove(entry);
        subtotal-=entry.price;
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

    public static void updateIngredients(Entry entry) throws SQLException, ClassNotFoundException{
        boolean half=entry.isHalfSide();
        if(entry.side1!=null){
            loopEntry(ingredients.get(entry.side1), half);
        }
        if(entry.side2!=null){
            loopEntry(ingredients.get(entry.side2), half);
        }
        if(entry.protien1!=null){
            loopEntry(ingredients.get(entry.protien1), half);
        }
        if(entry.protien2!=null){
            loopEntry(ingredients.get(entry.protien2), half);
        }
        if(entry.protien3!=null){
            loopEntry(ingredients.get(entry.protien3), half);
        }
        if(entry.miscItem!=null){
            loopEntry(ingredients.get(entry.miscItem), half);
        }
    }
    
    public static void loopEntry(ArrayList<Pair<Double, Integer>> rs,boolean half) throws SQLException, ClassNotFoundException{
        if(rs==null){
            return;
        }
        String amounts="";
        for(Pair<Double, Integer> item:rs){
            if(half){
                amounts+="WHEN "+Integer.toString(item.getValue())+" THEN Quantity - "+Double.toString(item.getKey()*.5);
            }
            else{
                amounts+="WHEN "+Integer.toString(item.getValue())+" THEN Quantity - "+Double.toString(item.getKey());
            }
        }
        DBUtil.dbExecuteUpdate("UPDATE inventory SET Quantity = CASE inventory_id "+amounts+" ELSE Quantity END");
    }

}
