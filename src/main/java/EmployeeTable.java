import java.sql.Date;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class EmployeeTable {
    @FXML
    private Button log_out_button;
    @FXML
    private Pane edit_pane;

    @FXML
    private AnchorPane background_edit;
    
    @FXML
    private Button cashier;
    @FXML
    private Button inventory;
    @FXML
    private Button employees;
    @FXML
    private Button order_history;
    @FXML
    private TextField search_bar;
    @FXML
    private TableView<Employee> employee_table;
    @FXML
    private TableColumn<Employee, Integer> employee_id_col;
    @FXML
    private TableColumn<Employee, String> name_col;
    @FXML
    private TableColumn<Employee, String> email_col;
    @FXML
    private TableColumn<Employee, String> role_col;
    @FXML
    private TableColumn<Employee, Date> Last_login_col;
    @FXML
    private TableColumn<Employee, Boolean> active_col;

    @FXML
    private ChoiceBox<String> employee_dropdown;

    @FXML
    private ChoiceBox<String> status_dropdown;

    @FXML
    private TextField edit_email;

    @FXML
    private Text role_text;

    @FXML
    private Text login_text;

    @FXML
    private Text eid_text;

    @FXML
    private Button Apply;

    @FXML
    private Button Cancel;



    @FXML
    private void searchEmployee(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {// searches when
                                                                                                      // search button
                                                                                                      // is hit
        try {
            ObservableList<Employee> e = EmployeeDB.find_Employee(search_bar.getText());// search bar will retreive an
                                                                                        // employee based off text
            populateEmployees(e);// populate with employee object e
        } catch (SQLException error) {
            error.printStackTrace();
            throw error;
        }
    }

    @FXML
    private void searchEmployees(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            ObservableList<Employee> employeeData = EmployeeDB.searchEmployees();// get employee data
            populateEmployees(employeeData);// populate table
            if(employee_dropdown != null){//if edit button is clicked, new scene
                ObservableList<String> e_names = FXCollections.observableArrayList();
                for(Employee e : employeeData){
                    e_names.add(e.getEmployeeName());
                }
                employee_dropdown.setItems(e_names);
            }
        } catch (SQLException error) {
            System.out.println("Error occurred while getting employees information from DB.\n" + error);
            throw error;
        }
    }

    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        edit_pane.setVisible(false);
        setBackground(false);
        employee_id_col.setCellValueFactory(cellData -> cellData.getValue().EmployeeIDProperty().asObject());
        name_col.setCellValueFactory(cellData -> cellData.getValue().EmployeeNameProperty());
        email_col.setCellValueFactory(cellData -> cellData.getValue().EmployeeEmailProperty());
        role_col.setCellValueFactory(cellData -> cellData.getValue().EmployeeRoleProperty());
        Last_login_col.setCellValueFactory(cellData -> cellData.getValue().EmployeeLoginProperty());
        active_col.setCellValueFactory(cellData -> cellData.getValue().EmployeeStatusProperty());
        searchEmployees(null);
        if(status_dropdown != null){
            ObservableList<String> status_list = FXCollections.observableArrayList();
            status_list.add("Active");
            status_list.add("Inactive");
            status_dropdown.setItems(status_list);
        }
    }

    @FXML
    private void setBackground(Boolean value) {
        if (value) {
            background_edit.getChildren().forEach(node -> {
                if (!(node.equals(edit_pane))) {
                    node.setOpacity(0.3);
                    node.setDisable(true);
                }
            });
        }
        else {
            background_edit.getChildren().forEach(node -> {
                if (!(node.equals(edit_pane))) {
                    node.setOpacity(1);
                    node.setDisable(false);
                }
            });
        }
    }

    @FXML
    private void populateEmployee(Employee e) throws ClassNotFoundException {
        ObservableList<Employee> employeeData = FXCollections.observableArrayList();// make a list for employee data
                                                                                    // thats visible
        employeeData.add(e);// add employee e
        employee_table.setItems(employeeData);// set
    }

    @FXML
    private void populateEmployees(ObservableList<Employee> employeeData) throws ClassNotFoundException {
        employee_table.setItems(employeeData);// set
    }

    @FXML
    private void editEmployee (ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        setBackground(true);
        edit_pane.setVisible(true);
        //GUIRunner.changeScene("employee_edit");
    }

    
    @FXML
    private void fill_employee (ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(employee_dropdown.getValue() != null){//if user has chosen an employee
            Employee e = EmployeeDB.find_edit_Employee(employee_dropdown.getValue());
            edit_email.setText(e.getEmployeeEmail());
            if(e.getEmployeeStatus()){
                status_dropdown.setValue("Active");
            }
            else{
                status_dropdown.setValue("Inactive");
            }
            role_text.setText(e.getEmployeeRole());
            login_text.setText("" + e.getEmployeeLogin());
            eid_text.setText("" + e.getEmployeeID());

        }
    }

    @FXML
    private void Cancel_button(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        setBackground(false);
        edit_pane.setVisible(false);
        searchEmployees(null);
        //GUIRunner.changeScene("employees");
    }


    @FXML
    private void Apply_button(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        //edit_email.setText(e.getEmployeeEmail());
        Boolean status = false;
        if(status_dropdown.getValue() == "Active"){
            status = true;
        }
        Employee e = EmployeeDB.find_edit_Employee(employee_dropdown.getValue());
        EmployeeDB.updateEmployeeValues(e.getEmployeeID(), edit_email.getText(), status);
        setBackground(false);
        edit_pane.setVisible(false);
        searchEmployees(null);
        //GUIRunner.changeScene("employees");
    }
    // @FXML
    // private void edit_button_click (ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
    //     GUIRunner.changeScene("employee_edit");
    // }

    // @FXML
    // private void updateEmployeeEmail (ActionEvent actionEvent) throws
    // SQLException, ClassNotFoundException {
    // try {
    // EmployeeDB.updateEmployeeEmail(name_col.getText(),email_col.getText());
    // } catch (SQLException error) {
    // throw error;
    // }
    // } //updates an employee email
    // Insert employee to database
    // @FXML
    // private void insertEmployee (ActionEvent actionEvent) throws SQLException,
    // ClassNotFoundException {
    // try {
    // EmployeeDB.insertEmployee(employee_id_col ,name_col ,email_col ,
    // role_col.getText() , Last_login_col , active_col);
    // } catch (SQLException error) {
    // throw error;
    // }
    // }

    @FXML
    private void changeToCashier(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        GUIRunner.changeScene("cashier1");
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
    private void logOut(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
        Scene login = new Scene(FXMLLoader.load(getClass().getResource("/fxml/login.fxml")));
        System.out.println("Logged Out");
        GUIRunner.stage.setScene(login);
        GUIRunner.stage.show();
    }
}