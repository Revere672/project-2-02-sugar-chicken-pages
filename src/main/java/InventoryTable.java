import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.sql.*;

public class InventoryTable {
    @FXML
    private Button cashier;
    @FXML
    private Button inventory;
    @FXML
    private Button employees;
    @FXML
    private Button order_history;
    @FXML
    private TextField product_search;
    @FXML
    private TableView<Inventory> inventory_table;
    @FXML
    private TableColumn<Inventory, Integer> inventory_ID_col;
    @FXML
    private TableColumn<Inventory, String> prod_name_col;
    @FXML
    private TableColumn<Inventory, String> supplier_col;
    @FXML
    private TableColumn<Inventory, Double> cost_col;
    @FXML
    private TableColumn<Inventory, Double> quantity_col;
    @FXML
    private Button logoutButton;

    @FXML
    private void searchInventory(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        try {
            if (product_search.getText() == "" || product_search.getText() == null) {
                searchInventories(null);
            }

            ObservableList<Inventory> inventoryData = InventoryDB.searchInventory(product_search.getText());

            populateInventories(inventoryData);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @FXML
    private void searchInventories(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            ObservableList<Inventory> inventoryData = InventoryDB.searchInventories();

            populateInventories(inventoryData);
        } catch (SQLException e) {
            System.out.println("Error occurred while getting employees information from DB.\n" + e);
            throw e;
        }
    }

    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        inventory_ID_col.setCellValueFactory(cellData -> cellData.getValue().inventoryIDProperty().asObject());
        prod_name_col.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        supplier_col.setCellValueFactory(cellData -> cellData.getValue().supplierProperty());
        cost_col.setCellValueFactory(cellData -> cellData.getValue().costProperty().asObject());
        quantity_col.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        product_search.setText("");
        searchInventories(null);
    }

    @FXML
    private void populateInventory(Inventory inventory) throws ClassNotFoundException {
        if (inventory != null) {
            ObservableList<Inventory> inventoryData = FXCollections.observableArrayList();
            inventoryData.add(inventory);
            inventory_table.setItems(inventoryData);
        }
    }

    @FXML
    private void populateInventories(ObservableList<Inventory> inventoryData) throws ClassNotFoundException {
        inventory_table.setItems(inventoryData);
    }

    @FXML
    private void addProduct(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("inventory_add_product");
    }

    @FXML
    private void editProduct(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("inventory_edit_product");
    }

    @FXML
    private void changeToCashier(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("cashier1");
    }

    @FXML
    private void changeToEmployees(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("employees");
    }

    @FXML
    private void changeToOrderHistory(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("order_history");
    }

    @FXML
    public void logOut(ActionEvent e) {
        GUIRunner.currentUser = 0;  
        GUIRunner.isManager = false; 

        GUIRunner.changeScene("login");
        System.out.println("Logout successful");
    }
}
