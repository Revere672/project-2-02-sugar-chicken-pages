import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
//import javafx.scene.control.TextField;
import java.time.LocalDateTime;
import javafx.scene.control.Button;

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
    private Button logoutButton;

    private int currentOffset = 0;
    private boolean isLoading = false;
    private int load_count = 1000;

    // @FXML
    // private void searchOrders(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
    //     try {
    //         OrderHistory order_history = OrderDB.searchOrders(order_search.getText());

    //         populateInventory(inventory);
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //         throw e;
    //     }
    // }

    @FXML
    private void searchOrders(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            ObservableList<Order> orderData = OrderDB.searchOrders();
            populateOrders(orderData);
        } catch (SQLException e) {
            System.out.println("Error occurred while getting orders information from DB.\n" + e);
            throw e;
        }
    }

    @FXML
    private void initialize() {
        order_ID_col.setCellValueFactory(cellData -> cellData.getValue().orderIDProperty().asObject());
        order_time_col.setCellValueFactory(cellData -> cellData.getValue().orderTimeProperty());
        price_col.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        employee_ID_col.setCellValueFactory(cellData -> cellData.getValue().employeeIDProperty().asObject());
        loadOrders();
        addScrollListener();
    }

    private void addScrollListener() {
        order_table.skinProperty().addListener((observable, oldSkin, newSkin) -> {
            ScrollBar verticalScrollbar = getVerticalScrollBar(order_table);
            if (verticalScrollbar != null) {
                verticalScrollbar.valueProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal.doubleValue() == verticalScrollbar.getMax() && !isLoading) {
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

    @FXML
    public void logOut(ActionEvent e) {
        GUIRunner.currentUser = 0;  
        GUIRunner.isManager = false; 

        GUIRunner.changeScene("login");
        System.out.println("Logout successful");
    }
}
