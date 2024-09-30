import java.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.sql.*;
import javax.sql.rowset.*;

public class LogIn {

    @FXML TextField username;
    @FXML PasswordField password;
    @FXML Button logInButton;

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
        }
        else {
            System.out.println("Login Failed");
        }
    }
}