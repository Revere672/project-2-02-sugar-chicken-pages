import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class acts as a restock record, giving information about the inventory ID, product name, 
 * current amount of stock for a specific inventory item, quantity of said inventory item used in one day, 
 * and a recommendation for whether a restock should be considered or not
 */
public class Restock {
    private IntegerProperty inventory_ID;
    private StringProperty product_name;
    private DoubleProperty quantity;
    private DoubleProperty currInv;
    private StringProperty restockRec;

    /**
     * Default constructor that initializes all properties of the class: inventory ID, product name, 
     * quantity, current inventory, restock recommendation
     */
    public Restock() {
        this.inventory_ID = new SimpleIntegerProperty();
        this.product_name = new SimpleStringProperty();
        this.quantity = new SimpleDoubleProperty();
        this.currInv = new SimpleDoubleProperty();
        this.restockRec = new SimpleStringProperty();
    }

    /**
     * Gets the inventory ID of a specific inventory item
     * 
     * @return the inventory ID as an int
     */
    public int getInventoryID() {
        return inventory_ID.get();
    }

    /**
     * Update the inventory ID of a specific inventory item with a new one
     * 
     * @param inventory_ID as the new inventory ID for the item
     */
    public void setInventoryID(int inventory_ID) {
        this.inventory_ID.set(inventory_ID);
    }

    /**
     * Returns the property representing the inventory ID of an item
     * 
     * @return the inventory ID as an IntegerProperty
     */
    public IntegerProperty inventoryIDProperty() {
        return inventory_ID;
    }

    /**
     * Gets the name of a specific inventory item
     * 
     * @return the name of an inventory item as a string
     */
    public String getProductName() {
        return product_name.get();
    }

    /**
     * Update the name of a specific inventory item with a new one
     * 
     * @param product_name as the new name for the item
     */
    public void setProductName(String product_name) {
        this.product_name.set(product_name);
    }

    /**
     * Returns the property representing the name of an inventory item
     * 
     * @return the product name as a StringProperty
     */
    public StringProperty productNameProperty() {
        return product_name;
    }

    /**
     * Gets the amount of a specific inventory item used in a singular day
     * 
     * @return the amount of the item used in one day as a double
     */
    public double getQuantity() {
        return quantity.get();
    }

    /**
     * Updates the amount of an inventory item during a single day
     * 
     * @param quantity as the new amount of the item used
     */
    public void setQuantity(double quantity) {
        this.quantity.set(quantity);
    }

    /**
     * Returns the property representing the amount of the inventory item used in one day
     * 
     * @return the amount of the inventory used as a DoubleProperty
     */
    public DoubleProperty quantityProperty() {
        return quantity;
    }

    /**
     * Gets the amount of a specific inventory item currently in stock
     * 
     * @return the amount of the item current in stock as a double
     */
    public double getCurrInv() {
        return currInv.get();
    }

    /**
     * Updates the amount of an inventory item currently in stock
     * 
     * @param inv_val as the new amount of the item in stock
     */
    public void setCurrInv(double inv_val) {
        this.currInv.set(inv_val);
    }

    /**
     * Returns the property representing the amount of an inventory item in stock
     * 
     * @return the amount of the inventory item in stock as a DoubleProperty
     */
    public DoubleProperty currInvProperty() {
        return currInv;
    }

    /**
     * Gets the restock recommendation for a specific inventory item
     * 
     * @return the restock recommendation as a string
     */
    public String getRestockRecommendation() {
        return restockRec.get();
    }

    /**
     * Updates the restock commendation 
     * 
     * @param restock_val as the new recommendation
     */
    public void setRestockRecommendation(String restock_val) {
        this.restockRec.set(restock_val);
    }

    /**
     * Returns the property representing the restock recommendation for an inventory item
     * 
     * @return the restock recommendation for an inventory item as a StringProperty
     */
    public StringProperty restockRecommendationProperty() {
        return restockRec;
    }

    /**
     * Returns the a formatted string of the restock record, which contains the: 
     * inventory ID, product name, current quantity in stock, quantity of the item used in one day, and its restock commendation
     * 
     * @return the formatted string with all information as a string
     */
    public String formattedString() {
        return "ID: " + getInventoryID() + "\nName: " + getProductName() + "\nCurrent Quantity: " + getCurrInv() + "\n1-day usage: " + getQuantity() + "\nRestock: " + getRestockRecommendation();
    }
}
