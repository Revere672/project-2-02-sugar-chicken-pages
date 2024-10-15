import java.sql.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class InventoryDB {
    
    public static ObservableList<Inventory> searchInventory(String product_name) throws SQLException, ClassNotFoundException {
        String stmt = "SELECT * FROM inventory WHERE product_name LIKE '%"+product_name+"%' ORDER BY inventory_id ASC;";

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

    private static Menu getMenuResult(ResultSet rsMenu) throws SQLException {
        Menu menu = null;

        if (rsMenu.next()) {
            menu = new Menu();
            menu.setMenuID(rsMenu.getInt("menu_ID"));
            menu.setMenuName(rsMenu.getString("menu_name"));
            menu.setExtraCost(rsMenu.getDouble("extra_cost"));
            menu.setActive(rsMenu.getBoolean("active"));
        }

        return menu;
    }

    public static ObservableList<Inventory> searchInventories() throws SQLException, ClassNotFoundException {
        String stmt = "SELECT * FROM inventory ORDER BY inventory_id ASC;";

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

    public static void insertMenuItem(String menu_name, String extra_cost) throws SQLException, ClassNotFoundException {
        String idStmt = "SELECT * FROM menu ORDER BY menu_ID DESC LIMIT 1;";
        ResultSet rs = DBUtil.dbExecuteQuery(idStmt);
        Menu item = getMenuResult(rs);
        int menu_ID = item.getMenuID() + 1;
        String stmt = "INSERT INTO menu (menu_name, menu_ID, extra_cost, active) VALUES ('"+menu_name+"', '"+menu_ID+"', '"+Double.parseDouble(extra_cost)+"', '"+true+"');";

        try {
            DBUtil.dbExecuteUpdate(stmt);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void insertIngredientsNeeded(String menu_name, String product_name, String quantity_needed) throws SQLException, ClassNotFoundException {
        try {
        String idStmt = "SELECT * FROM inventory WHERE product_name = '"+product_name+"';";
        ResultSet rs = DBUtil.dbExecuteQuery(idStmt);
        rs.next();
        int inventory_ID = rs.getInt("inventory_ID");
        String stmt = "INSERT INTO ingredients_needed (menu_name, inventory_ID, quantity_needed) VALUES ('"+menu_name+"', '"+inventory_ID+"', '"+Double.parseDouble(quantity_needed)+"');";

        DBUtil.dbExecuteUpdate(stmt);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void editProduct(int product_ID, String product_name, String supplier, String cost, String restock_quantity) throws SQLException, ClassNotFoundException {
        String stmt = "UPDATE inventory SET product_name='"+product_name+"', supplier='"+supplier+"', cost='"+cost+"', restock_quantity='"+restock_quantity+"' WHERE inventory_id='"+product_ID+"';";

        try {
            DBUtil.dbExecuteUpdate(stmt);
        } catch (Exception e) {
            throw e;
        }
    }

    public static void updateRestock(int product_ID, double quantity) throws SQLException, ClassNotFoundException {
        String stmt = "UPDATE inventory SET quantity='"+quantity+"' WHERE inventory_id='"+product_ID+"';";

        try {
            DBUtil.dbExecuteUpdate(stmt);
        } catch (Exception e) {
            throw e;
        }
    }

    public static Inventory findProduct(int product_ID) throws SQLException, ClassNotFoundException {
        try {
            String findStmt = "SELECT * FROM inventory WHERE inventory_id='"+product_ID+"';";
            ResultSet rs = DBUtil.dbExecuteQuery(findStmt);
            return InventoryDB.getInventoryResult(rs);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static double getRestock(int product_ID) throws SQLException, ClassNotFoundException {
        try {
            String findStmt = "SELECT * FROM inventory WHERE inventory_id='"+product_ID+"';";
            ResultSet rs = DBUtil.dbExecuteQuery(findStmt);
            if (rs.next()) {
                return rs.getDouble("restock_quantity");
            }
            return 0.0;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static double getQuantity(int product_ID) throws SQLException, ClassNotFoundException {
        try {
            String findStmt = "SELECT * FROM inventory WHERE inventory_id='"+product_ID+"';";
            ResultSet rs = DBUtil.dbExecuteQuery(findStmt);
            if (rs.next()) {
                return rs.getDouble("quantity");
            }
            return 0.0;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
