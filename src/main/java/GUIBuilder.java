import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class GUIBuilder {

    public GUIBuilder(String[] args) throws ClassNotFoundException, SQLException, IOException {
        Scene inventory = new Scene(FXMLLoader.load(getClass().getResource("/fxml/inventory.fxml")));
        GUIRunner.scenes.put("inventory", inventory);
        Scene cashier1 = new Scene(FXMLLoader.load(getClass().getResource("/fxml/cashier1.fxml")));
        GUIRunner.scenes.put("cashier1", cashier1);
        Scene cashier2 = new Scene(FXMLLoader.load(getClass().getResource("/fxml/cashier2.fxml")));
        GUIRunner.scenes.put("cashier2", cashier2);
        Scene employees = new Scene(FXMLLoader.load(getClass().getResource("/fxml/employees.fxml")));
        GUIRunner.scenes.put("employees", employees);
    }
}