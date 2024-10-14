import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AnalysisController {

    @FXML
    private void xReport(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        System.out.println(GUIRunner.xReport());
    }
    @FXML
    private void zReport(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        System.out.println(GUIRunner.zReport());
    }

    @FXML
    private void changeToEmployees(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("employees");
    }
    @FXML
    private void changeToInventory(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("inventory");
    }
    @FXML
    private void changeToOrderHistory(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
         GUIRunner.changeScene("order_history");
    }
    @FXML
    private void changeToAnalysis(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("analysis");
   }

    @FXML
    private Button cashier;
    @FXML
    private Button analysis;
    @FXML
    private Button inventory;
    @FXML
    private Button employees;
    @FXML
    private Button order_history;
}
