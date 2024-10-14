import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.time.LocalDateTime;

public class Order {
    private IntegerProperty order_ID;
    private SimpleObjectProperty<LocalDateTime> order_time;
    private DoubleProperty price;
    private IntegerProperty employee_id;

    public Order() {
        this.order_ID = new SimpleIntegerProperty();
        this.order_time = new SimpleObjectProperty<>();
        this.price = new SimpleDoubleProperty();
        this.employee_id = new SimpleIntegerProperty();
    }


    public int getOrderID() {
        return order_ID.get();
    }

    public void setOrderID(int order_ID) {
        this.order_ID.set(order_ID);
    }

    public IntegerProperty orderIDProperty() {
        return order_ID;
    }

    public LocalDateTime getOrderTime() {
        return order_time.get();
    }

    public void setOrderTime(LocalDateTime order_time) {
        this.order_time.set(order_time);
    }

    public SimpleObjectProperty<LocalDateTime> orderTimeProperty() {
        return order_time;
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public int getEmployeeID() {
        return employee_id.get();
    }

    public void setEmployeeID(int employee_id) {
        this.employee_id.set(employee_id);
    }

    public IntegerProperty employeeIDProperty() {
        return employee_id;
    }
}