import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
//import javafx.scene.control.TextField;
import java.time.LocalDateTime;

import java.sql.*;

public class OrderHistory {
    // @FXML
    // private TextField order_search;
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
        try {
            ObservableList<Order> orderData = OrderDB.searchOrders();
            populateOrders(orderData);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error occurred while getting orders information from DB.\n");
        }
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
        order_table.setItems(orderData);
    }

}
