import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
//import javafx.scene.layout.Priority;

import java.sql.*;

public class InventoryTable {
    @FXML
    private AnchorPane main_inventory;
    @FXML
    private Pane edit_pane;
    @FXML
    private Pane add_pane;
    @FXML
    private Pane update_pane;
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
    private TextArea failed_text1;
    @FXML
    private TextArea failed_text2;
    @FXML
    private Button apply_button;
    @FXML
    private TextField product_name_text1;
    @FXML
    private TextField supplier_text1;
    @FXML
    private TextField cost_text1;
    @FXML
    private TextField quantity_text1;
    @FXML
    private TextField restock_text1;
    @FXML
    private Button add_button;
    @FXML
    private TextField product_name_text2;
    @FXML
    private TextField quantity_text2;
    @FXML
    private TextField restock_text2;
    @FXML
    private TextField update2;
    @FXML
    private Button decrement_button;
    @FXML
    private Button increment_button;
    @FXML
    private Button update_button;
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
    private TableColumn<Inventory, Void> quantity_col;
    // @FXML
    // private TableColumn<Inventory, Void> quantity_col_label;
    @FXML
    private TableColumn<Inventory, Void> action_col;

    private int product_ID;

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
        edit_pane.setVisible(false);
        add_pane.setVisible(false);
        update_pane.setVisible(false);
        setBackground(false);

        inventory_ID_col.setCellValueFactory(cellData -> cellData.getValue().inventoryIDProperty().asObject());
        prod_name_col.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        supplier_col.setCellValueFactory(cellData -> cellData.getValue().supplierProperty());
        cost_col.setCellValueFactory(cellData -> cellData.getValue().costProperty().asObject());
        //quantity_col.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        quantity_col.setCellFactory(param -> new TableCell<Inventory, Void>() {
            private final Text text = new Text();
            private final TextField textLabel = new TextField();

            VBox textBox = new VBox(text);
            VBox labelBox = new VBox(textLabel);

