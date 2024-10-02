import java.sql.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.time.LocalDate;

public class OrderDB {
    
    public static Order searchOrder(Integer order_id) throws SQLException, ClassNotFoundException {
        String stmt = "SELECT * FROM order_history WHERE order_ID ='"+ order_id.toString() +"' LIMIT 1000;";

        try {
            ResultSet rsOrder = DBUtil.dbExecuteQuery(stmt);

            Order order = getOrderResult(rsOrder);

            return order;
        } catch (Exception e) {
            throw e;
        }
    }

    private static Order getOrderResult(ResultSet rsOrder) throws SQLException {
        Order order = null;

        if (rsOrder.next()) {
            order = new Order();
            order.setOrderID(rsOrder.getInt("order_ID"));
            order.setOrderTime(rsOrder.getTimestamp("order_time").toLocalDateTime());
            order.setPrice(rsOrder.getDouble("total_price"));
            order.setEmployeeID(rsOrder.getInt("employee_ID"));
        }

        return order;
    }

    public static ObservableList<Order> searchOrders(int offset, int count) throws SQLException, ClassNotFoundException {
        String stmt = "SELECT * FROM order_history ORDER BY order_ID DESC LIMIT " + count + " OFFSET " + offset + ";";

        try {
            ResultSet rsOrder = DBUtil.dbExecuteQuery(stmt);

            ObservableList<Order> orderList = getOrderList(rsOrder);

            return orderList;
        } catch (Exception e) {
            throw e;
        }
    }

    public static ObservableList<Order> searchOrdersByDate(LocalDate d) throws SQLException, ClassNotFoundException {
        int year = d.getYear();
        int month = d.getMonthValue();
        int day = d.getDayOfMonth();


        String stmt = "SELECT * FROM order_history WHERE EXTRACT(YEAR from order_time) = " + year + " AND EXTRACT(MONTH from order_time) = " + month + " AND EXTRACT(DAY from order_time) = " + day + " ORDER BY order_ID DESC;";

        try {
            ResultSet rsOrder = DBUtil.dbExecuteQuery(stmt);

            ObservableList<Order> orderList = getOrderList(rsOrder);

            return orderList;
        } catch (Exception e) {
            throw e;
        }
    }

    public static ObservableList<Order> searchOrders() throws SQLException, ClassNotFoundException {
        String stmt = "SELECT * FROM order_history ORDER BY order_ID DESC LIMIT 1000 OFFSET 0;";

        try {
            ResultSet rsOrder = DBUtil.dbExecuteQuery(stmt);

            ObservableList<Order> orderList = getOrderList(rsOrder);

            return orderList;
        } catch (Exception e) {
            throw e;
        }
    }

    private static ObservableList<Order> getOrderList(ResultSet rsOrder) throws SQLException, ClassNotFoundException {
        ObservableList<Order> orderList = FXCollections.observableArrayList();
        System.out.println("OrderList being created");

        while (rsOrder.next()) {
            Order order = new Order();
            order.setOrderID(rsOrder.getInt("order_ID"));
            order.setOrderTime(rsOrder.getTimestamp("order_time").toLocalDateTime());
            order.setPrice(rsOrder.getDouble("total_price"));
            order.setEmployeeID(rsOrder.getInt("employee_ID"));
            orderList.add(order);
        }

        return orderList;
    }

    public static void insertOrder(int order_ID, Timestamp order_time, double price, int employee_id) throws SQLException, ClassNotFoundException {
        String stmt = "INSERT INTO orders (order_ID, order_time, total_price, employee_ID) VALUES (" + order_ID + ", '" + order_time + "', " + price + ", " + employee_id + ");";
    
        try {
            DBUtil.dbExecuteUpdate(stmt);
        } catch (Exception e) {
            throw e;
        }
    }
    
}
