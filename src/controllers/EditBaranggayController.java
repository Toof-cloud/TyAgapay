package controllers;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.Admin;
import util.DatabaseHandler;



public class EditBaranggayController implements Initializable {
    
    @FXML
    private Button editAdminButton;

    @FXML
    private TextField editContactField;

    @FXML
    private TextField editFirstNameField;

    @FXML
    private TextField editGmailField;

    @FXML
    private TextField editLastNameField;

    @FXML
    private ComboBox<String> editOfficial_TypeBox;

    @FXML
    private TextField editPasswordField;

    private AdminTableController adminTableController;
    public void setAdminTableController(AdminTableController controller) {
        this.adminTableController = controller;
    }
    private String adminId; // To store the ID of the admin being edited
    private Admin originalAdmin; // Original admin data for comparison if needed
    public void setAdminData(Admin admin) {
    this.adminId = admin.getAdminID(); // Store the ID for editing
    this.originalAdmin = admin; 
    editFirstNameField.setText(admin.getAdminfirstname());
    editLastNameField.setText(admin.getAdminlastname());
    editOfficial_TypeBox.setValue(admin.getOfficial_type());
    editPasswordField.setText(admin.getAdminpassword());
    editContactField.setText(admin.getContact());
    editGmailField.setText(admin.getGmail());
}

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        editOfficial_TypeBox.getItems().addAll(
            "Baranggay Captain",
            "Baranggay Kagawad",
            "Baranggay Secretary",
            "Baranggay Treasurer",
            "SK Chairperson"
        );
        addGlowEffect(editAdminButton);
    }

    @FXML
    private void editAccountButtonHandler(ActionEvent event) {
    // Get updated values from fields
    String firstName = editFirstNameField.getText().trim();
    String lastName = editLastNameField.getText().trim();
    String officialType = editOfficial_TypeBox.getValue();
    String password = editPasswordField.getText().trim();
    String contact = editContactField.getText().trim();
    String gmail = editGmailField.getText().trim();

    // Validation...
    if (firstName.isEmpty() || lastName.isEmpty() || officialType == null || password.isEmpty() || contact.isEmpty() || gmail.isEmpty()) {
        showAlert("All fields are required.");
        return;
    }
    if (!contact.matches("^09\\d{9}$")) {
        showAlert("Contact number must start with 09 and be 11 digits long.");
        return;
    }
    if (!gmail.matches("^[A-Za-z0-9._%+-]+@gmail\\.com$")) {
        showAlert("Gmail must be a valid address ending with @gmail.com.");
        return;
    }
    if (
        firstName.equals(originalAdmin.getAdminfirstname()) &&
        lastName.equals(originalAdmin.getAdminlastname()) &&
        officialType.equals(originalAdmin.getOfficial_type()) &&
        password.equals(originalAdmin.getAdminpassword()) &&
        contact.equals(originalAdmin.getContact()) &&
        gmail.equals(originalAdmin.getGmail())
    ) {
        showAlert("No changes detected. Nothing to update.");
        return;
    }
    // Create updated Admin object
    Admin updatedAdmin = new Admin(adminId, firstName, lastName, officialType, password, contact, gmail, null);

    // Update in DB
    if (DatabaseHandler.editAdmin(updatedAdmin)) {
        showAlert("Account updated successfully!");
        if (adminTableController != null) {
        adminTableController.refreshTable();
    }
        editAdminButton.getScene().getWindow().hide();
        // Optionally refresh the table in the main window
    } else {
        showAlert("Failed to update account. Please try again.");
    }
}

    // Helper method to show alerts
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addGlowEffect(Button button) {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#00cfff")); // You can change to your theme color like "#010a5b"
        glow.setRadius(10);
        glow.setSpread(0.4);

        button.setOnMouseEntered((MouseEvent e) -> button.setEffect(glow));
        button.setOnMouseExited((MouseEvent e) -> button.setEffect(null));
    }
}
