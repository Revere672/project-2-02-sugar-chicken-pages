import java.sql.*;
import javax.sql.rowset.*;

public class DBUtil {
    private static String PGUSER = System.getenv("PGUSER");
    private static String PGPASSWORD = System.getenv("PGPASSWORD");
    private static String PGHOST = System.getenv("PGHOST");
    private static String PGDATABASE = System.getenv("PGDATABASE");

    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static Connection conn = null;
    private static final String connStr = "jdbc:postgresql://" + PGHOST + "/" + PGDATABASE;

    public static void dbConnect() throws SQLException, ClassNotFoundException {
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
            dbConnect();
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

            dbDisconnect();
        }

        return crs;
    }

    public static void dbExecuteUpdate(String updateStmt) throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        try {
            dbConnect();
            stmt = conn.createStatement();
            stmt.executeUpdate(updateStmt);
        } catch (Exception e) {
            System.err.println(e);
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }

            dbDisconnect();
        }
    }
}
