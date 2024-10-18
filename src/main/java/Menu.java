import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Class for creating menu objects.
 * 
 * @author Joshua George
 */
public class Menu {
    /**
     * The unique identifier for the menu.
     * This property is used to store and observe changes to the menu ID.
     */
    private IntegerProperty menu_ID;

    /**
     * The name of the menu.
     * This property is used to store and observe changes to the menu name.
     */
    private StringProperty menu_name;

    /**
     * The additional cost associated with the menu.
     * This property is used to store and observe changes to the extra cost.
     */
    private DoubleProperty extra_cost;

    /**
     * Indicates whether the menu is active.
     * This property is used to store and observe changes to the active status.
     */
    private BooleanProperty active;

    /**
     * Default constructor for the Menu class.
     * This constructor initializes the properties of the Menu object with default
     * values.
     * It creates new instances of SimpleIntegerProperty, SimpleStringProperty,
     * SimpleDoubleProperty, and SimpleBooleanProperty
     * for menu_ID, menu_name, extra_cost, and active respectively.
     */
    public Menu() {
        this.menu_ID = new SimpleIntegerProperty();
        this.menu_name = new SimpleStringProperty();
        this.extra_cost = new SimpleDoubleProperty();
        this.active = new SimpleBooleanProperty();
    }

    /**
     * Gets the menu ID.
     *
     * @return the menu ID.
     */
    public int getMenuID() {
        return menu_ID.get();
    }

    /**
     * Sets the menu ID.
     *
     * @param menu_ID the new menu ID.
     */
    public void setMenuID(int menu_ID) {
        this.menu_ID.set(menu_ID);
    }

    /**
     * Gets the menu ID property.
     *
     * @return the menu ID property.
     */
    public IntegerProperty menuIDProperty() {
        return menu_ID;
    }

    /**
     * Gets the menu name.
     *
     * @return the menu name.
     */
    public String getMenuName() {
        return menu_name.get();
    }

    /**
     * Sets the menu name.
     *
     * @param menu_name the new menu name.
     */
    public void setMenuName(String menu_name) {
        this.menu_name.set(menu_name);
    }

    /**
     * Gets the menu name property.
     *
     * @return the menu name property.
     */
    public StringProperty menuNameProperty() {
        return menu_name;
    }

    /**
     * Gets the extra cost.
     *
     * @return the extra cost.
     */
    public double getExtraCost() {
        return extra_cost.get();
    }

    /**
     * Sets the extra cost.
     *
     * @param extra_cost the new extra cost.
     */
    public void setExtraCost(double extra_cost) {
        this.extra_cost.set(extra_cost);
    }

    /**
     * Gets the extra cost property.
     *
     * @return the extra cost property.
     */
    public DoubleProperty extraCostProperty() {
        return extra_cost;
    }

    /**
     * Checks if the menu is active.
     *
     * @return true if the menu is active, false otherwise.
     */
    public boolean isActive() {
        return active.get();
    }

    /**
     * Sets the active status of the menu.
     *
     * @param active the new active status.
     */
    public void setActive(boolean active) {
        this.active.set(active);
    }

    /**
     * Gets the active property.
     *
     * @return the active property.
     */
    public BooleanProperty activeProperty() {
        return active;
    }
}
