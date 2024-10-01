import java.sql.*;
import javafx.beans.property.*;

public class Employee {
    private IntegerProperty employee_ID;
    private StringProperty name;
    private StringProperty email;
    private StringProperty role;
    private SimpleObjectProperty<Date> login_date;
    private BooleanProperty active_status;
    //edit column


    public Employee(){
        this.employee_ID = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.role = new SimpleStringProperty();
        this.login_date = new SimpleObjectProperty();
        this.active_status = new SimpleBooleanProperty();
    }

    public int getEmployeeID(){
        return employee_ID.get();
    }

    public void setEmployeeID(int employee_ID){
        this.employee_ID.set(employee_ID);
    }

    public IntegerProperty EmployeeIDProperty(){
        return employee_ID;
    }

    public String getEmployeeName(){
        return name.get();
    }

    public void setEmployeeName(String name){
        this.name.set(name);
    }

    public StringProperty EmployeeNameProperty(){
        return name;
    }

    public String getEmployeeEmail(){
        return email.get();
    }

    public void setEmployeeEmail(String email){
        this.email.set(email);
    }

    public StringProperty EmployeeEmailProperty(){
        return email;
    }

    public String getEmployeeRole(){
        return role.get();
    }

    public void setEmployeeRole(String role){
        this.role.set(role);
    }

    public StringProperty EmployeeRoleProperty(){
        return role;
    }

    public Object getEmployeeLogin(){
        return login_date.get();
    }

    public void setEmployeeLogin(Date login_date){
        this.login_date.set(login_date);
    }

    public ObjectProperty EmployeeLoginProperty(){
        return login_date;
    }

    public Boolean getEmployeeStatus(){
        return active_status.get();
    }

    public void setEmployeeStatus(Boolean active_status){
        this.active_status.set(active_status);
    }

    public BooleanProperty EmployeeStatusProperty(){
        return active_status;
    }

}
