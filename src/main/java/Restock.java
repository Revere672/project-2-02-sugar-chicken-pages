import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Restock {
    private IntegerProperty inventory_ID;
    private StringProperty product_name;
    private DoubleProperty quantity;
    private DoubleProperty currInv;
    private StringProperty restockRec;

    public Restock() {
        this.inventory_ID = new SimpleIntegerProperty();
        this.product_name = new SimpleStringProperty();
        this.quantity = new SimpleDoubleProperty();
        this.currInv = new SimpleDoubleProperty();
        this.restockRec = new SimpleStringProperty();
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


    public double getQuantity() {
        return quantity.get();
    }

    public void setQuantity(double quantity) {
        this.quantity.set(quantity);
    }

    public DoubleProperty quantityProperty() {
        return quantity;
    }

    public double getCurrInv() {
        return currInv.get();
    }

    public void setCurrInv(double inv_val) {
        this.currInv.set(inv_val);
    }

    public DoubleProperty currInvProperty() {
        return currInv;
    }

    public String getRestockRecommendation() {
        return restockRec.get();
    }

    public void setRestockRecommendation(String restock_val) {
        this.restockRec.set(restock_val);
    }

    public StringProperty restockRecommendationProperty() {
        return restockRec;
    }

    public String formattedString() {
        return "ID: " + getInventoryID() + "\nName: " + getProductName() + "\nCurrent Quantity: " + getCurrInv() + "\n1-day usage: " + getQuantity() + "\nRestock: " + getRestockRecommendation();
    }
}