            HBox box = new HBox(textBox, labelBox);

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item != null) {
                    setGraphic(null);
                } else {
                    box.setPrefWidth(quantity_col.getPrefWidth());
                    textBox.setPrefWidth(box.getPrefWidth()/2);
                    labelBox.setPrefWidth(box.getPrefWidth()/2 + 20);
                    textLabel.setEditable(false);
                    textLabel.setPrefWidth(60);
                    textLabel.setAlignment(Pos.CENTER);
                    //textLabel.setStyle(textLabel.getStyle() + "-fx-padding: ;");
                    Inventory product = getTableView().getItems().get(getIndex());
                    text.setText(""+product.getQuantity());
                    if (product.getQuantity() < 80) {
                        //System.out.println(product.getQuantity());
                        textLabel.setText("Low");
                        textLabel.setStyle(textLabel.getStyle() + "-fx-background-color: rgb(255,31,31);");
                    }
                    else if (product.getQuantity() < 120) {
                        //System.out.println(product.getQuantity());
                        textLabel.setText("Medium");
                        textLabel.setStyle(textLabel.getStyle() + "-fx-background-color: rgb(0,219,7);");
                    }
                    else {
                        //System.out.println(product.getQuantity());
                        textLabel.setText("High");
                        textLabel.setStyle(textLabel.getStyle() + "-fx-background-color: rgb(255,166,0);");
                    }
                    setGraphic(box);
                }
            }
        });
        action_col.setCellFactory(param -> new TableCell<Inventory, Void>() {
            private final Button editButton = new Button("\u270E");
            private final Button restockButton = new Button("\uF4CB");

            HBox box = new HBox(editButton, restockButton);

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item != null) {
                    setGraphic(null);
                } else {
                    setGraphic(box);
                    editButton.setOnAction(event -> {
                        try {
                            Inventory product = getTableView().getItems().get(getIndex());
                            if (product != null) {
                                product_ID = product.getInventoryID();
                                fillProduct(null);
                                editProduct(null);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });

                    restockButton.setOnAction(event -> {
                        try {
                            Inventory product = getTableView().getItems().get(getIndex());
                            if (product != null) {
                                product_ID = product.getInventoryID();
                                fillUpdate(null);
                                updateProduct(null);
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        });

        product_search.setText("");
        searchInventories(null);
        product_name_text.setText(null);
        supplier_text.setText(null);
        cost_text.setText(null);
        restock_text.setText(null);
        failed_text.setText(null);
        product_name_text1.setText(null);
        supplier_text1.setText(null);
        cost_text1.setText(null);
        quantity_text1.setText(null);
        restock_text1.setText(null);
        failed_text1.setText(null);
        product_name_text2.setText(null);
        quantity_text2.setText(null);
        restock_text2.setText(null);
        update2.setText(null);
        failed_text2.setText(null);
    }

    @FXML
    private void setBackground(Boolean value) {
        if (value) {
            main_inventory.getChildren().forEach(node -> {
                if (!(node.equals(edit_pane) || node.equals(add_pane) || node.equals(update_pane))) {
                    node.setOpacity(0.3);
                    node.setDisable(true);
                }
            });
        }
        else {
            main_inventory.getChildren().forEach(node -> {
                if (!(node.equals(edit_pane) || node.equals(add_pane) || node.equals(update_pane))) {
                    node.setOpacity(1);
                    node.setDisable(false);
                }
            });
        }
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
        setBackground(true);
        add_pane.setVisible(true);
        product_name_text1.requestFocus();
    }

    @FXML
    private void editProduct(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        setBackground(true);
        edit_pane.setVisible(true);
        product_name_text.requestFocus();
    }

    @FXML
    private void updateProduct(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        setBackground(true);
        update_pane.setVisible(true);
        update2.requestFocus();
    }

    // @FXML
    // private void searchProduct(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
    //     try {
    //         ObservableList<Inventory> inventoryData;
    //         if (product_edit_search.getText() == "" || product_edit_search.getText() == null) {
    //             inventoryData = InventoryDB.searchInventories();
    //         }
    //         else {
    //             inventoryData = InventoryDB.searchInventory(product_edit_search.getText());
    //         }

    //         ObservableList<String> productData = FXCollections.observableArrayList();
    //         for (Inventory item: inventoryData) {
    //             productData.add(item.getProductName());
    //         }
    //         product_drop_down.setItems(productData);
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         throw e;
    //     }
    // }

    @FXML 
    private void fillProduct(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            Inventory item = InventoryDB.findProduct(product_ID);
            product_name_text.setText(item.getProductName());
            supplier_text.setText(item.getSupplier());
            cost_text.setText(""+item.getCost());
            restock_text.setText(""+InventoryDB.getRestock(product_ID));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @FXML 
    private void fillUpdate(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            Inventory item = InventoryDB.findProduct(product_ID);
            product_name_text2.setText(item.getProductName());
            quantity_text2.setText(""+InventoryDB.getQuantity(product_ID));
            restock_text2.setText(""+InventoryDB.getRestock(product_ID));
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @FXML
    private void add(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (product_name_text1.getText() == "" || supplier_text1.getText() == "" || cost_text1.getText() == ""
            || quantity_text1.getText() == "" || restock_text1.getText() == ""
            || product_name_text1.getText() == null || supplier_text1.getText() == null || cost_text1.getText() == null
            || quantity_text1.getText() == null || restock_text1.getText() == null) {

            addFailed();
        }
        else {
            InventoryDB.insertProduct(product_name_text1.getText(), supplier_text1.getText(), cost_text1.getText(), quantity_text1.getText(), restock_text1.getText());
            product_name_text1.setText(null);
            supplier_text1.setText(null);
            cost_text1.setText(null);
            quantity_text1.setText(null);
            restock_text1.setText(null);
            failed_text1.setText(null);
            failed_text.setText(null);
            setBackground(false);
            add_pane.setVisible(false);
            searchInventory(null);
        }
    }

    @FXML
    private void apply(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (product_name_text.getText() == "" || supplier_text.getText() == "" || cost_text.getText() == "" || restock_text.getText() == ""
            || product_name_text.getText() == null || supplier_text.getText() == null || cost_text.getText() == null || restock_text.getText() == null) {
            addFailed();
        }
        else {
            InventoryDB.editProduct(product_ID, product_name_text.getText(), supplier_text.getText(), cost_text.getText(), restock_text.getText());
            product_name_text.setText(null);
            supplier_text.setText(null);
            cost_text.setText(null);
            restock_text.setText(null);
            failed_text.setText(null);
            failed_text1.setText(null);
            setBackground(false);
            edit_pane.setVisible(false);
            searchInventory(null);
        }
    }

    @FXML
    private void update(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if (Double.parseDouble(quantity_text2.getText()) < 0) {
            updateFailed();
        }
        else {
            InventoryDB.updateRestock(product_ID, Double.parseDouble(quantity_text2.getText()));
            product_name_text2.setText(null);
            quantity_text2.setText(null);
            restock_text2.setText(null);
            update2.setText(null);
            failed_text2.setText(null);
            setBackground(false);
            update_pane.setVisible(false);
            searchInventory(null);
        }
    }

    @FXML
    private void cancel(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        product_name_text.setText(null);
        supplier_text.setText(null);
        cost_text.setText(null);
        restock_text.setText(null);
        failed_text.setText(null);
        product_name_text1.setText(null);
        supplier_text1.setText(null);
        cost_text1.setText(null);
        quantity_text1.setText(null);
        restock_text1.setText(null);
        failed_text1.setText(null);
        product_name_text2.setText(null);
        quantity_text2.setText(null);
        restock_text2.setText(null);
        update2.setText(null);
        failed_text2.setText(null);
        setBackground(false);
        edit_pane.setVisible(false);
        add_pane.setVisible(false);
        update_pane.setVisible(false);
        searchInventory(null);
    }

    @FXML
    private void addFailed() {
        failed_text.setText("One or more entries are blank");
        failed_text1.setText("One or more entries are blank");
    }

    @FXML
    private void updateFailed() {
        failed_text2.setText("Quantity cannot be negative");
    }

    @FXML
    private void changeQuantityFailed() {
        failed_text2.setText("Cannot increment/decrement by a negative");
    }

    @FXML
    private void increment(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        double amount  = 0.0;
        if (update2.getText() != "" && update2.getText() != null) {
            amount = Double.parseDouble(update2.getText());
        }
        
        if (amount < 0) {
            changeQuantityFailed();
        }
        else {
            amount += Double.parseDouble(quantity_text2.getText());
            quantity_text2.setText(""+amount);
        }
    }

    @FXML
    private void decrement(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        double amount  = 0.0;
        if (update2.getText() != "" && update2.getText() != null) {
            amount = Double.parseDouble(update2.getText());
        }
        
        if (amount < 0) {
            changeQuantityFailed();
        }
        else {
            amount = Double.parseDouble(quantity_text2.getText()) - amount;
            quantity_text2.setText(""+amount);
        }
    }

    @FXML
    private void nameEditEnter(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        supplier_text.requestFocus();
    }

    @FXML
    private void supplierEditEnter(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        cost_text.requestFocus();
    }

    @FXML
    private void costEditEnter(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        restock_text.requestFocus();
    }

    @FXML
    private void restockEditEnter(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        apply_button.requestFocus();
    }

    @FXML
    private void nameAddEnter(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        supplier_text1.requestFocus();
    }

    @FXML
    private void supplierAddEnter(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        cost_text1.requestFocus();
    }

    @FXML
    private void costAddEnter(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        quantity_text1.requestFocus();
    }

    @FXML
    private void quantityAddEnter(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        restock_text1.requestFocus();
    }

    @FXML
    private void restockAddEnter(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        add_button.requestFocus();
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
        // GUIRunner.changeScene("order_history");
    }
}
