import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.*;

public class HomePage extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/HomePage.fxml"));

        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        launch();
    }

}