package controllers;


import java.net.URL;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Admin;
import util.DatabaseHandler;

public class DistributeController3 implements Initializable {

    // FXML Labels for citizen and disaster info
    @FXML private Label fullNameLabel;
    @FXML private Label citizenIDlabel;
    @FXML private Label householdnumlabel;
    @FXML private Label disasterIDlabel;
    @FXML private Label distributionIDlabel;
    @FXML private Label barangaylabel;

    // FXML Labels for relief goods
    @FXML private Label ricelabel;
    @FXML private Label cansardineslabel;
    @FXML private Label cantunalabel;
    @FXML private Label canbeeflabel;
    @FXML private Label noodleslabel;
    @FXML private Label biscuitslabel;
    @FXML private Label bottledwaterlabel;
    @FXML private Label barsoaplabel;
    @FXML private Label toothpastelabel;
    @FXML private Label shampoolabel;
    @FXML private Label napkinlabel;
    @FXML private Label milklabel;

    @FXML private Button confirmButton;
    @FXML private Button backButton;

    private String currentDisasterType;
    private Admin admin;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        admin = model.AdminSession.getInstance().getAdmin();
        Platform.runLater(() -> {
            testConnection();
            setupBarangayLabel();
            loadItemQuantities(); // Always show stocks after barangay is set
        });
    }

    // Set barangay label from admin session
    private void setupBarangayLabel() {
        if (admin != null) {
            barangaylabel.setText(admin.getBaranggay());
            System.out.println("[DEBUG] Barangay label set to: " + admin.getBaranggay());
        } else {
            System.out.println("[DEBUG] Admin is null in setupBarangayLabel");
        }
    }

    // Test database connection (for debugging)
    private void testConnection() {
        try {
            DatabaseHandler db = DatabaseHandler.getInstance();
            if (db != null) {
                System.out.println("[DEBUG] Database connection successful");
            }
        } catch (Exception e) {
            System.err.println("[DEBUG] Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Called by previous controller to set citizen/disaster info
    public void setDistributionData(String citizenId, String fullName, String household, String disasterId, String disasterType) {
        System.out.printf("[DEBUG] setDistributionData called with: %s, %s, %s, %s, %s%n",
                citizenId, fullName, household, disasterId, disasterType);
        fullNameLabel.setText(fullName);
        citizenIDlabel.setText(citizenId);
        householdnumlabel.setText(household);
        disasterIDlabel.setText(disasterId);
        currentDisasterType = disasterType;
        loadItemQuantities();
        System.out.println("[DEBUG] Labels set in setDistributionData");
    }

    private void loadItemQuantities() {
    try {
        System.out.println("[DEBUG] loadItemQuantities called");
        String barangayName = barangaylabel.getText();
        if (barangayName == null || barangayName.isEmpty()) {
            System.out.println("[DEBUG] Barangay label is empty in loadItemQuantities");
            return;
        }
        int disasterId = Integer.parseInt(disasterIDlabel.getText());
        int householdSize = Integer.parseInt(householdnumlabel.getText());

        // Get per-person bundle for this disaster
        Map<String, Integer> bundle = DatabaseHandler.getInstance().getDisasterBundle(disasterId);

        // Get available stocks
        ResultSet rs = DatabaseHandler.getItemStockForBarangay(barangayName);
        Map<String, Integer> stocks = new HashMap<>();
        while (rs.next()) {
            stocks.put(rs.getString("item_name"), rs.getInt("quantity"));
        }

        // For each item, show needed/available
        ricelabel.setText(bundle.getOrDefault("Rice", 0) * householdSize + "/" + stocks.getOrDefault("Rice", 0));
        cansardineslabel.setText(bundle.getOrDefault("Canned Sardines", 0) * householdSize + "/" + stocks.getOrDefault("Canned Sardines", 0));
        cantunalabel.setText(bundle.getOrDefault("Canned Tuna", 0) * householdSize + "/" + stocks.getOrDefault("Canned Tuna", 0));
        canbeeflabel.setText(bundle.getOrDefault("Canned Beef", 0) * householdSize + "/" + stocks.getOrDefault("Canned Beef", 0));
        noodleslabel.setText(bundle.getOrDefault("Noodles", 0) * householdSize + "/" + stocks.getOrDefault("Noodles", 0));
        biscuitslabel.setText(bundle.getOrDefault("Biscuits", 0) * householdSize + "/" + stocks.getOrDefault("Biscuits", 0));
        bottledwaterlabel.setText(bundle.getOrDefault("Bottle Water", 0) * householdSize + "/" + stocks.getOrDefault("Bottle Water", 0));
        barsoaplabel.setText(bundle.getOrDefault("Bar Soap", 0) * householdSize + "/" + stocks.getOrDefault("Bar Soap", 0));
        toothpastelabel.setText(bundle.getOrDefault("Toothpaste", 0) * householdSize + "/" + stocks.getOrDefault("Toothpaste", 0));
        shampoolabel.setText(bundle.getOrDefault("Shampoo", 0) * householdSize + "/" + stocks.getOrDefault("Shampoo", 0));
        napkinlabel.setText(bundle.getOrDefault("Napkin", 0) * householdSize + "/" + stocks.getOrDefault("Napkin", 0));
        milklabel.setText(bundle.getOrDefault("Powdered Milk", 0) * householdSize + "/" + stocks.getOrDefault("Powdered Milk", 0));

        System.out.println("[DEBUG] Relief goods labels updated with needed/available.");
    } catch (Exception e) {
        e.printStackTrace();
        showError("Error", "Failed to load quantities: " + e.getMessage());
    }
}

    // Show summary alert on confirm
    @FXML
    private void handleConfirmButton() {
    try {
        int citizenId = Integer.parseInt(citizenIDlabel.getText());
        int disasterId = Integer.parseInt(disasterIDlabel.getText());
        String barangayName = barangaylabel.getText();
        int barangayId = DatabaseHandler.getInstance().getBarangayIdByName(barangayName);

        Map<String, Integer> bundle = DatabaseHandler.getInstance().getDisasterBundle(disasterId);
        int householdSize = Integer.parseInt(householdnumlabel.getText());

        // Get available stocks
        ResultSet rs = DatabaseHandler.getItemStockForBarangay(barangayName);
        Map<String, Integer> stocks = new HashMap<>();
        while (rs.next()) {
            stocks.put(rs.getString("item_name"), rs.getInt("quantity"));
        }

        // Check for insufficient stock
        StringBuilder insufficient = new StringBuilder();
        for (Map.Entry<String, Integer> entry : bundle.entrySet()) {
            String itemName = entry.getKey();
            int needed = entry.getValue() * householdSize;
            int available = stocks.getOrDefault(itemName, 0);
            if (available < needed) {
                insufficient.append(itemName)
                            .append(" (needed: ").append(needed)
                            .append(", available: ").append(available)
                            .append(")\n");
            }
        }

        if (insufficient.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Insufficient Stock");
            alert.setHeaderText("Some items are ubos na (out of stock) or not enough:");
            alert.setContentText(insufficient.toString());
            alert.showAndWait();
            return; // Stop distribution
        }

        // 1. Insert into distributions and get the new ID
        int distributionId = DatabaseHandler.getInstance().insertDistribution(citizenId, disasterId, barangayId);

        // 2.5 Mark citizen as distributed
        DatabaseHandler.getInstance().markCitizenAsDistributed(citizenId);

        // 2. Insert into distribution_items and decrement stock
        for (Map.Entry<String, Integer> entry : bundle.entrySet()) {
            String itemName = entry.getKey();
            int itemId = DatabaseHandler.getInstance().getItemIdByName(itemName);
            int quantity = entry.getValue() * householdSize;
            DatabaseHandler.getInstance().insertDistributionItem(distributionId, itemId, quantity);
            DatabaseHandler.getInstance().decrementItemStock(barangayId, itemId, quantity);
        }

        // 3. Show alert with distribution ID
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Distribution Complete");
        alert.setHeaderText("Distribution Successful!");
        alert.setContentText("Distribution ID: " + distributionId + "\n\n" +
            "You can view this transaction in the history table.");
        alert.showAndWait();
    } catch (Exception e) {
        showError("Distribution Error", e.getMessage());
    }
}
    

    // Show error alert
    private void showError(String header, String content) {
        System.err.printf("[DEBUG] showError called: %s - %s%n", header, content);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void backButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DistributePage2.fxml"));
            Parent root = loader.load();
            DistributeController2 controller = loader.getController();
            // Pass the disaster type back!
            controller.setDisasterType(currentDisasterType);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Could not load previous page");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}