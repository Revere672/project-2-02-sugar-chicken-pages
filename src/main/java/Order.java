import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.time.LocalDateTime;

/**
 * Class to store order information
 */
public class Order {
    private IntegerProperty order_ID;
    private SimpleObjectProperty<LocalDateTime> order_time;
    private DoubleProperty price;
    private IntegerProperty employee_id;

    /**
     * Creates a new order object
     */
    public Order() {
        this.order_ID = new SimpleIntegerProperty();
        this.order_time = new SimpleObjectProperty<>();
        this.price = new SimpleDoubleProperty();
        this.employee_id = new SimpleIntegerProperty();
    }

    /**
     * Gets the order ID
     * 
     * @return The order ID
     */
    public int getOrderID() {
        return order_ID.get();
    }

    /**
     * Sets the order ID
     * 
     * @param order_ID The order ID
     */
    public void setOrderID(int order_ID) {
        this.order_ID.set(order_ID);
    }

    /**
     * Gets the order ID property
     * 
     * @return The order ID property
     */
    public IntegerProperty orderIDProperty() {
        return order_ID;
    }

    /**
     * Gets the order time
     * 
     * @return The order time
     */
    public LocalDateTime getOrderTime() {
        return order_time.get();
    }

    /**
     * Sets the order time
     * 
     * @param order_time The order time
     */
    public void setOrderTime(LocalDateTime order_time) {
        this.order_time.set(order_time);
    }

    /**
     * Gets the order time property
     * 
     * @return The order time property
     */
    public SimpleObjectProperty<LocalDateTime> orderTimeProperty() {
        return order_time;
    }

    /**
     * Gets the price of the order
     * 
     * @return The order price
     */
    public double getPrice() {
        return price.get();
    }

    /**
     * Sets the price of the order
     * 
     * @param price The order price
     */
    public void setPrice(double price) {
        this.price.set(price);
    }

    /**
     * Gets the order price property
     * 
     * @return The order price property
     */
    public DoubleProperty priceProperty() {
        return price;
    }

    /**
     * Gets the employee ID
     * 
     * @return The employee ID
     */
    public int getEmployeeID() {
        return employee_id.get();
    }

    /**
     * Sets the employee ID
     * 
     * @param employee_id The employee ID
     */
    public void setEmployeeID(int employee_id) {
        this.employee_id.set(employee_id);
    }

    /**
     * Gets the employee ID property
     * 
     * @return The employee ID property
     */
    public IntegerProperty employeeIDProperty() {
        return employee_id;
    }
}