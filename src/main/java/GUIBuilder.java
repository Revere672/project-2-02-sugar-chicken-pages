import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class GUIBuilder {

    public GUIBuilder(String[] args) throws ClassNotFoundException, SQLException, IOException {
        Scene inventory = new Scene(FXMLLoader.load(getClass().getResource("/fxml/inventory.fxml")));
        GUIRunner.scenes.put("inventory", inventory);
    }
}