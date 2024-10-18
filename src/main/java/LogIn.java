import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controller class for log in page analysis.
 * 
 * @author Joshua George
 */
public class LogIn {

    /**
     * The TextField for entering the username.
     */
    @FXML
    TextField username;

    /**
     * The PasswordField for entering the password.
     */
    @FXML
    PasswordField password;

    /**
     * The Button for submitting the login form.
     */
    @FXML
    Button log_in_button;

    /**
     * Initializes the login form by clearing the username and password fields,
     * setting the focus to the username field, and clearing the scenes in the
     * GUIRunner.
     * It also creates a new instance of GUIBuilder.
     *
     * @throws ClassNotFoundException if the JDBC driver class is not found.
     * @throws SQLException           if a database access error occurs.
     * @throws IOException            if an I/O error occurs.
     */
    @FXML
    public void initialize() throws ClassNotFoundException, SQLException, IOException {
        username.clear();
        password.clear();
        username.requestFocus();
        GUIRunner.scenes.clear();
        GUIBuilder build = new GUIBuilder();
    }

    /**
     * Handles the action event for moving to the next entry in the login form.
     * This method sets the focus to the password field when the action event is
     * triggered.
     *
     * @param e the action event that triggers this method.
     * @throws SQLException           if a database access error occurs.
     * @throws ClassNotFoundException if the JDBC driver class is not found.
     */
    @FXML
    public void nextEntry(ActionEvent e) throws SQLException, ClassNotFoundException {
        password.requestFocus();
    }

    /**
     * Handles the login action event.
     * This method executes a SQL query to verify the username and password entered
     * by the user.
     * If the credentials are valid, it sets the current user and changes the scene
     * based on the user's role.
     * If the credentials are invalid, it prompts the user to re-enter the
     * credentials.
     *
     * @param e the action event that triggers this method.
     * @throws SQLException           if a database access error occurs.
     * @throws ClassNotFoundException if the JDBC driver class is not found.
     */
    @FXML
    public void logIn(ActionEvent e) throws SQLException, ClassNotFoundException {
        String scriptQry = "SELECT * FROM Passwords WHERE Employee_ID = '" + username.getText() + "' AND password = '"
                + password.getText() + "';";
        ResultSet rs = DBUtil.dbExecuteQuery(scriptQry);
        if (rs.next()) {
            GUIRunner.currentUser = rs.getInt(1);
            if (rs.getInt(1) >= 200000) {
                GUIRunner.isManager = true;
                // System.out.println("This user is a Manager");
                GUIRunner.changeScene("inventory");
            } else {
                GUIRunner.isManager = false;
                // System.out.println("This user is a Cashier");
                GUIRunner.changeScene("cashier1");

            }
            System.out.println("Login Successful");
        } else {
            System.out.println("Login Failed");
            username.requestFocus();
        }
        username.clear();
        password.clear();
    }
}