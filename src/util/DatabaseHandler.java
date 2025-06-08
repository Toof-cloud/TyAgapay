package util;
import java.sql.*;
import model.Admin;

public class DatabaseHandler {
    private static DatabaseHandler handler = null;
    private static Statement stmt = null;
    private static PreparedStatement pstatement = null;

    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    public static Connection getDBConnection()
    {
        Connection connection = null;
        String dburl = "jdbc:mysql://localhost:3306/agapaydb?serverTimezone=UTC";
        String userName = "root";
        String password = "password";

        try {
            connection = DriverManager.getConnection(dburl, userName, password);
            System.out.println("Database connected successfully.");
        } catch (Exception e){
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        }

        return connection;
    }

    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = getDBConnection().createStatement();
            result = stmt.executeQuery(query);
        }
        catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        }
        finally {
        }
        return result;
    }

    private static String loggedInBaranggay = null;

    public static void setLoggedInBaranggay(String baranggay) {
    loggedInBaranggay = baranggay;
    }
    public static String getLoggedInBaranggay() {
    return loggedInBaranggay;

    }
    public static Admin validateLogin(String username, String password) {
    String query = "SELECT ao.*, b.name AS barangay_name " +
                   "FROM admin_officials ao " +
                   "JOIN barangays b ON ao.barangay_id = b.barangay_id " +
                   "WHERE ((BINARY ao.gmail = ?) OR (BINARY ao.contact = ?)) AND ao.adminpassword = ?";
    try (Connection conn = getDBConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, username);
        stmt.setString(2, username);
        stmt.setString(3, password);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            // Save the barangay_id for filtering
            setLoggedInBaranggay(rs.getString("barangay_id"));
            return new Admin(
                rs.getString("admin_id"),
                rs.getString("adminfirstname"),
                rs.getString("adminlastname"),
                rs.getString("barangay_name"), // Use barangay name for display
                rs.getString("official_type"),
                rs.getString("adminpassword"),
                rs.getString("contact"),
                rs.getString("gmail"),
                rs.getString("created_at")
            );
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
    public static ResultSet getAdmins() {
    getInstance();
    ResultSet result = null;
    try {
        String query = "SELECT ao.*, b.name AS barangay_name " +
                       "FROM admin_officials ao " +
                       "JOIN barangays b ON ao.barangay_id = b.barangay_id " +
                       "WHERE ao.barangay_id = ?";
        Connection conn = getDBConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, getLoggedInBaranggay()); // This should be the barangay_id
        result = stmt.executeQuery();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return result;
}

    public static boolean deleteAdmin(String adminId) {
        getInstance();
        String query = "DELETE FROM admin_officials WHERE admin_id = ?";
        try (Connection conn = getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, adminId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //citizen
    public static ResultSet getCitizens() {
    getInstance();
    ResultSet result = null;
    try {
        String query = "SELECT c.*, b.name AS barangay_name, ci.name AS city_name, r.name AS region_name, co.name AS country_name " +
                       "FROM citizens c " +
                       "JOIN barangays b ON c.barangay_id = b.barangay_id " +
                       "JOIN cities ci ON c.city_id = ci.city_id " +
                       "JOIN regions r ON c.region_id = r.region_id " +
                       "JOIN countries co ON c.country_id = co.country_id " +
                       "WHERE c.barangay_id = ?";
        Connection conn = getDBConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, getLoggedInBaranggay()); // Only citizens from the logged-in official's barangay
        result = stmt.executeQuery();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return result;
}
}
