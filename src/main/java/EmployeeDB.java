import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
public class EmployeeDB {
    
    public static ObservableList<Employee> find_Employee(String name) throws SQLException, ClassNotFoundException {
        String selected_Employee = "SELECT * FROM Employees WHERE Employee_name LIKE '%" + name + "%';";

        try {
            ResultSet returned_Employee = DBUtil.dbExecuteQuery(selected_Employee);//Gets ResultSet of employee from table
            ObservableList<Employee> e = getEmployeeList(returned_Employee);//send the result to getRequestedEmployee function and return employee object
            //Return employee object
            return e;
        } catch (SQLException error) {
            System.out.println("While searching an employee with " + name + " name, an error occurred: " + error);
            throw error;//return the error that id dne
        }
    }

    private static Employee getRequestedEmployee(ResultSet result_e) throws SQLException{
        Employee e = null;
        if (result_e.next()) {
            e = new Employee();
            e.setEmployeeID(result_e.getInt("employee_ID"));
            e.setEmployeeName(result_e.getString("employee_name"));
            e.setEmployeeEmail(result_e.getString("employee_email"));
            e.setEmployeeRole(result_e.getString("employee_role"));
            e.setEmployeeLogin(result_e.getDate("last_login"));
            e.setEmployeeStatus(result_e.getBoolean("status"));
        }
        return e;
    }

    public static ObservableList<Employee> searchEmployees () throws SQLException, ClassNotFoundException {
        String selected_Employee = "SELECT * FROM Employees;";
        try {
            ResultSet returned_Employee = DBUtil.dbExecuteQuery(selected_Employee);//Get ResultSet of the returned employees
            ObservableList<Employee> e_list = getEmployeeList(returned_Employee);//Send ResultSet to the getEmployeeList function and return the employee object
            System.out.println(e_list.size());
            return e_list;
        } catch (SQLException error) {
            System.out.println("SQL select operation has been failed: " + error);
            throw error;//this select query does not exist so throw error
        }
    }

    private static ObservableList<Employee> getEmployeeList(ResultSet result_e) throws SQLException, ClassNotFoundException {
        ObservableList<Employee> eList = FXCollections.observableArrayList();//obseravble employee list(full of employee objects)
        while (result_e.next()) {
            Employee e = new Employee();//create a new employee
            e.setEmployeeID(result_e.getInt("employee_ID"));
            e.setEmployeeName(result_e.getString("employee_name"));
            e.setEmployeeEmail(result_e.getString("employee_email"));
            e.setEmployeeRole(result_e.getString("employee_role"));
            e.setEmployeeLogin(result_e.getDate("last_login"));
            e.setEmployeeStatus(result_e.getBoolean("active"));//set the employees values
            
            eList.add(e);//add employee e to the employee list
        }
        return eList;//return the elist as an observable list
    }

    public static void updateEmployeeEmail(String employee_ID, String employee_Email) throws SQLException, ClassNotFoundException {
        //Create new string with update email
        String new_q =
                "BEGIN\n" +
                        "   UPDATE Employees\n" +
                        "      SET employee_email = '" + employee_Email + "'\n" +
                        "    WHERE employee_ID = " + employee_ID + ";\n" +
                        "   COMMIT;\n" +
                        "END;";
        try {
            DBUtil.dbExecuteUpdate(new_q);//try to execute the update email function
        } catch (SQLException error) {
            System.out.print("Error occurred while UPDATE Operation: " + error);
            throw error;//throw error if update function doesn't correspond
        }
    }

    //make an updateEmployeeStatus thing

    public static void insertEmployee(int Employee_ID,String name, String email, String role, Date login , Boolean status) throws SQLException, ClassNotFoundException {
        //create a new querey that inserts a new employee with given attributes
        String new_q =
                "BEGIN\n" +
                        "INSERT INTO Employees\n" +
                        "(employee_ID, employee_name, employee_email, employee_role, last_login, status)\n" +
                        "VALUES\n" +
                        "('"+Employee_ID+"' ,'"+name+"','"+email+"','"+role+"' ,'"+login+"' ,'"+status+"');\n" +
                        "END;";
        //Execute DELETE operation
        try {
            DBUtil.dbExecuteUpdate(new_q);
        } catch (SQLException error) {
            System.out.print("Error occurred while DELETE Operation: " + error);
            throw error;
        }
    }
}