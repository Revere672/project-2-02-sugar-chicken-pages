import java.sql.*;
import javafx.beans.property.*;

/**
 * This class constructs the basic properties of an employee at a restraunt, also has getters and setters for an employee and their properties
 * @author Alexey Bobkov
 */

public class Employee {
    private IntegerProperty employee_ID;
    private StringProperty name;
    private StringProperty email;
    private StringProperty role;
    private SimpleObjectProperty<Date> login_date;
    private BooleanProperty active_status;
    //edit column

    /**
     * Constructs employee with an ID, name, email, role, last login date, and active status
     */
    public Employee(){
        this.employee_ID = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.role = new SimpleStringProperty();
        this.login_date = new SimpleObjectProperty();
        this.active_status = new SimpleBooleanProperty();
    }
    /**
     * Gets an employee ID
     * @return The employee ID
     */
    public int getEmployeeID(){
        return employee_ID.get();
    }
    /**
     * Sets an employee ID
     * @param employee_ID The ID of the employee
     */
    public void setEmployeeID(int employee_ID){
        this.employee_ID.set(employee_ID);
    }
    /**
     * Gets an employee ID property
     * @return The employee ID property
     */
    public IntegerProperty EmployeeIDProperty(){
        return employee_ID;
    }
    /**
     * Gets an employee name
     * @return The employee name
     */
    public String getEmployeeName(){
        return name.get();
    }
     /**
     * Sets an employee name
     * @param name The name of the employee
     */
    public void setEmployeeName(String name){
        this.name.set(name);
    }
    /**
     * Gets an employee name property
     * @return The employee name property
     */
    public StringProperty EmployeeNameProperty(){
        return name;
    }
    /**
     * Gets an employee email
     * @return The employee email
     */
    public String getEmployeeEmail(){
        return email.get();
    }
     /**
     * Sets an employee email
     * @param email The email of the employee
     */
    public void setEmployeeEmail(String email){
        this.email.set(email);
    }
    /**
     * Gets an employee email property
     * @return The employee email property
     */
    public StringProperty EmployeeEmailProperty(){
        return email;
    }
    /**
     * Gets an employee role
     * @return The employee role
     */
    public String getEmployeeRole(){
        return role.get();
    }
     /**
     * Sets an employee role
     * @param role The role of the employee
     */
    public void setEmployeeRole(String role){
        this.role.set(role);
    }
    /**
     * Gets an employee role property
     * @return The employee role property
     */
    public StringProperty EmployeeRoleProperty(){
        return role;
    }
    /**
     * Gets an employee last login date
     * @return The employee last login
     */
    public Object getEmployeeLogin(){
        return login_date.get();
    }
     /**
     * Sets an employee last login date
     * @param login_date The last login date of the employee
     */
    public void setEmployeeLogin(Date login_date){
        this.login_date.set(login_date);
    }
    /**
     * Gets an employee login property
     * @return The employee login property
     */
    public ObjectProperty EmployeeLoginProperty(){
        return login_date;
    }
    /**
     * Gets an employee status
     * @return The employee status
     */
    public Boolean getEmployeeStatus(){
        return active_status.get();
    }
     /**
     * Sets an employee status
     * @param active_status The status of the employee
     */
    public void setEmployeeStatus(Boolean active_status){
        this.active_status.set(active_status);
    }
    /**
     * Gets an employee status property
     * @return The employee status property
     */
    public BooleanProperty EmployeeStatusProperty(){
        return active_status;
    }

}
