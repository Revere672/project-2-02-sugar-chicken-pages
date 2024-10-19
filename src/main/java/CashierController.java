
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

/**
 * Class controller for the cashier view
 */
public class CashierController implements Initializable {

    private Scene scene;
    public static ArrayList<ToggleButton> selected=new ArrayList<>();
    public static Entry workingEntry;
    public static String special;
    public static Font butFont=new Font("Arial",8.3);;

    /**
     * A class representing a tuple of three integers.
     */
    static class Tuple3 {
        public Integer x;
        public Integer y;
        public Integer change;

        /**
         * Constructs a Tuple3 with the specified values.
         *
         * @param a the first value
         * @param b the second value
         * @param c the third value
         */
        public Tuple3(int a, int b, int c) {
            x = a;
            y = b;
            change = c;
        }
    }

    /**
     * Switches the scene to the cashier1 view.
     *
     * @param event the action event
     * @throws IOException if an I/O error occurs
     */
    public void switch_to_cashier1(ActionEvent event) throws IOException {
        scene = GUIRunner.changeScene("cashier1");
        DisplayReceipt.updateRecipt(scene);
    }

    /**
     * Switches the scene to the cashier2 view.
     *
     * @param event the action event
     * @return the new scene
     * @throws IOException if an I/O error occurs
     */
    public Scene switch_to_cashier2(ActionEvent event) throws IOException {
        scene = GUIRunner.changeScene("cashier2");
        DisplayReceipt.updateRecipt(scene);
        return scene;
    }

    /**
     * Adds the current working entry to the order and updates the scene.
     *
     * @param event the action event
     * @throws IOException if an I/O error occurs
     */
    public void addToOrder(ActionEvent event) throws IOException {
        DisplayReceipt.addEntry(workingEntry);

        scene = ((Node) event.getSource()).getScene();
        DisplayReceipt.updateRecipt(scene);
        ((Node) event.getSource()).setOpacity(0);
        ((Node) event.getSource()).setDisable(true);
        deSelectAll();
        switch_to_cashier1(event);
        workingEntry = null;
    }

    /**
     * Completes the current action and switches to the cashier1 view.
     *
     * @param event the action event
     * @throws IOException if an I/O error occurs
     */
    public void done(ActionEvent event) throws IOException {
        deSelectAll();
        ((Node) event.getSource()).setOpacity(0);
        ((Node) event.getSource()).setDisable(true);
        workingEntry = null;
        switch_to_cashier1(event);
    }

    /**
     * Deselects all selected toggle buttons.
     */
    public static void deSelectAll() {
        for (ToggleButton t : selected) {
            t.setSelected(false);
        }
        selected.clear();
    }

    /**
     * Deselects the toggle button with the specified ID.
     *
     * @param s the ID of the toggle button to deselect
     */
    public static void deSelect(String s) {
        for (ToggleButton t : selected) {
            if (t.getId().equals(s)) {
                t.setSelected(false);
            }
        }
    }

    /**
     * Undoes the last entry added to the receipt.
     *
     * @param event the action event
     */
    public void undo(ActionEvent event) {
        ArrayList<Entry> entries = DisplayReceipt.getEntries();
        if (entries.isEmpty()) {
            return;
        }
        DisplayReceipt.subtotal -= entries.get(entries.size() - 1).price;
        entries.remove(entries.size() - 1);
        scene = ((Node) event.getSource()).getScene();
        DisplayReceipt.updateRecipt(scene);
    }

    /**
     * Finalizes the order and updates the database.
     *
     * @param event the action event
     * @throws IOException if an I/O error occurs
     * @throws SQLException if a database access error occurs
     * @throws ClassNotFoundException if the JDBC driver class is not found
     */
    public void FinishOrder(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        if (DisplayReceipt.getEntries().isEmpty()) {
            return;
        }
        ResultSet rs = DBUtil.dbExecuteQuery("SELECT MAX(order_id) FROM order_history;");
        rs.next();
        DisplayReceipt.orderId = rs.getInt(1) + 1;

        DBUtil.dbExecuteUpdate("INSERT INTO Order_History VALUES (" + DisplayReceipt.orderId + ",\'" + DisplayReceipt.dateFormat.format(new Date()) + "\'," + (DisplayReceipt.subtotal * 1.0875) + "," + GUIRunner.currentUser + ")");

        String itemSQL = "INSERT INTO Order_Items VALUES ";
        int orderNum = 1;

        for (Entry e : DisplayReceipt.entries) {
            DisplayReceipt.updateIngredients(e);
            DBUtil.dbExecuteUpdate(itemSQL + e.updateInfo(DisplayReceipt.orderId, orderNum++));
        }

        DisplayReceipt.entries.clear();
        workingEntry = null;

        DisplayReceipt.orderId++;
        DisplayReceipt.subtotal = 0;

        DisplayReceipt.updateRecipt(((Node) event.getSource()).getScene());
    }

