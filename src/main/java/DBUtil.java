import java.sql.*;
import javax.sql.rowset.*;

public class DBUtil {

    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static Connection conn = null;

    public static void dbConnect(String PGHOST,String PGDATABASE,String PGUSER,String PGPASSWORD)throws SQLException, ClassNotFoundException {
        String connStr = "jdbc:postgresql://" + PGHOST + ":5432/" + PGDATABASE;
        System.out.println(connStr);
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(connStr, PGUSER, PGPASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            throw e;
        }

        System.out.println("Connected to database successfully!");
    }

    public static void dbDisconnect() throws SQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSet crs = null;

        try {
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(queryStmt);

            crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.populate(resultSet);
        } catch (Exception e) {
            System.err.println(e);
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }

            if (stmt != null) {
                stmt.close();
            }
        }

        return crs;
    }

    public static void dbExecuteUpdate(String updateStmt) throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(updateStmt);
        } catch (Exception e) {
            System.err.println(e);
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }
}