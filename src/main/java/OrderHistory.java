import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TableCell;
import java.time.LocalDateTime;
import java.time.LocalDate;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.sql.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;


public class OrderHistory {
    // @FXML
    // private TextField order_search;
    @FXML
    private Button log_out_button;
    @FXML
    private AnchorPane main_order_history;
    @FXML
    private Button cashier;
    @FXML
    private Button inventory;
    @FXML
    private Button employees;
    @FXML
    private Button order_history;
    @FXML
    private TableView<Order> order_table;
    @FXML
    private TableColumn<Order, Integer>  order_ID_col;
    @FXML
    private TableColumn<Order, LocalDateTime>  order_time_col;
    @FXML
    private TableColumn<Order, Double> price_col;
    @FXML
    private TableColumn<Order, Integer> employee_ID_col;
    @FXML
    private TableColumn<Order, Void> view_button_col;
    @FXML
    private DatePicker order_date_picker;
    @FXML
    private Pane order_items_pane;
    @FXML
    private Button order_date_search_button;
    @FXML
    private TextField order_id_field;
    @FXML
    private TextArea order_items_scrollable;

    private int orderID;

    private int currentOffset = 0;
    private boolean isLoading = false;
    private boolean isDateLoaded = false;
    private int load_count = 1000;


    // @FXML
    // private void searchOrders(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
    //     try {
    //         ObservableList<Order> orderData = OrderDB.searchOrders();
    //         populateOrders(orderData);
    //     } catch (SQLException e) {
    //         System.out.println("Error occurred while getting orders information from DB.\n" + e);
    //         throw e;
    //     }
    // }

