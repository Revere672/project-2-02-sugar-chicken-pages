import java.io.IOException;
import java.sql.SQLException;

/**
 * Main entry point for the application.
 * This class is used to run the GUIRunner class.
 * This class is needed in order to run the jar file built with gradle
 * 
 * @author Joshua George
 */
public class MainEntry {
    /**
     * The main entry point for the application.
     * This method launches the GUIRunner application.
     *
     * @param args the command line arguments.
     * @throws ClassNotFoundException if the JDBC driver class is not found.
     * @throws SQLException           if a database access error occurs.
     * @throws IOException            if an I/O error occurs.
     */
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        GUIRunner.main(args);
    }
}