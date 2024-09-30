import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.*;

public class InventoryTable {
    @FXML
    private TextField product_search;
    @FXML
    private TableView<Inventory> inventoryTable;
    @FXML
    private TableColumn<Inventory, Integer>  inventory_ID_col;
    @FXML
    private TableColumn<Inventory, String>  prod_name_col;
    @FXML
    private TableColumn<Inventory, String> supplier_col;
    @FXML
    private TableColumn<Inventory, Double> cost_col;
    @FXML
    private TableColumn<Inventory, Integer> quanity_col;

    @FXML
    private void searchInventory(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        try {
            Inventory inventory = InventoryDB.searchInventory(product_search.getText());

            populateInventory(inventory);
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
        } catch (SQLException e){
            System.out.println("Error occurred while getting employees information from DB.\n" + e);
            throw e;
        }
    }

    @FXML
    private void initialize() {
        inventory_ID_col.setCellValueFactory(cellData -> cellData.getValue().inventoryIDProperty().asObject());
        prod_name_col.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        supplier_col.setCellValueFactory(cellData -> cellData.getValue().supplierProperty());
        cost_col.setCellValueFactory(cellData -> cellData.getValue().costProperty().asObject());
        quanity_col.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
    }

    @FXML
    private void populateInventory(Inventory inventory) throws ClassNotFoundException {
        if (inventory != null) {
            ObservableList<Inventory> inventoryData = FXCollections.observableArrayList();
            inventoryData.add(inventory);
            inventoryTable.setItems(inventoryData);
        }
    }

    @FXML
    private void populateInventories(ObservableList<Inventory> inventoryData) throws ClassNotFoundException {
        inventoryTable.setItems(inventoryData);
    }
}
