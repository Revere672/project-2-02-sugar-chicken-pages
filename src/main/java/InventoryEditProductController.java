import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

public class InventoryEditProductController {
    @FXML
    private TextField product_search;
    @FXML
    private TextField product_edit_search;
    @FXML
    private ChoiceBox<String> product_drop_down;
    @FXML
    private TextField product_name_text;
    @FXML
    private TextField supplier_text;
    @FXML
    private TextField cost_text;
    @FXML
    private TextField restock_text;
    @FXML
    private TextArea failed_text;
    @FXML
    private Button apply_button;
    @FXML
    private TableView<Inventory> inventory_table;
    @FXML
    private TableColumn<Inventory, Integer>  inventory_ID_col;
    @FXML
    private TableColumn<Inventory, String>  prod_name_col;
    @FXML
    private TableColumn<Inventory, String> supplier_col;
    @FXML
    private TableColumn<Inventory, Double> cost_col;
    @FXML
    private TableColumn<Inventory, Double> quantity_col;

    @FXML
    private void searchInventory(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        try {
            if (product_search.getText() == "" || product_search.getText() == null) {
                searchInventories(null);
            }
            else {
                ObservableList<Inventory> inventoryData = InventoryDB.searchInventory(product_search.getText());
                populateInventories(inventoryData);
            }
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
    private void initialize() throws SQLException, ClassNotFoundException {
        product_name_text.requestFocus();
        inventory_ID_col.setCellValueFactory(cellData -> cellData.getValue().inventoryIDProperty().asObject());
        prod_name_col.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        supplier_col.setCellValueFactory(cellData -> cellData.getValue().supplierProperty());
        cost_col.setCellValueFactory(cellData -> cellData.getValue().costProperty().asObject());
        quantity_col.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        product_name_text.setText("");
        supplier_text.setText("");
        cost_text.setText("");
        restock_text.setText("");
        failed_text.setText("");
        searchInventory(null);
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
    private void searchProduct(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        try {
            ObservableList<Inventory> inventoryData;
            if (product_edit_search.getText() == "" || product_edit_search.getText() == null) {
                inventoryData = InventoryDB.searchInventories();
            }
            else {
                inventoryData = InventoryDB.searchInventory(product_edit_search.getText());
            }

            ObservableList<String> productData = FXCollections.observableArrayList();
            for (Inventory item: inventoryData) {
                productData.add(item.getProductName());
            }
            product_drop_down.setItems(productData);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @FXML void fillProduct(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            if (product_drop_down.getValue() != null) {
                Inventory item = InventoryDB.findProduct(product_drop_down.getValue());
                product_name_text.setText(item.getProductName());
                supplier_text.setText(item.getSupplier());
                cost_text.setText(""+item.getCost());
                restock_text.setText(""+InventoryDB.getRestock(product_drop_down.getValue()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @FXML
    private void apply(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (product_name_text.getText() == "" || supplier_text.getText() == "" || cost_text.getText() == "" || restock_text.getText() == "") {
            addFailed();
        }
        else {
            InventoryDB.editProduct(product_drop_down.getValue(), product_name_text.getText(), supplier_text.getText(), cost_text.getText(), restock_text.getText());
            product_name_text.setText(null);
            supplier_text.setText(null);
            cost_text.setText(null);
            restock_text.setText(null);
            failed_text.setText(null);
            GUIRunner.changeScene("inventory");
        }
    }

    @FXML
    private void cancel(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        product_name_text.setText(null);
        supplier_text.setText(null);
        cost_text.setText(null);
        restock_text.setText(null);
        failed_text.setText(null);
        GUIRunner.changeScene("inventory");
    }

    @FXML
    private void addFailed() {
        failed_text.setText("One or more entries are blank");
    }

    // @FXML
    // private void nameEnter(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
    //     supplier_text.requestFocus();
    // }

    @FXML
    private void supplierEnter(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        cost_text.requestFocus();
    }

    @FXML
    private void costEnter(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        restock_text.requestFocus();
    }

    @FXML
    private void restockEnter(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        apply_button.requestFocus();
    }
}
