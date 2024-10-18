import java.sql.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.time.LocalDate;

/**
 * Class to handle communication between the controller and the database
 */
public class OrderDB {
    
    /**
     * Finds the first 1000 orders by ID
     * 
     * @param order_id The order ID to search for
     * @return An order object
     * @throws SQLException if a database access error occurs
     * @throws ClassNotFoundException if the database driver class is not found
     */
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

    /**
     * Get the order object from a result set
     * 
     * @param rsOrder The result set
     * @return An order object
     * @throws SQLException if a database access error occurs
     */
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

    /**
     * Search for a certain orders in a range
     * 
     * @param offset Order to start at
     * @param count Number of orders to get
     * @return A list of orders in the range
     * @throws SQLException if a database access error occurs
     * @throws ClassNotFoundException if the database driver class is not found
     */
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

    /**
     * Search for an order by ID
     * 
     * @param orderID The order ID
     * @return A list of Entries in an order
     * @throws SQLException if a database access error occurs
     * @throws ClassNotFoundException if the database driver class is not found
     */
    public static ObservableList<Entry> searchOrderItems(int orderID) throws SQLException, ClassNotFoundException {
        String stmt = "SELECT * FROM order_items WHERE order_id = " + orderID +  ";";

        try {
            ResultSet rsOrder = DBUtil.dbExecuteQuery(stmt);

            ObservableList<Entry> entryList = getEntryList(rsOrder);

            return entryList;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Gets the entries from a result set
     * 
     * @param rsOrder The result set
     * @return A list of entries in an order
     * @throws SQLException if a database access error occurs
     * @throws ClassNotFoundException if the database driver class is not found
     */
    private static ObservableList<Entry> getEntryList(ResultSet rsOrder) throws SQLException, ClassNotFoundException {
        ObservableList<Entry> entryList = FXCollections.observableArrayList();
        System.out.println("OrderList being created");

        while (rsOrder.next()) {
            // int orderID = rsOrder.getInt("order_ID");
            // int itemNumber = rsOrder.getInt("Item_Number");
            String itemName = rsOrder.getString("Item_Name");
            String side1 = rsOrder.getString("Side_1");
            String side2 = rsOrder.getString("Side_2");
            String protein1 = rsOrder.getString("Protein_1");
            String protein2 = rsOrder.getString("Protein_2");
            String protein3 = rsOrder.getString("Protein_3");
            String miscItem = rsOrder.getString("Misc_Item");
            double itemCost = rsOrder.getDouble("Item_Cost");

            String[] arrStr = {itemName, side1, side2, protein1, protein2, protein3, miscItem};

            Entry order = new Entry(arrStr, itemCost);
            entryList.add(order);
        }

        return entryList;
    }

    /**
     * Gets orders on a specified day
     * 
     * @param d The date
     * @return A list of orders on a day
     * @throws SQLException if a database access error occurs
     * @throws ClassNotFoundException if the database driver class is not found
     */
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

    /**
     * Searches for the first 1000 orders
     * 
     * @return A list of the first 1000 orders
     * @throws SQLException if a database access error occurs
     * @throws ClassNotFoundException if the database driver class is not found
     */
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

    /**
     * Gets a list of orders from a result set
     * 
     * @param rsOrder The result set
     * @return A list of orders from a result set
     * @throws SQLException if a database access error occurs
     * @throws ClassNotFoundException if the database driver class is not found
     */
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

    /**
     * Inserts a new order into the database
     * 
     * @param order_ID The order ID
     * @param order_time The time of the order
     * @param price The total price of the order
     * @param employee_id The employee ID who took the order
     * @throws SQLException if a database access error occurs
     * @throws ClassNotFoundException if the database driver class is not found
     */
    public static void insertOrder(int order_ID, Timestamp order_time, double price, int employee_id) throws SQLException, ClassNotFoundException {
        String stmt = "INSERT INTO orders (order_ID, order_time, total_price, employee_ID) VALUES (" + order_ID + ", '" + order_time + "', " + price + ", " + employee_id + ");";
    
        try {
            DBUtil.dbExecuteUpdate(stmt);
        } catch (Exception e) {
            throw e;
        }
    }
    
}
