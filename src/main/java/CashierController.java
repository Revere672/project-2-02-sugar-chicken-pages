
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;

public class CashierController implements Initializable {

    private Scene scene;
    public static ArrayList<ToggleButton> selected=new ArrayList<>();
    public static Entry workingEntry;
    public static String special;

    public void switch_to_cashier1(ActionEvent event) throws IOException{
        scene = GUIRunner.changeScene("cashier1");
        DisplayReceipt.updateRecipt(scene);
    }

    
    public Scene switch_to_cashier2(ActionEvent event) throws IOException{
        scene = GUIRunner.changeScene("cashier2");
        DisplayReceipt.updateRecipt(scene);
        return scene;
    }

    public void addToOrder(ActionEvent event) throws IOException{
        DisplayReceipt.addEntry(workingEntry);

        scene = ((Node)event.getSource()).getScene();
        DisplayReceipt.updateRecipt(scene);
        ((Node)event.getSource()).setOpacity(0);
        ((Node)event.getSource()).setDisable(true);
        deSelectAll();
        switch_to_cashier1(event);
        workingEntry=null;
    }

    public void done(ActionEvent event) throws IOException{
        deSelectAll();
        ((Node)event.getSource()).setOpacity(0);
        ((Node)event.getSource()).setDisable(true);
        workingEntry=null;
        switch_to_cashier1(event);
    }

    public void deSelectAll(){
        for(ToggleButton t : selected){
            System.out.println(t.getId());
            t.setSelected(false);
        }
        selected.clear();
   }

   public void deSelect(String s){
        for(ToggleButton t : selected){
            if(t.getId().equals(s)){
            t.setSelected(false);
            }
        }
   }

   public void undo(ActionEvent event){
        ArrayList<Entry> entries=DisplayReceipt.getEntries();
        if(entries.isEmpty()){
            return;
        }
        DisplayReceipt.subtotal-=entries.get(entries.size()-1).price;
        entries.remove(entries.size()-1);
        scene=((Node)event.getSource()).getScene();
        DisplayReceipt.updateRecipt(scene);
   }

    public void FinishOrder(ActionEvent event) throws IOException, SQLException, ClassNotFoundException{
        if(DisplayReceipt.getEntries().isEmpty()){
            return;
        }
        ResultSet rs=DBUtil.dbExecuteQuery("SELECT MAX(order_id) FROM order_history;");
        rs.next();
        DisplayReceipt.orderId = rs.getInt(1)+1;
        
        DBUtil.dbExecuteUpdate("INSERT INTO Order_History VALUES ("+DisplayReceipt.orderId+",\'"+DisplayReceipt.dateFormat.format(new Date())+"\',"+(DisplayReceipt.subtotal*1.0875)+","+GUIRunner.currentUser+")");

        
        String itemSQL="INSERT INTO Order_Items VALUES ";
        int orderNum=1;

        for(Entry e:DisplayReceipt.entries){
            System.out.println(itemSQL+e.updateInfo(DisplayReceipt.orderId, orderNum));
            DisplayReceipt.updateIngredients(e);
            DBUtil.dbExecuteUpdate(itemSQL+e.updateInfo(DisplayReceipt.orderId, orderNum++));
        }
        
        DisplayReceipt.entries.clear();
        workingEntry=null;

        DisplayReceipt.orderId++;
        DisplayReceipt.subtotal=0;

        DisplayReceipt.updateRecipt(((Node)event.getSource()).getScene());
    }

