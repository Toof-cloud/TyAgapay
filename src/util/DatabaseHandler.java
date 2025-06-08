package util;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import model.Admin;
import model.Citizen;

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
    public static Map<String, String> getBarangayLocationInfo(String barangayName) {
    Map<String, String> info = new HashMap<>();
    String query = "SELECT b.name AS barangay_name, ci.name AS city_name, b.zip, r.name AS region_name, co.name AS country_name " +
                   "FROM barangays b " +
                   "JOIN cities ci ON b.city_id = ci.city_id " +
                   "JOIN regions r ON ci.region_id = r.region_id " +
                   "JOIN countries co ON r.country_id = co.country_id " +
                   "WHERE b.name = ?";
    try (Connection conn = getDBConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, barangayName);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                info.put("barangay_name", rs.getString("barangay_name"));
                info.put("city_name", rs.getString("city_name"));
                info.put("zip", rs.getString("zip"));
                info.put("region_name", rs.getString("region_name"));
                info.put("country_name", rs.getString("country_name"));
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return info;
    }
    public static boolean addCitizen(Citizen citizen) {
    getInstance();
    boolean success = false;

    // Get the logged-in barangay name
    model.Admin admin = model.AdminSession.getInstance().getAdmin();
    if (admin == null) return false;
    String barangayName = admin.getBaranggay();

    // Look up IDs for barangay, city, region, country
    String queryIds = "SELECT b.barangay_id, ci.city_id, r.region_id, co.country_id " +
                      "FROM barangays b " +
                      "JOIN cities ci ON b.city_id = ci.city_id " +
                      "JOIN regions r ON ci.region_id = r.region_id " +
                      "JOIN countries co ON r.country_id = co.country_id " +
                      "WHERE b.name = ?";
    try (Connection conn = getDBConnection();
         PreparedStatement stmtIds = conn.prepareStatement(queryIds)) {
        stmtIds.setString(1, barangayName);
        try (ResultSet rs = stmtIds.executeQuery()) {
            if (rs.next()) {
                int barangayId = rs.getInt("barangay_id");
                int cityId = rs.getInt("city_id");
                int regionId = rs.getInt("region_id");
                int countryId = rs.getInt("country_id");

                // Now insert the citizen
                String insert = "INSERT INTO citizens " +
                        "(citizenlastname, citizenfirstname, contactNum, marital_status, household_no, valid_id_type, valid_id_code, house_no, barangay_id, city_id, region_id, country_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmtInsert = conn.prepareStatement(insert)) {
                    stmtInsert.setString(1, citizen.getCitizenLastname());
                    stmtInsert.setString(2, citizen.getCitizenFirstname());
                    stmtInsert.setString(3, citizen.getContactNum());
                    stmtInsert.setString(4, citizen.getMaritalStatus());
                    stmtInsert.setString(5, citizen.getHouseholdNo());
                    stmtInsert.setString(6, citizen.getValidIdType());
                    stmtInsert.setString(7, citizen.getValidIdCode());
                    stmtInsert.setString(8, citizen.getHouseNo());
                    stmtInsert.setInt(9, barangayId);
                    stmtInsert.setInt(10, cityId);
                    stmtInsert.setInt(11, regionId);
                    stmtInsert.setInt(12, countryId);

                    int rows = stmtInsert.executeUpdate();
                    success = rows > 0;
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return success;
}
    public static boolean deleteCitizen(String citizenId) {
    getInstance();
    String query = "DELETE FROM citizens WHERE citizen_id = ?";
    try (Connection conn = getDBConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, citizenId);
        int rows = stmt.executeUpdate();
        return rows > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
    public static ResultSet getValidIdTypes() {
    ResultSet result = null;
    String query = "SELECT name FROM valid_ids";
    try {
        Connection conn = getDBConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        result = stmt.executeQuery();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return result;
    }
}