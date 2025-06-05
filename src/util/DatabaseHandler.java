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

    public static Admin validateLogin(String username, String password) {
    String query = "SELECT * FROM admin_officials WHERE ((BINARY gmail = ?) OR (BINARY contact = ?)) AND adminpassword = ?";
    try (Connection conn = getDBConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, username);
        stmt.setString(2, username);
        stmt.setString(3, password);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return new Admin(
                rs.getString("admin_id"),
                rs.getString("adminfirstname"),
                rs.getString("adminlastname"),
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
            String query = "SELECT * FROM admin_officials";
            result = getInstance().execQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    public static boolean addAdmin(Admin admin) {
    String query = "INSERT INTO admin_officials (adminfirstname, adminlastname, official_type, adminpassword, contact, gmail) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection conn = getDBConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, admin.getAdminfirstname());
        stmt.setString(2, admin.getAdminlastname());
        stmt.setString(3, admin.getOfficial_type());
        stmt.setString(4, admin.getAdminpassword());
        stmt.setString(5, admin.getContact());
        stmt.setString(6, admin.getGmail());
        int rows = stmt.executeUpdate();
        return rows > 0;
    } catch (SQLIntegrityConstraintViolationException e) {
        // Duplicate entry for contact or gmail
        System.err.println("Duplicate contact or gmail.");
        // Optionally, show a user-friendly message in your UI
    } catch (SQLException e) {
        // Enum or other SQL error
        System.err.println("SQL Error: " + e.getMessage());
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

    public static boolean editAdmin(Admin admin) {
        getInstance();
        String query = "UPDATE admin_officials SET adminfirstname = ?, adminlastname = ?, official_type = ?, adminpassword = ?, contact = ?, gmail = ? WHERE admin_id = ?";
        try (Connection conn = getDBConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, admin.getAdminfirstname());
            stmt.setString(2, admin.getAdminlastname());
            stmt.setString(3, admin.getOfficial_type());
            stmt.setString(4, admin.getAdminpassword());
            stmt.setString(5, admin.getContact());
            stmt.setString(6, admin.getGmail());
            stmt.setString(7, admin.getAdminID());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
}
