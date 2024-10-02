import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class GUIBuilder {

    public GUIBuilder(String[] args) throws ClassNotFoundException, SQLException, IOException {
        Scene inventory = new Scene(FXMLLoader.load(getClass().getResource("/fxml/inventory.fxml")));
        GUIRunner.scenes.put("inventory", inventory);

        FXMLLoader cashier1Loader = new FXMLLoader(getClass().getResource("/fxml/cashier1.fxml"));
        Scene cashier1 = new Scene(cashier1Loader.load());
        GUIRunner.scenes.put("cashier1", cashier1);
        cashier1.setUserData(cashier1Loader.getController());

        FXMLLoader cashier2Loader = new FXMLLoader(getClass().getResource("/fxml/cashier2.fxml"));
        Scene cashier2 = new Scene(cashier2Loader.load());
        GUIRunner.scenes.put("cashier2", cashier2);
        cashier2.setUserData(cashier2Loader.getController());

        Scene employees = new Scene(FXMLLoader.load(getClass().getResource("/fxml/employees.fxml")));
        GUIRunner.scenes.put("employees", employees);
        Scene orders = new Scene(FXMLLoader.load(getClass().getResource("/fxml/orderhistory.fxml")));
        GUIRunner.scenes.put("orders", orders);
        Scene inventory_add_product = new Scene(
                FXMLLoader.load(getClass().getResource("/fxml/inventory_add_product.fxml")));
        GUIRunner.scenes.put("inventory_add_product", inventory_add_product);
        Scene inventory_edit_product = new Scene(
                FXMLLoader.load(getClass().getResource("/fxml/inventory_edit_product.fxml")));
        GUIRunner.scenes.put("inventory_edit_product", inventory_edit_product);
    }
}