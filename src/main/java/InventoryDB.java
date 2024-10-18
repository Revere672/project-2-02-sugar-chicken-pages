import java.sql.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

/**
 * Class for generating and sending SQL queries to the database.
 * @author Reeve Baker
 */
public class InventoryDB {
    
    /**
     * Searches the database for a product in the inventory table.
     * 
     * @param product_name Name of the product.
     * @return A list of the products similar to the product name.
     * @throws ClassNotFoundException if the database driver class is not found.
     * @throws SQLException if a database access error occurs.
     */
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

    /**
     * Gets the inventory item from a result set.
     * 
     * @param rsInventory A result set of an inventory item.
     * @return An inventory object.
     * @throws SQLException if a database access error occurs.
     */
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

    /**
     * Creates a menu object from a result set.
     * 
     * @param rsMenu A result set containing a menu item.
     * @return A menu item object.
     * @throws SQLException if a database access error occurs.
     */
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

    /**
     * Searches the database for a all products in the inventory table.
     * 
     * @return A list of all the products in the inventory.
     * @throws ClassNotFoundException if the database driver class is not found.
     * @throws SQLException if a database access error occurs.
     */
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

    /**
     * Creates a list of inventory objects from a result set.
     * 
     * @param rsInventory A result set of a list of inventory items.
     * @return A list of all the inventory objects in the result set.
     * @throws ClassNotFoundException if the database driver class is not found.
     * @throws SQLException if a database access error occurs.
     */
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

    /**
     * Inserts a new product into the inventory database.
     * 
     * @param product_name Name of the product.
     * @param supplier Supplier for the product.
     * @param cost Cost of the product
     * @param quantity Quantity of the product.
     * @param restock_quantity Restock amount to set the quantity to.
     * @throws ClassNotFoundException if the database driver class is not found.
     * @throws SQLException if a database access error occurs.
     */
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

