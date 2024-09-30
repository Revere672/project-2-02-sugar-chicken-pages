import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LogIn {

    @FXML TextField username;
    @FXML PasswordField password;
    @FXML Button logInButton;

    @FXML
    public void nextEntry(ActionEvent e) throws SQLException, ClassNotFoundException {
        password.requestFocus();
    }

    @FXML
    public void logIn(ActionEvent e) throws SQLException, ClassNotFoundException {
        String scriptQry = "SELECT * FROM Passwords WHERE Employee_ID = '" + username.getText() + "' AND password = '" + password.getText() + "';";
        ResultSet rs = DBUtil.dbExecuteQuery(scriptQry);
        if(rs.next()) {
            if(rs.getInt(1) >= 200000) {
                GUIRunner.isManager = true;
                System.out.println("This user is a Manager");
            }
            else {
                GUIRunner.isManager = false;
                System.out.println("This user is a Cashier");

            }
            System.out.println("Login Successful");
            GUIRunner.changeScene("inventory");
        }
        else {
            System.out.println("Login Failed");
        }
    }
}