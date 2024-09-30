package application;

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

    public void logIn(ActionEvent e) {
        String scriptQry = "SELECT * FROM Passwords WHERE Employee_ID = " + username.getText() + "AND password = " + password.getText() + ";";
        ResultSet rs = DBUtil.dbExecuteQuery(scriptQry);
        System.out.println(rs.next());
    }
}