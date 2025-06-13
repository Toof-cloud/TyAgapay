package util;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import model.Admin;
import model.Citizen;
import model.Inventory;

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

    public static Connection getDBConnection() {
    Connection connection = null;
    // Add serverTimezone parameter to the URL
    String dburl = "jdbc:mysql://localhost:3306/Agapaydb?serverTimezone=Asia/Singapore";
    String userName = "root";
    String password = "password";

    try {
        // Load the JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        connection = DriverManager.getConnection(dburl, userName, password);
        System.out.println("Database connected successfully.");
    } catch (ClassNotFoundException e) {
        System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
        e.printStackTrace();
    } catch (SQLException e) {
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
               "WHERE c.barangay_id = ? AND (c.distributed = FALSE OR c.distributed = 0)";
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

    // inventory
    public static ResultSet getInventory() {
        getInstance();
        ResultSet result = null;
        try {
            String query = "SELECT " +
                    "i.inventory_id, " +
                    "i.distributor, " +
                    "CONCAT(a.adminfirstname, ' ', a.adminlastname) AS received_by, " +
                    "b.name AS barangay, " +
                    "ri.item_name, " +
                    "ii.quantity, " +
                    "i.inventory_date " +
                    "FROM inventory i " +
                    "JOIN admin_officials a ON i.received_by = a.admin_id " +
                    "JOIN barangays b ON i.barangay_id = b.barangay_id " +
                    "JOIN inventory_item ii ON i.inventory_id = ii.inventory_id " +
                    "JOIN relief_item ri ON ii.item_id = ri.item_id";
            Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            result = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static ResultSet getInventoryItemsForBarangay(String barangayName) {
    ResultSet result = null;
    try {
        String query = "SELECT ri.item_name, ii.quantity " +
                       "FROM inventory i " +
                       "JOIN inventory_item ii ON i.inventory_id = ii.inventory_id " +
                       "JOIN relief_item ri ON ii.item_id = ri.item_id " +
                       "JOIN barangays b ON i.barangay_id = b.barangay_id " +
                       "WHERE b.name = ?";
        Connection conn = getDBConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, barangayName);
        result = stmt.executeQuery();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return result;
}
    public static ResultSet getInventoryForBarangay(String barangayName) {
    ResultSet result = null;
    try {
        String query = "SELECT " +
                "i.inventory_id, " +
                "i.distributor, " +
                "CONCAT(a.adminfirstname, ' ', a.adminlastname) AS received_by, " +
                "b.name AS barangay, " +
                "ri.item_name, " +
                "ii.quantity, " +
                "i.inventory_date " +
                "FROM inventory i " +
                "JOIN admin_officials a ON i.received_by = a.admin_id " +
                "JOIN barangays b ON i.barangay_id = b.barangay_id " +
                "JOIN inventory_item ii ON i.inventory_id = ii.inventory_id " +
                "JOIN relief_item ri ON ii.item_id = ri.item_id " +
                "WHERE b.name = ?";
        Connection conn = getDBConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, barangayName);
        result = stmt.executeQuery();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return result;
}

    public static ResultSet getBarangayOfficials(String barangayName) {
    ResultSet result = null;
    try {
        String sql = "SELECT admin_id, adminfirstname, adminlastname FROM admin_officials ao " +
                     "JOIN barangays b ON ao.barangay_id = b.barangay_id WHERE b.name = ?";
        Connection conn = getDBConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, barangayName);
        result = stmt.executeQuery();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return result;
}
    /**
     * Adds a new inventory transaction, its items, and updates item_stock.
     * @param inventory The Inventory object (must have distributor, receivedBy, barangayId, inventoryDate)
     * @param itemQuantities Map of item_id to quantity for this transaction
     * @return true if successful, false otherwise
     */
    public static boolean addInventory(Inventory inventory, Map<Integer, Integer> itemQuantities) {
    Connection conn = null;
    PreparedStatement invStmt = null;
    PreparedStatement itemStmt = null;
    PreparedStatement stockStmt = null;
    ResultSet generatedKeys = null;
    boolean success = false;

    try {
        conn = getDBConnection();
        conn.setAutoCommit(false);

        // 1. Insert into inventory
        String invSql = "INSERT INTO inventory (distributor, received_by, barangay_id, inventory_date) VALUES (?, ?, ?, ?)";
        invStmt = conn.prepareStatement(invSql, Statement.RETURN_GENERATED_KEYS);
        invStmt.setString(1, inventory.getDistributor());
        invStmt.setInt(2, inventory.getReceivedById()); // Make sure your Inventory class has getReceivedBy()
        invStmt.setInt(3, inventory.getBarangayId());
        invStmt.setString(4, inventory.getInventoryDate());
        invStmt.executeUpdate();

        generatedKeys = invStmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            int inventoryId = generatedKeys.getInt(1);

            // 2. Insert inventory items and update stock
            String itemSql = "INSERT INTO inventory_item (inventory_id, item_id, quantity) VALUES (?, ?, ?)";
            itemStmt = conn.prepareStatement(itemSql);

            String stockSql = "INSERT INTO item_stock (barangay_id, item_id, quantity) VALUES (?, ?, ?) " +
                              "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";
            stockStmt = conn.prepareStatement(stockSql);

            for (Map.Entry<Integer, Integer> entry : itemQuantities.entrySet()) {
                int itemId = entry.getKey();
                int quantity = entry.getValue();

                // Insert inventory_item
                itemStmt.setInt(1, inventoryId);
                itemStmt.setInt(2, itemId);
                itemStmt.setInt(3, quantity);
                itemStmt.addBatch();

                // Update item_stock
                stockStmt.setInt(1, inventory.getBarangayId());
                stockStmt.setInt(2, itemId);
                stockStmt.setInt(3, quantity);
                stockStmt.addBatch();
            }
            itemStmt.executeBatch();
            stockStmt.executeBatch();
            conn.commit();
            success = true;
        } else {
            conn.rollback();
        }
    } catch (Exception e) {
        e.printStackTrace();
        try { if (conn != null) conn.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
    } finally {
        try { if (generatedKeys != null) generatedKeys.close(); } catch (Exception e) {}
        try { if (invStmt != null) invStmt.close(); } catch (Exception e) {}
        try { if (itemStmt != null) itemStmt.close(); } catch (Exception e) {}
        try { if (stockStmt != null) stockStmt.close(); } catch (Exception e) {}
        try { if (conn != null) conn.setAutoCommit(true); conn.close(); } catch (Exception e) {}
    }
    return success;
}
    public static int getAdminIdByName(String fullName) {
    int id = -1;
    try {
        String[] parts = fullName.trim().split(" ", 2); // Split into 2 parts only
        if (parts.length < 2) return -1;
        String sql = "SELECT admin_id FROM admin_officials WHERE adminfirstname = ? AND adminlastname = ?";
        Connection conn = getDBConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, parts[0]);
        stmt.setString(2, parts[1]);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) id = rs.getInt("admin_id");
        rs.close(); stmt.close(); conn.close();
    } catch (Exception e) { e.printStackTrace(); }
    return id;
}

    public static int getBarangayIdByName(String barangayName) {
        int id = -1;
        try {
            String sql = "SELECT barangay_id FROM barangays WHERE name = ?";
            Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, barangayName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) id = rs.getInt("barangay_id");
            rs.close(); stmt.close(); conn.close();
        } catch (Exception e) { e.printStackTrace(); }
        return id;
}

    public static int getItemIdByName(String itemName) {
        int id = -1;
        try {
            String sql = "SELECT item_id FROM relief_item WHERE item_name = ?";
            Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, itemName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) id = rs.getInt("item_id");
            rs.close(); stmt.close(); conn.close();
        } catch (Exception e) { e.printStackTrace(); }
        return id;
    }

    public static ResultSet getItemStockForBarangay(String barangayName) {
    ResultSet result = null;
    try {
        String query = "SELECT ri.item_name, s.quantity " +
                       "FROM item_stock s " +
                       "JOIN barangays b ON s.barangay_id = b.barangay_id " +
                       "JOIN relief_item ri ON s.item_id = ri.item_id " +
                       "WHERE b.name = ?";
        Connection conn = getDBConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, barangayName);
        result = stmt.executeQuery();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return result;
    }

    public Map<String, Integer> getBarangayItemStocks(String barangayName) {
        Map<String, Integer> stocks = new HashMap<>();
        String query = "SELECT ri.item_name, s.quantity " +
                    "FROM item_stock s " +
                    "JOIN relief_items ri ON s.item_id = ri.item_id " +
                    "JOIN barangays b ON s.barangay_id = b.barangay_id " +
                    "WHERE b.name = ?";
        try (
            Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setString(1, barangayName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    stocks.put(rs.getString("item_name"), rs.getInt("quantity"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stocks;
    }

    public Map<String, Integer> getDisasterBundle(int disasterId) {
        Map<String, Integer> bundle = new HashMap<>();
        String sql = "SELECT ri.item_name, drq.quantity_per_person " +
                    "FROM disaster_relief_quantities drq " +
                    "JOIN relief_items ri ON drq.item_id = ri.item_id " +
                    "WHERE drq.disaster_id = ?";
        try (
            Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, disasterId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bundle.put(rs.getString("item_name"), rs.getInt("quantity_per_person"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bundle;
    }

    public int insertDistribution(int citizenId, int disasterId, int barangayId) throws SQLException {
        String sql = "INSERT INTO distributions (citizen_id, disaster_id, barangay_id) VALUES (?, ?, ?)";
        try (Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, citizenId);
            stmt.setInt(2, disasterId);
            stmt.setInt(3, barangayId);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        throw new SQLException("Failed to insert distribution");
    }

    public void insertDistributionItem(int distributionId, int itemId, int quantity) throws SQLException {
        String sql = "INSERT INTO distribution_items (distribution_id, item_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, distributionId);
            stmt.setInt(2, itemId);
            stmt.setInt(3, quantity);
            stmt.executeUpdate();
        }
    }

    public void decrementItemStock(int barangayId, int itemId, int quantity) throws SQLException {
        String sql = "UPDATE item_stock SET quantity = quantity - ? WHERE barangay_id = ? AND item_id = ?";
        try (Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, barangayId);
            stmt.setInt(3, itemId);
            stmt.executeUpdate();
        }
    }

    public void markCitizenAsDistributed(int citizenId) {
        String sql = "UPDATE citizens SET distributed = TRUE WHERE citizen_id = ?";
        try (Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, citizenId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ResultSet getDistributedCitizens() {
        ResultSet result = null;
        try {
            String query = "SELECT c.*, b.name AS barangay_name, ci.name AS city_name, r.name AS region_name, co.name AS country_name " +
                "FROM citizens c " +
                "JOIN barangays b ON c.barangay_id = b.barangay_id " +
                "JOIN cities ci ON c.city_id = ci.city_id " +
                "JOIN regions r ON c.region_id = r.region_id " +
                "JOIN countries co ON r.country_id = co.country_id " +
                "WHERE (c.distributed = TRUE OR c.distributed = 1) AND c.barangay_id = ?";
            Connection conn = getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, getLoggedInBaranggay()); // This should be the barangay_id of the logged-in admin
            result = stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}