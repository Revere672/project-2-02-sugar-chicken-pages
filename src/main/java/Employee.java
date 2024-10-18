import java.sql.*;
import javafx.beans.property.*;

/*
 * Class reponsible for viewing/updating employee's attributes in the database
 */
public class Employee {
    private IntegerProperty employee_ID;
    private StringProperty name;
    private StringProperty email;
    private StringProperty role;
    private SimpleObjectProperty<Date> login_date;
    private BooleanProperty active_status;
    //edit column

    /*
     * Default constructor for an employee that sets all of its 
     * attributes with JavaFX property bindings
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
     * Returns the current value of the employee ID
     * 
     * @return the employee ID as an integer
     */
    public int getEmployeeID(){
        return employee_ID.get();
    }

    /**
     * Updates the current value of the employee ID with a new one
     * 
     * @param employee_ID as the new employee ID
     */
    public void setEmployeeID(int employee_ID){
        this.employee_ID.set(employee_ID);
    }

    /**
     * Returns the employee ID property for data binding
     * 
     * @return the employee_ID property
     */
    public IntegerProperty EmployeeIDProperty(){
        return employee_ID;
    }

    /**
     * Returns the employee's current name
     * 
     * @return the employee's name as a string
     */
    public String getEmployeeName(){
        return name.get();
    }

    /**
     * Updates the employee's current name with a new one
     * 
     * @param name as the employee's new name
     */
    public void setEmployeeName(String name){
        this.name.set(name);
    }

    /**
     * Returns the name property for data binding
     * 
     * @return the name property
     */
    public StringProperty EmployeeNameProperty(){
        return name;
    }

    /**
     * Returns the employee's current email
     * 
     * @return the employee's email as a string
     */
    public String getEmployeeEmail(){
        return email.get();
    }

    /**
     * Updates the employee's current email with a new one
     * 
     * @param email as the employee's new email
     */
    public void setEmployeeEmail(String email){
        this.email.set(email);
    }

    /**
     * Returns the email property for data binding
     * 
     * @return the email property
     */
    public StringProperty EmployeeEmailProperty(){
        return email;
    }

    /**
     * Returns the employee's current role
     * 
     * @return the employee's role as a string
     */
    public String getEmployeeRole(){
        return role.get();
    }

    /**
     * Updates the employee's current role with a new one
     * 
     * @param role as the employee's new role
     */
    public void setEmployeeRole(String role){
        this.role.set(role);
    }

    /**
     * Returns the role property for data binding
     * 
     * @return the role property
     */
    public StringProperty EmployeeRoleProperty(){
        return role;
    }

    /**
     * Returns the employee's last login date
     * 
     * @return the last login date as an Object 
     */
    public Object getEmployeeLogin(){
        return login_date.get();
    }

    /**
     * Updates the employee's last login date with a new one
     * 
     * @param login_date as the employee's new login date
     */
    public void setEmployeeLogin(Date login_date){
        this.login_date.set(login_date);
    }

    /**
     * Returns the login_date property for data binding
     * 
     * @return the login_date property
     */
    public ObjectProperty EmployeeLoginProperty(){
        return login_date;
    }

    /**
     * Returns the employee's current active status
     * 
     * @return the employee's current active status as a boolean
     */
    public Boolean getEmployeeStatus(){
        return active_status.get();
    }

    /**
     * Updates the employee's current active status with a new one
     * 
     * @param active_status as the employee's new actice status
     */
    public void setEmployeeStatus(Boolean active_status){
        this.active_status.set(active_status);
    }

    /**
     * Returns the active_status property for data binding
     * 
     * @return the active_status property
     */
    public BooleanProperty EmployeeStatusProperty(){
        return active_status;
    }

}
