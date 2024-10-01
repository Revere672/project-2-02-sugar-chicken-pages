import java.sql.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class InventoryDB {
    
    public static ObservableList<Inventory> searchInventory(String product_name) throws SQLException, ClassNotFoundException {
        String stmt = "SELECT * FROM inventory WHERE product_name LIKE '%"+product_name+"%';";

        try {
            ResultSet rsInventory = DBUtil.dbExecuteQuery(stmt);

            ObservableList<Inventory> inventory_list = getInventoryList(rsInventory);

            return inventory_list;
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
            inventory.setQuantity(Math.round((rsInventory.getDouble("quantity") / rsInventory.getDouble("restock_quantity")) * 1000.0) / 10.0);
        }

        return inventory;
    }

    public static ObservableList<Inventory> searchInventories() throws SQLException, ClassNotFoundException {
        String stmt = "SELECT * FROM inventory;";

        try {
            ResultSet rsInventory = DBUtil.dbExecuteQuery(stmt);

            ObservableList<Inventory> inventory_list = getInventoryList(rsInventory);

            return inventory_list;
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
            inventory.setQuantity(Math.round((rsInventory.getDouble("quantity") / rsInventory.getDouble("restock_quantity")) * 100.0));
            inventoryList.add(inventory);
        }

        return inventoryList;
    }

    public static void insertProduct(String product_name, String supplier, String cost, String quantity, String restock_quantity) throws SQLException, ClassNotFoundException {
        String idStmt = "SELECT * FROM inventory ORDER BY inventory_ID DESC LIMIT 1;";
        ResultSet rs = DBUtil.dbExecuteQuery(idStmt);
        Inventory item = getInventoryResult(rs);
        int inventory_ID = item.getInventoryID() + 1;
        String stmt = "INSERT INTO inventory (inventory_ID, product_name, supplier, cost, quantity, restock_quantity) VALUES ('"+inventory_ID+"', '"+product_name+"', '"+supplier+"', '"+Double.parseDouble(cost)+"', '"+Double.parseDouble(quantity)+"', '"+Double.parseDouble(restock_quantity)+"');";

        try {
            DBUtil.dbExecuteUpdate(stmt);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void editProduct(String orig_name, String product_name, String supplier, String cost, String restock_quantity) throws SQLException, ClassNotFoundException {
        String stmt = "UPDATE inventory SET product_name='"+product_name+"', supplier='"+supplier+"', cost='"+cost+"', restock_quantity='"+restock_quantity+"' WHERE product_name='"+orig_name+"';";

        try {
            DBUtil.dbExecuteUpdate(stmt);
        } catch (Exception e) {
            throw e;
        }
    }
}