    @FXML
    private void searchOrdersByDate(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            LocalDate selectedDate = order_date_picker.getValue();
            if (selectedDate != null) {
                ObservableList<Order> orderData = OrderDB.searchOrdersByDate(selectedDate);
                populateOrders_replace(orderData);
                isDateLoaded = true;
            }
            else {
                ObservableList<Order> orderData = OrderDB.searchOrders();
                populateOrders_replace(orderData);
                isDateLoaded = false;
            }
        } catch (SQLException e) {
            System.out.println("Searching by date had an error.\n" + e);
            throw e;
        }
    }

    @FXML
    private void initialize() {
        order_items_pane.setVisible(false);
        setBackground(false);

        order_ID_col.setCellValueFactory(cellData -> cellData.getValue().orderIDProperty().asObject());
        order_time_col.setCellValueFactory(cellData -> cellData.getValue().orderTimeProperty());
        price_col.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        employee_ID_col.setCellValueFactory(cellData -> cellData.getValue().employeeIDProperty().asObject());

        view_button_col.setCellFactory(param -> new TableCell<Order, Void>() {
            private final Button viewButton = new Button("🔍");

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item != null) {
                    setGraphic(null);
                } else {
                    setGraphic(viewButton);
                    viewButton.setOnAction(event -> {
                        Order order = getTableView().getItems().get(getIndex());
                        if (order != null) {
                            try {
                                orderID = order.getOrderID();
                                fillHistory(null);
                            }
                            catch (SQLException | ClassNotFoundException e) {
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        }
                    });
                }
            }
        });

        loadOrders();
        addScrollListener();
    }

    private void addScrollListener() {
        order_table.skinProperty().addListener((observable, oldSkin, newSkin) -> {
            ScrollBar verticalScrollbar = getVerticalScrollBar(order_table);
            if (verticalScrollbar != null) {
                verticalScrollbar.valueProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal.doubleValue() == verticalScrollbar.getMax() && !isLoading && !isDateLoaded) {
                        loadOrders();
                    }
                });
            }
        });
    }

    private ScrollBar getVerticalScrollBar(TableView<?> tableView) {
        for (javafx.scene.Node node : tableView.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar) {
                ScrollBar scrollbar = (ScrollBar) node;
                if (scrollbar.getOrientation() == javafx.geometry.Orientation.VERTICAL) {
                    return scrollbar;
                }
            }
        }
        return null;
    }

    private void setBackground(Boolean value) {
        if (value) {
            main_order_history.getChildren().forEach(node -> {
                if (!(node.equals(order_items_pane))) {
                    node.setOpacity(0.3);
                    node.setDisable(true);
                }
            });
        }
        else {
            main_order_history.getChildren().forEach(node -> {
                if (!(node.equals(order_items_pane))) {
                    node.setOpacity(1);
                    node.setDisable(false);
                }
            });
        }
    }

    private void fillHistory(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            ObservableList<Entry> orderItems = OrderDB.searchOrderItems(orderID);
            int count = 1;
            // Output Order # first
            order_id_field.setText(orderID + "");
            String output = "";
            for(Entry entry : orderItems) {
                // Handle items here for display elements
                // Change formatted string to not print null values
                // Do NOT pull from items for prices - we can change pricing so current pricing is not relevant!
                // Just show items in menu
                output += "----------ITEM " + count + "----------\n";
                output += entry.orderType + "\n";
                if(entry.side1 != "null" && !entry.side1.equals("null")) output += "\t" + entry.side1 + "\n";
                if(entry.side2 != "null" && !entry.side2.equals("null")) output += "\t" + entry.side2 + "\n";
                if(entry.protien1 != "null" && !entry.protien1.equals("null")) output += "\t" + entry.protien1 + "\n";
                if(entry.protien2 != "null" && !entry.protien2.equals("null")) output += "\t" + entry.protien2 + "\n";
                if(entry.protien3 != "null" && !entry.protien3.equals("null")) output += "\t" + entry.protien3 + "\n";
                if(entry.miscItem != "null" && !entry.miscItem.equals("null")) output += "\t" + entry.miscItem + "\n";

                //System.out.println(entry.miscItem == "null" || entry.miscItem.equals("null"));

                output += "\n";
                //output += entry.formatedString();
                count++;
            }
            order_items_scrollable.setText(output);
            setBackground(true);
            order_items_pane.setVisible(true);
        } catch (SQLException | ClassNotFoundException e) {
            throw e;
        }
    }

    @FXML
    private void close_pane(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        order_id_field.setText(null);
        order_items_scrollable.setText(null);
        setBackground(false);
        order_items_pane.setVisible(false);
    }

    @FXML
    private void populateOrders(Order order) throws ClassNotFoundException {
        if (order != null) {
            ObservableList<Order> orderData = FXCollections.observableArrayList();
            orderData.add(order);
            order_table.setItems(orderData);
        }
    }

    @FXML
    private void populateOrders(ObservableList<Order> orderData) {
        if (order_table.getItems() == null) {
            order_table.setItems(orderData);
        } else {
            order_table.getItems().addAll(orderData);
        }
    }

    @FXML
    private void populateOrders_replace(ObservableList<Order> orderData) {
        if (order_table.getItems() != null) {
            order_table.getItems().clear();  // Clear the existing items in the table
        }
        order_table.setItems(orderData);  // Set the new order data
    }

    private void loadOrders() {
        isLoading = true; // Set loading flag to true
        try {
            ObservableList<Order> orderData = OrderDB.searchOrders(currentOffset, load_count);
            populateOrders(orderData);
            currentOffset += load_count; // Increment the offset for the next load
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error occurred while getting orders information from DB.\n" + e);
        } finally {
            isLoading = false; // Reset loading flag
        }
    }

    @FXML
    private void refresh(ActionEvent action) {
        order_table.getItems().clear();
        currentOffset = 0;
        order_date_picker.setValue(null);
        loadOrders();

        ScrollBar verticalScrollbar = getVerticalScrollBar(order_table);
        if (verticalScrollbar != null) {
            verticalScrollbar.setValue(0);
        }
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
    private void changeToCashier(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
         GUIRunner.changeScene("cashier1");
    }
    @FXML
    private void changeToAnalysis(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("analysis");
   }

    @FXML
    private void logOut(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
        Scene login = new Scene(FXMLLoader.load(getClass().getResource("/fxml/login.fxml")));
        System.out.println("Logged Out");
        GUIRunner.stage.setScene(login);
        GUIRunner.stage.show();
    }
}
