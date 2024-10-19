import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

/**
 * Class for building the GUI.
 * This class is used to load the scenes for the GUI.
 * It is used to load the inventory, cashier, employees, order history, and
 * analysis scenes.
 */
public class GUIBuilder {

    /**
     * Default constructor for the GUIBuilder class.
     * 
     * @throws ClassNotFoundException if the database driver class is not found.
     * @throws SQLException if a database access error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public GUIBuilder() throws ClassNotFoundException, SQLException, IOException {
        loadScenes();
    }

    /**
     * Loads the scenes for the GUI.
     * This method loads the inventory, cashier, employees, order history, and
     * analysis scenes.
     * 
     * @throws ClassNotFoundException if the database driver class is not found.
     * @throws SQLException if a database access error occurs.
     * @throws IOException if an I/O error occurs.
     */
    private void loadScenes() throws IOException, SQLException, ClassNotFoundException {
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
        CashierController.buildGrids(cashier1, cashier2);

        Scene employees = new Scene(FXMLLoader.load(getClass().getResource("/fxml/employees.fxml")));
        GUIRunner.scenes.put("employees", employees);

        Scene order_history = new Scene(FXMLLoader.load(getClass().getResource("/fxml/orderhistory.fxml")));
        GUIRunner.scenes.put("order_history", order_history);

        Scene analysis = new Scene(FXMLLoader.load(getClass().getResource("/fxml/analysis.fxml")));
        GUIRunner.scenes.put("analysis", analysis);
    }
}