import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Class for creating inventory objects.
 * @author Reeve Baker
 */
public class Inventory {
    private IntegerProperty inventory_ID;
    private StringProperty product_name;
    private StringProperty supplier;
    private DoubleProperty cost;
    private DoubleProperty quantity;

    /**
     * Creates a new inventory object.
     */
    public Inventory() {
        this.inventory_ID = new SimpleIntegerProperty();
        this.product_name = new SimpleStringProperty();
        this.supplier = new SimpleStringProperty();
        this.cost = new SimpleDoubleProperty();
        this.quantity = new SimpleDoubleProperty();
    }

    /**
     * Get the ID of the product.
     * 
     * @return The ID of the product.
     */
    public int getInventoryID() {
        return inventory_ID.get();
    }

    /**
     * Set the ID of the product.
     * 
     * @param inventory_ID The ID of the product.
     */
    public void setInventoryID(int inventory_ID) {
        this.inventory_ID.set(inventory_ID);
    }

    /**
     * Get the ID property object of the product.
     * 
     * @return The ID property object of the product.
     */
    public IntegerProperty inventoryIDProperty() {
        return inventory_ID;
    }

    /**
     * Get the name of the product.
     * 
     * @return The name of the product.
     */
    public String getProductName() {
        return product_name.get();
    }

    /**
     * Set the name of the product.
     * 
     * @param inventory_ID The name of the product.
     */
    public void setProductName(String product_name) {
        this.product_name.set(product_name);
    }

    /**
     * Get the name property object of the product.
     * 
     * @return The name property object of the product.
     */
    public StringProperty productNameProperty() {
        return product_name;
    }

    /**
     * Get the supplier of the product.
     * 
     * @return The supplier of the product.
     */
    public String getSupplier() {
        return supplier.get();
    }

    /**
     * Set the supplier of the product.
     * 
     * @param inventory_ID The supplier of the product.
     */
    public void setSupplier(String supplier) {
        this.supplier.set(supplier);
    }

    /**
     * Get the supplier property object of the product.
     * 
     * @return The supplier property object of the product.
     */
    public StringProperty supplierProperty() {
        return supplier;
    }

    /**
     * Get the cost of the product.
     * 
     * @return The cost of the product.
     */
    public double getCost() {
        return cost.get();
    }

    /**
     * Set the cost of the product.
     * 
     * @param inventory_ID The cost of the product.
     */
    public void setCost(double cost) {
        this.cost.set(cost);
    }

    /**
     * Get the cost property object of the product.
     * 
     * @return The cost property object of the product.
     */
    public DoubleProperty costProperty() {
        return cost;
    }

    /**
     * Get the quantity of the product.
     * 
     * @return The quantity of the product.
     */
    public double getQuantity() {
        return quantity.get();
    }

    /**
     * Set the quantity of the product.
     * 
     * @param inventory_ID The quantity of the product.
     */
    public void setQuantity(double quantity) {
        this.quantity.set(quantity);
    }

    /**
     * Get the quantity property object of the product.
     * 
     * @return The quantity property object of the product.
     */
    public DoubleProperty quantityProperty() {
        return quantity;
    }
}
