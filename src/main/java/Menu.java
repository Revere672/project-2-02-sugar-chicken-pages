import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Menu {
    private IntegerProperty menu_ID;
    private StringProperty menu_name;
    private DoubleProperty extra_cost;
    private BooleanProperty active;

    public Menu() {
        this.menu_ID = new SimpleIntegerProperty();
        this.menu_name = new SimpleStringProperty();
        this.extra_cost = new SimpleDoubleProperty();
        this.active = new SimpleBooleanProperty();
    }

    public int getMenuID() {
        return menu_ID.get();
    }

    public void setMenuID(int menu_ID) {
        this.menu_ID.set(menu_ID);
    }

    public IntegerProperty menuIDProperty() {
        return menu_ID;
    }

    public String getMenuName() {
        return menu_name.get();
    }

    public void setMenuName(String menu_name) {
        this.menu_name.set(menu_name);
    }

    public StringProperty menuNameProperty() {
        return menu_name;
    }

    public double getExtraCost() {
        return extra_cost.get();
    }

    public void setExtraCost(double extra_cost) {
        this.extra_cost.set(extra_cost);
    }

    public DoubleProperty extraCostProperty() {
        return extra_cost;
    }

    public boolean isActive() {
        return active.get();
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    public BooleanProperty activeProperty() {
        return active;
    }
}
