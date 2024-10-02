import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
//import javafx.scene.control.TextField;
import java.time.LocalDateTime;
import java.time.LocalDate;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

import java.sql.*;

public class OrderHistory {
    // @FXML
    // private TextField order_search;
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
    private Button order_date_search_button;

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
                                int orderID = order.getOrderID();
                                ObservableList<Entry> orderItems = OrderDB.searchOrderItems(orderID);
                                int count = 1;
                                for(Entry entry : orderItems) {
                                    // Handle items here for display elements
                                    // Change formatted string to not print null values
                                    // Do NOT pull from items for prices - we can change pricing so current pricing is not relevant!
                                    // Just show items in menu
                                    System.out.println("----------ITEM " + count + "----------");
                                    System.out.println(entry.formatedString());
                                    count++;
                                }
                            }
                            catch (SQLException | ClassNotFoundException e) {
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
    private void changeToCashier(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("cashier1");
    }

    @FXML
    private void changeToEmployees(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("employees");
    }

    @FXML
    private void changeToInventory(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("inventory");
    }

}
