import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIRunner extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/inventory.fxml"));

        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DBUtil.dbConnect(args[0],args[1],args[1],args[2]);

        launch();
    }

}