    public void updateButtonStates() {
        System.out.println(GUIRunner.isManager);
        boolean isManager = GUIRunner.isManager;
        cashier.setDisable(!isManager);
        inventory.setDisable(!isManager);
        employees.setDisable(!isManager);
        order_history.setDisable(!isManager);
        analysis.setDisable(!isManager);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            DisplayReceipt.load();
        } catch (SQLException | ClassNotFoundException ex) {
        }
    }
    
    public void buttonPressedOther(ActionEvent event){
        String buttonPressed = ((Node)event.getSource()).getId();
        ((ToggleButton)event.getSource()).setSelected(false);
        int index=DisplayReceipt.extraCostName.indexOf(buttonPressed);
        workingEntry=new Entry("other",buttonPressed,DisplayReceipt.extraCostPrice.get(index));
        scene=((Node)event.getSource()).getScene();
        DisplayReceipt.addEntry(workingEntry);
        DisplayReceipt.updateRecipt(scene);
    }

    public void buttonPressedSide(ActionEvent event){
        String buttonPressed = ((Node)event.getSource()).getId();
        selected.add((ToggleButton)event.getSource());
        
        if(!workingEntry.addSide(buttonPressed)){
            workingEntry.removeSide(buttonPressed);
            deSelect(buttonPressed);
        }

        scene=((Node)event.getSource()).getScene();
        scene.lookup("#AddToOrder").setOpacity(1);
        scene.lookup("#AddToOrder").setDisable(false);
        DisplayReceipt.updateRecipt(scene);
    }

    public void buttonPressedProtein(ActionEvent event){
        String buttonPressed = ((Node)event.getSource()).getId();

        if(buttonPressed.contains("Special")){
            buttonPressed=special;
        }
        
        selected.add((ToggleButton)event.getSource());
        
        if(!workingEntry.addProtein(buttonPressed)){
            workingEntry.removeProtein(buttonPressed);
            deSelect((buttonPressed==special?"Special : ":"")+buttonPressed);
        }

        scene=((Node)event.getSource()).getScene();
        scene.lookup("#AddToOrder").setOpacity(1);
        scene.lookup("#AddToOrder").setDisable(false);
        DisplayReceipt.updateRecipt(scene);
    }

    public void buttonPressedType(ActionEvent event) throws IOException{
        String buttonPressed = ((Node)event.getSource()).getId();
        System.out.print(buttonPressed);
        workingEntry=new Entry(new String[]{buttonPressed.replaceAll("-", " ")},DisplayReceipt.overarchingCosts.get(buttonPressed.replaceAll("-", " ")));
        DisplayReceipt.addEntry(workingEntry);
        ToggleButton pressed=((ToggleButton)event.getSource());
        pressed.setSelected(false);
        scene=switch_to_cashier2(event);
        ((ToggleButton)scene.lookup("#"+pressed.getId())).setSelected(true);
        selected.add(((ToggleButton)scene.lookup("#"+pressed.getId())));
    }

    public void cancelCashier2(ActionEvent event) throws IOException{
        DisplayReceipt.removeEntry(workingEntry);
        deSelectAll();
        switch_to_cashier1(event);
    }

    public static void updateSpecialtyButton(Scene scene) throws SQLException, ClassNotFoundException{
        ResultSet rs=DBUtil.dbExecuteQuery("SELECT menu_Name FROM menu WHERE position('Special' in menu_name)>0 AND active=true;");
        rs.next();
        special=rs.getString(1).replaceAll("Special: ", "");
        ((ToggleButton)scene.lookup("#SpecialtyItem")).setText(special);
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
    private void changeToAnalysis(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("analysis");
   }
    @FXML
    private Button cashier;
    @FXML
    private Button inventory;
    @FXML
    private Button employees;
    @FXML
    private Button order_history;
    @FXML
    private Button analysis;

    //Buttons for all of the serving size options
    @FXML
    private ToggleButton button_bowl;

    @FXML
    private ToggleButton button_plate;

    @FXML
    private ToggleButton button_big_plate;

    @FXML
    private ToggleButton button_cub_meal;

    @FXML
    private ToggleButton button_carte;

    @FXML
    private ToggleButton button_confirm;

    //Buttons for all of the beverage options
    @FXML
    private ToggleButton button_water_cup;

    @FXML
    private ToggleButton button_fountain_drink;

    @FXML
    private ToggleButton button_water_bottle;

    @FXML
    private ToggleButton button_gatorade;

    @FXML
    private ToggleButton button_juice;

    @FXML
    private ToggleButton button_milk;

    //Buttons for all of the appetizer options
    @FXML
    private ToggleButton button_chicken_egg_roll;

    @FXML
    private ToggleButton button_veggie_spring_roll;

    @FXML
    private ToggleButton button_rangoon;

    @FXML
    private ToggleButton button_apple_pie_roll;

    //Buttons for all of the side options
    @FXML
    private ToggleButton button_fried_rice;

    @FXML
    private ToggleButton button_white_rice;

    @FXML
    private ToggleButton button_chow_mein;

    @FXML
    private ToggleButton button_super_greens;

    //Buttons for all of the protein options
    @FXML
    private ToggleButton button_bourbon_chicken;

    @FXML
    private ToggleButton button_orange_chicken;

    @FXML
    private ToggleButton button_teriyaki_chicken;

    @FXML
    private ToggleButton button_angus_steak;

    @FXML
    private ToggleButton button_kung_pao_chicken;

    @FXML
    private ToggleButton button_honey_sesame_chicken;

    @FXML
    private ToggleButton button_honey_walnut_shrimp;

    @FXML
    private ToggleButton button_beijing_beef;

    @FXML
    private ToggleButton button_mushroom_chicken;

    @FXML
    private ToggleButton button_string_bean_chicken;

    @FXML
    private ToggleButton button_broccoli_beef;

    @FXML
    private ToggleButton button_black_pepper_chicken;

    @FXML
    private ToggleButton button_sweet_fire_chicken;

}