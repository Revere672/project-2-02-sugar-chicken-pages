import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Inventory {
    private IntegerProperty inventory_ID;
    private StringProperty product_name;
    private StringProperty supplier;
    private DoubleProperty cost;
    private IntegerProperty quantity;

    public Inventory() {
        this.inventory_ID = new SimpleIntegerProperty();
        this.product_name = new SimpleStringProperty();
        this.supplier = new SimpleStringProperty();
        this.cost = new SimpleDoubleProperty();
        this.quantity = new SimpleIntegerProperty();
    }

    public int getInventoryID() {
        return inventory_ID.get();
    }

    public void setInventoryID(int inventory_ID) {
        this.inventory_ID.set(inventory_ID);
    }

    public IntegerProperty inventoryIDProperty() {
        return inventory_ID;
    }

    public String getProductName() {
        return product_name.get();
    }

    public void setProductName(String product_name) {
        this.product_name.set(product_name);
    }

    public StringProperty productNameProperty() {
        return product_name;
    }

    public String getSupplier() {
        return supplier.get();
    }

    public void setSupplier(String supplier) {
        this.supplier.set(supplier);
    }

    public StringProperty supplierProperty() {
        return supplier;
    }

    public double getCost() {
        return cost.get();
    }

    public void setCost(double cost) {
        this.cost.set(cost);
    }

    public DoubleProperty costProperty() {
        return cost;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }
}