    /**
     * Inserts a new menu item into the menu database.
     * 
     * @param menu_name Name of the menu item.
     * @param extra_cost Extra cost of a menu item.
     * @param type Type of the menu item.
     * @throws ClassNotFoundException if the database driver class is not found.
     * @throws SQLException if a database access error occurs.
     */
    public static void insertMenuItem(String menu_name, String extra_cost, String type) throws SQLException, ClassNotFoundException {
        String idStmt = 
        "SELECT " +
        "   MAX(CASE WHEN Menu_ID LIKE '60%' THEN Menu_ID END) AS Highest_Menu_ID_60, " +
        "   MAX(CASE WHEN Menu_ID LIKE '61%' THEN Menu_ID END) AS Highest_Menu_ID_61, " +
        "   MAX(CASE WHEN Menu_ID LIKE '62%' THEN Menu_ID END) AS Highest_Menu_ID_62, " +
        "   MAX(CASE WHEN Menu_ID LIKE '63%' THEN Menu_ID END) AS Highest_Menu_ID_63 " +
        "FROM menu; ";
        ResultSet rs = DBUtil.dbExecuteQuery(idStmt);
        rs.next();
        int menu_ID = 0;
        if(type.equals("Side")) {
            menu_ID = rs.getInt("Highest_Menu_ID_60") + 1;
        }
        if(type.equals("Appetizer")) {
            menu_ID = rs.getInt("Highest_Menu_ID_61") + 1;
        }
        if(type.equals("Protein")) {
            menu_ID = rs.getInt("Highest_Menu_ID_62") + 1;
        }
        if(type.equals("Misc")) {
            menu_ID = rs.getInt("Highest_Menu_ID_63") + 1;
        }
        
        String stmt = "INSERT INTO menu (menu_name, menu_ID, extra_cost, active) VALUES ('"+menu_name+"', '"+menu_ID+"', '"+Double.parseDouble(extra_cost)+"', '"+true+"');";

        try {
            DBUtil.dbExecuteUpdate(stmt);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Inserts a new ingredient into the ingredients needed database.
     * 
     * @param menu_name Name of the menu item.
     * @param product_name Name of the product.
     * @param quantity_needed Amount of product to be used in the menu item.
     * @throws ClassNotFoundException if the database driver class is not found.
     * @throws SQLException if a database access error occurs.
     */
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

    /**
     * Updates a product with the new information in the database.
     * 
     * @param product_ID ID of the product.
     * @param product_name Name of the product.
     * @param supplier Supplier for the product.
     * @param cost Cost of the product
     * @param restock_quantity Restock amount to set the quantity to.
     * @throws ClassNotFoundException if the database driver class is not found.
     * @throws SQLException if a database access error occurs.
     */
    public static void editProduct(int product_ID, String product_name, String supplier, String cost, String restock_quantity) throws SQLException, ClassNotFoundException {
        String stmt = "UPDATE inventory SET product_name='"+product_name+"', supplier='"+supplier+"', cost='"+cost+"', restock_quantity='"+restock_quantity+"' WHERE inventory_id='"+product_ID+"';";

        try {
            DBUtil.dbExecuteUpdate(stmt);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Updates the quantity amount of a product in the database.
     * 
     * @param product_ID ID of the product.
     * @param quantity Quantity of the product.
     * @throws ClassNotFoundException if the database driver class is not found.
     * @throws SQLException if a database access error occurs.
     */
    public static void updateRestock(int product_ID, double quantity) throws SQLException, ClassNotFoundException {
        String stmt = "UPDATE inventory SET quantity='"+quantity+"' WHERE inventory_id='"+product_ID+"';";

        try {
            DBUtil.dbExecuteUpdate(stmt);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Gets a product by product_ID from the database.
     * 
     * @param product_ID ID of the product.
     * @return An inventory object matching the product_ID.
     * @throws ClassNotFoundException if the database driver class is not found.
     * @throws SQLException if a database access error occurs.
     */
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

    /**
     * Searches the database for a the restock of a product in the inventory table.
     * 
     * @param product_ID ID of the product.
     * @return The restock amount of the product.
     * @throws ClassNotFoundException if the database driver class is not found.
     * @throws SQLException if a database access error occurs.
     */
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

    /**
     * Searches the database for the quantity of a product in the inventory table.
     * 
     * @param product_ID ID of the product.
     * @return The quantity of the product.
     * @throws ClassNotFoundException if the database driver class is not found.
     * @throws SQLException if a database access error occurs.
     */
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

    /**
     * Generates a list of restock objects for the restock report.
     * 
     * @return A list of restock objects.
     * @throws ClassNotFoundException if the database driver class is not found.
     * @throws SQLException if a database access error occurs.
     */
    public static ObservableList<Restock> getRestockReport() throws SQLException, ClassNotFoundException {
        ObservableList<Restock> restockList = FXCollections.observableArrayList();
        try {
            String restockQuery = "WITH Last_Order_Day AS ( " +
                "    SELECT DATE(MAX(o.Order_Time)) AS Last_Order_Date " +
                "    FROM Order_History o " +
                "), " +
                "Orders_Last_Day AS ( " +
                "    SELECT o.Order_ID " +
                "    FROM Order_History o " +
                "    JOIN Last_Order_Day lod ON DATE(o.Order_Time) = lod.Last_Order_Date " +
                "), " +
                "Daily_Usage AS ( " +
                "    SELECT " +
                "        i.Inventory_ID, " +
                "        i.Product_Name, " +
                "        SUM(ing.Quantity_Needed) AS Total_Quantity_Used " +
                "    FROM " +
                "        Inventory i " +
                "    LEFT JOIN Ingredients_Needed ing ON i.Inventory_ID = ing.Inventory_ID " +
                "    LEFT JOIN Order_Items oi ON ing.Menu_Name = oi.Side_1 OR ing.Menu_Name = oi.Side_2 " +
                "                                OR ing.Menu_Name = oi.Protein_1 OR ing.Menu_Name = oi.Protein_2 " +
                "                                OR ing.Menu_Name = oi.Protein_3 OR ing.Menu_Name = oi.Misc_Item " +
                "    JOIN Orders_Last_Day old ON oi.Order_ID = old.Order_ID " +
                "    GROUP BY i.Inventory_ID, i.Product_Name " +
                ") " +
                "SELECT " +
                "    i.Inventory_ID, " +
                "    i.Product_Name, " +
                "    COALESCE(du.Total_Quantity_Used, 0) AS Total_Quantity_Used, " +
                "    i.Quantity AS Current_Inventory, " +
                "    CASE " +
                "        WHEN i.Quantity <= 0 THEN 'Yes' " +
                "        WHEN COALESCE(du.Total_Quantity_Used, 0) > (i.Quantity / 2) THEN 'Yes' " +
                "        ELSE 'No' " +
                "    END AS Restock_Recommendation " +
                "FROM Inventory i " +
                "LEFT JOIN Daily_Usage du ON i.Inventory_ID = du.Inventory_ID " +
                "WHERE " +  
                "   CASE " +
                "       WHEN i.Quantity <= 0 THEN 'Yes' " +
                "       WHEN COALESCE(du.Total_Quantity_Used, 0) > (i.Quantity / 2) THEN 'Yes' " +
                "       ELSE 'No' " + 
                "   END = 'Yes'" +
                "ORDER BY i.Inventory_ID;";
            ResultSet rsRestock = DBUtil.dbExecuteQuery(restockQuery);
            while (rsRestock.next()) {
                Restock restock = new Restock();
                restock.setInventoryID(rsRestock.getInt("inventory_ID"));
                restock.setProductName(rsRestock.getString("product_name"));
                restock.setQuantity(rsRestock.getDouble("total_quantity_used"));
                restock.setCurrInv(rsRestock.getDouble("current_inventory"));
                restock.setRestockRecommendation(rsRestock.getString("restock_recommendation"));
                restockList.add(restock);
            }
            return restockList;
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
