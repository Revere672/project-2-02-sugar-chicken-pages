import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.*;

public class EmployeeTable{
    @FXML
    private TextField search_bar;
    @FXML
    private TableView<Employee> employee_table;
    @FXML
    private TableColumn<Employee, Integer>  employee_id_col;
    @FXML
    private TableColumn<Employee, String>  name_col;
    @FXML
    private TableColumn<Employee, String> email_col;
    @FXML
    private TableColumn<Employee, String>  role_col;
    @FXML
    private TableColumn<Employee, Date>  Last_login_col;
    @FXML
    private TableColumn<Employee, Boolean>  active_col;

    @FXML
    private void searchEmployee(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {//searches when search button is hit
        try {
            ObservableList<Employee> e = EmployeeDB.find_Employee(search_bar.getText());//search bar will retreive an employee based off text
            populateEmployees(e);//populate with employee object e
        } catch (SQLException error) {
            error.printStackTrace();
            throw error;
        }
    }

    @FXML
    private void searchEmployees(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            ObservableList<Employee> employeeData = EmployeeDB.searchEmployees();//get employee data
            populateEmployees(employeeData);//populate table
        } catch (SQLException error){
            System.out.println("Error occurred while getting employees information from DB.\n" + error);
            throw error;
        }
    }

    @FXML
    private void initialize ()  throws SQLException, ClassNotFoundException{
        employee_id_col.setCellValueFactory(cellData -> cellData.getValue().EmployeeIDProperty().asObject());
        name_col.setCellValueFactory(cellData -> cellData.getValue().EmployeeNameProperty());
        email_col.setCellValueFactory(cellData -> cellData.getValue().EmployeeEmailProperty());
        role_col.setCellValueFactory(cellData->cellData.getValue().EmployeeRoleProperty());
        Last_login_col.setCellValueFactory(cellData->cellData.getValue().EmployeeLoginProperty());
        active_col.setCellValueFactory(cellData->cellData.getValue().EmployeeStatusProperty());
        searchEmployees(null);
    }

    @FXML
    private void populateEmployee (Employee e) throws ClassNotFoundException {
        ObservableList<Employee> employeeData = FXCollections.observableArrayList();//make a list for employee data thats visible
        employeeData.add(e);//add employee e
        employee_table.setItems(employeeData);//set
    }

    @FXML
    private void populateEmployees (ObservableList<Employee> employeeData) throws ClassNotFoundException {
        employee_table.setItems(employeeData);//set
    }

    // @FXML
    // private void updateEmployeeEmail (ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
    //     try {
    //         EmployeeDB.updateEmployeeEmail(name_col.getText(),email_col.getText());
    //     } catch (SQLException error) {
    //         throw error;
    //     }
    // } //updates an employee email
    //Insert employee to database
    // @FXML
    // private void insertEmployee (ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
    //     try {
    //         EmployeeDB.insertEmployee(employee_id_col ,name_col ,email_col , role_col.getText() , Last_login_col , active_col);
    //     } catch (SQLException error) {
    //         throw error;
    //     }
    // }

    
}