    /**
     * Updates the states of various buttons based on the user's role.
     */
    public void updateButtonStates() {
        boolean isManager = GUIRunner.isManager;
        cashier.setDisable(!isManager);
        inventory.setDisable(!isManager);
        employees.setDisable(!isManager);
        order_history.setDisable(!isManager);
        analysis.setDisable(!isManager);
    }

    /**
     * Initializes the controller.
     *
     * @param location the location used to resolve relative paths for the root object, or null if the location is not known
     * @param resources the resources used to localize the root object, or null if the root object was not localized
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            DisplayReceipt.load();
        } catch (SQLException | ClassNotFoundException ex) {
        }
    }

    /**
     * Handles the event when a button for "other" items is pressed.
     *
     * @param event the action event
     */
    public static void buttonPressedOther(ActionEvent event) {
        String buttonPressed = ((Node) event.getSource()).getId();
        ((ToggleButton) event.getSource()).setSelected(false);
        int index = DisplayReceipt.extraCostName.indexOf(buttonPressed);
        workingEntry = new Entry("other", buttonPressed, DisplayReceipt.extraCostPrice.get(index));
        Scene scene = ((Node) event.getSource()).getScene();
        DisplayReceipt.addEntry(workingEntry);
        DisplayReceipt.updateRecipt(scene);
    }

    /**
     * Handles the event when a button for side items is pressed.
     *
     * @param event the action event
     */
    public static void buttonPressedSide(ActionEvent event) {
        String buttonPressed = ((Node) event.getSource()).getId();
        selected.add((ToggleButton) event.getSource());

        if (!workingEntry.addSide(buttonPressed)) {
            workingEntry.removeSide(buttonPressed);
            deSelect(buttonPressed);
        }

        Scene scene = ((Node) event.getSource()).getScene();
        scene.lookup("#AddToOrder").setOpacity(1);
        scene.lookup("#AddToOrder").setDisable(false);
        DisplayReceipt.updateRecipt(scene);
    }

    /**
     * Handles the event when a button for protein items is pressed.
     *
     * @param event the action event
     */
    public static void buttonPressedProtein(ActionEvent event) {
        String buttonPressed = ((Node) event.getSource()).getId();

        if (buttonPressed.contains("Special")) {
            buttonPressed = special;
        }

        selected.add((ToggleButton) event.getSource());

        if (!workingEntry.addProtein(buttonPressed)) {
            workingEntry.removeProtein(buttonPressed);
            deSelect((buttonPressed.equals(special) ? "Special : " : "") + buttonPressed);
        }

        Scene scene = ((Node) event.getSource()).getScene();
        scene.lookup("#AddToOrder").setOpacity(1);
        scene.lookup("#AddToOrder").setDisable(false);
        DisplayReceipt.updateRecipt(scene);
    }

    /**
     * Handles the event when a button for item types is pressed.
     *
     * @param event the action event
     * @throws IOException if an I/O error occurs
     */
    public void buttonPressedType(ActionEvent event) throws IOException {
        String buttonPressed = ((Node) event.getSource()).getId();
        workingEntry = new Entry(new String[]{buttonPressed.replaceAll("-", " ")}, DisplayReceipt.overarchingCosts.get(buttonPressed.replaceAll("-", " ")));
        DisplayReceipt.addEntry(workingEntry);
        ToggleButton pressed = ((ToggleButton) event.getSource());
        pressed.setSelected(false);
        scene = switch_to_cashier2(event);
        ((ToggleButton) scene.lookup("#" + pressed.getId())).setSelected(true);
        selected.add(((ToggleButton) scene.lookup("#" + pressed.getId())));
    }

    /**
     * Cancels the current action and switches back to the cashier1 view.
     *
     * @param event the action event
     * @throws IOException if an I/O error occurs
     */
    public void cancelCashier2(ActionEvent event) throws IOException {
        DisplayReceipt.removeEntry(workingEntry);
        deSelectAll();
        switch_to_cashier1(event);
    }

