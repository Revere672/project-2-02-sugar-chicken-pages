import DBUtil;
import java.sql.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class InventoryDB {
    
    public static Inventory searchInventory(String product_name) throws SQLException, ClassNotFoundException {
        String stmt = "SELECT * FROM inventory WHERE product_name="+product_name;

        try {
            ResultSet rsInventory = DBUtil.dbExecuteQuery(stmt);

            Inventory inventory = getInventoryResult(rsInventory);

            return inventory;
        } catch (Exception e) {
            throw e;
        }
    }

    private static Inventory getInventoryResult(ResultSet rsInventory) throws SQLException {
        Inventory inventory = null;

        if (rsInventory.next()) {
            inventory = new Inventory();
            inventory.setInventoryID(rsInventory.getInt("inventory_ID"));
            inventory.setProductName(rsInventory.getString("product_name"));
            inventory.setSupplier(rsInventory.getString("supplier"));
            inventory.setCost(rsInventory.getDouble("cost"));
            inventory.setQuantity(rsInventory.getInt("quantity"));
        }

        return inventory;
    }

    public static ObservableList<Inventory> searchInventories() throws SQLException, ClassNotFoundException {
        String stmt = "SELECT * FROM inventory";

        try {
            ResultSet rsInventory = DBUtil.dbExecuteQuery(stmt);

            ObservableList<Inventory> inventoryList = getInventoryList(rsInventory);

            return inventoryList;
        } catch (Exception e) {
            throw e;
        }
    }

    private static ObservableList<Inventory> getInventoryList(ResultSet rsInventory) throws SQLException, ClassNotFoundException {
        ObservableList<Inventory> inventoryList = FXCollections.observableArrayList();

        while (rsInventory.next()) {
            Inventory inventory = new Inventory();
            inventory.setInventoryID(rsInventory.getInt("inventory_ID"));
            inventory.setProductName(rsInventory.getString("product_name"));
            inventory.setSupplier(rsInventory.getString("supplier"));
            inventory.setCost(rsInventory.getDouble("cost"));
            inventory.setQuantity(rsInventory.getInt("quantity"));
        }

        return inventoryList;
    }

    public static void insertProduct(int inventory_ID, String product_name, String supplier, int cost, int quantity) throws SQLException, ClassNotFoundException {
        String stmt = "INSERT INTO inventory (inventory_ID, product_name, supplier, cost, quantity) VALUES ('"+inventory_ID+"', '"+product_name+"', '"+supplier+"', '"+cost+"', '"+quantity+"');";

        try {
            DBUtil.dbExecuteUpdate(stmt);
        } catch (Exception e) {
            throw e;
        }
    }
}