    /**
     * Builds the grids for the menu items in the specified scenes.
     *
     * @param scene1 the first scene
     * @param scene2 the second scene
     * @throws SQLException if a database access error occurs
     * @throws ClassNotFoundException if the JDBC driver class is not found
     */
    public static void buildGrids(Scene scene1,Scene scene2)throws SQLException, ClassNotFoundException {
        ResultSet rs=DBUtil.dbExecuteQuery("SELECT menu_Name,menu_id FROM menu WHERE active=true;");
        HashMap<Integer,Tuple3> locs=new HashMap<>();
        locs.put(600000,new Tuple3(0,0,1));
        locs.put(610000,new Tuple3(4,0,-1));
        locs.put(620000,new Tuple3(4,0,-1));
        locs.put(630000,new Tuple3(0,0,1));

        while(rs.next()){
            Tuple3 pos=locs.get(Integer.parseInt(rs.getString(2))/10000*10000);
            ToggleButton but=new ToggleButton(rs.getString(1).replaceAll("Special:",""));
            but.setId(rs.getString(1));
            but.setPrefHeight(40);
            but.setPrefWidth(65);
            but.setFont(butFont);
            but.setWrapText(true);
            switch (Integer.parseInt(rs.getString(2))/10000) {
                case 61://apps
                    but.setOnAction((ActionEvent event) -> {
                        buttonPressedOther(event);
                    });
                    but.setStyle("-fx-background-color: #eb8da6; -fx-border-color: #000000;-fx-text-alignment: center;");
                    ((GridPane)scene1.lookup("#Grid")).add(but,pos.x,pos.y);
                    break;
                case 63://drinks
                    but.setOnAction((ActionEvent event) -> {
                        buttonPressedOther(event);
                    });
                    but.setStyle("-fx-background-color: #5a93db; -fx-border-color: #000000;-fx-text-alignment: center;");
                    ((GridPane)scene1.lookup("#Grid")).add(but,pos.x,pos.y);
                    break;
                case 62://protien
                    but.setOnAction((ActionEvent event) -> {
                        buttonPressedProtein(event);
                    });
                    if(DisplayReceipt.extraCostPrice.get(DisplayReceipt.extraCostName.indexOf(rs.getString(1)))>0.1)
                    but.setStyle("-fx-background-color: #d9ead3ff; -fx-border-color: #FDAA48;-fx-text-alignment: center;");
                    else
                    but.setStyle("-fx-background-color: #d9ead3ff; -fx-border-color: #000000;-fx-text-alignment: center;");
                    ((GridPane)scene2.lookup("#Grid")).add(but,pos.x,pos.y);
                    break;
                default://sides
                    but.setOnAction((ActionEvent event) -> {
                        buttonPressedSide(event);
                    });
                    but.setStyle("-fx-background-color: #ADD8E6; -fx-border-color: #000000;-fx-text-alignment: center;");
                    ((GridPane)scene2.lookup("#Grid")).add(but,pos.x,pos.y);
                    break;
            }
            pos.y++;
            if(pos.y>=4){
                pos.y=0;
                pos.x+=pos.change;
            }
        }
    }

    /**
     * Handles the action event for changing the scene to the employees view.
     * 
     * @param actionEvent The action event.
     * @throws SQLException if a database access error occurs.
     * @throws ClassNotFoundException if the database driver class is not found.
     */
    @FXML
    private void changeToEmployees(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("employees");
    }

    /**
     * Handles the action event for changing the scene to the inventory view.
     * 
     * @param actionEvent The action event.
     * @throws SQLException if a database access error occurs.
     * @throws ClassNotFoundException if the database driver class is not found.
     */
    @FXML
    private void changeToInventory(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("inventory");
    }

    /**
     * Handles the action event for changing the scene to the order history view.
     * 
     * @param actionEvent The action event.
     * @throws SQLException if a database access error occurs.
     * @throws ClassNotFoundException if the database driver class is not found.
     */
    @FXML
    private void changeToOrderHistory(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("order_history");
    }

    /**
     * Handles the action event for changing the scene to the analysis view.
     * 
     * @param actionEvent The action event.
     * @throws SQLException if a database access error occurs.
     * @throws ClassNotFoundException if the database driver class is not found.
     */
    @FXML
    private void changeToAnalysis(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("analysis");
    }

    /**
     * Handles the action event for changing the scene to the cashier view.
     * 
     * @param actionEvent The action event.
     * @throws SQLException if a database access error occurs.
     * @throws ClassNotFoundException if the database driver class is not found.
     */
    @FXML
    private void changeToCashier(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("cashier1");
    }

    /**
     * Handles the action event for logging out and changing the scene to the login view.
     * 
     * @param actionEvent The action event.
     * @throws SQLException if a database access error occurs.
     * @throws ClassNotFoundException if the database driver class is not found.
     * @throws IOException if an I/O error occurs.
     */
    @FXML
    private void logOut(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
        Scene login = new Scene(FXMLLoader.load(getClass().getResource("/fxml/login.fxml")));
        System.out.println("Logged Out");
        GUIRunner.stage.setScene(login);
        GUIRunner.stage.show();
    }

    @FXML
    private Button log_out_button;
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

    @FXML
    private Button logoutButton;
}