package controllers;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

public class AddOfficialController implements Initializable{
    @FXML
    private Button addAdminButton;

    @FXML
    private TextField addContactField;

    @FXML
    private TextField addFirstNameField;

    @FXML
    private TextField addGmailField;

    @FXML
    private TextField addLastNameField;

    @FXML
    private TextField addPasswordField;

    @FXML
    private ComboBox<String> addOfficial_TypeBox;

    private AdminTableController adminTableController;
    public void setAdminTableController(AdminTableController controller) {
    this.adminTableController = controller;
}

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        addOfficial_TypeBox.getItems().addAll(
            "Baranggay Captain",
            "Baranggay Kagawad",
            "Baranggay Secretary",
            "Baranggay Treasurer",
            "SK Chairperson"
        );
        // Initialize the button with a glow effect
        addGlowEffect(addAdminButton);
    }
    @FXML
    private void addAccountButtonHandler() throws IOException {
    String firstName = addFirstNameField.getText().trim();
    String lastName = addLastNameField.getText().trim();
    String officialType = addOfficial_TypeBox.getValue();
    String password = addPasswordField.getText().trim();
    String contact = addContactField.getText().trim();
    String gmail = addGmailField.getText().trim();

    // Validation: all fields must be filled
    if (firstName.isEmpty() || lastName.isEmpty() || officialType == null || password.isEmpty() || contact.isEmpty() || gmail.isEmpty()) {
        showAlert("All fields are required.");
        return;
    }

    // Validation: contact must start with 09 and be 11 digits
    if (!contact.matches("^09\\d{9}$")) {
        showAlert("Contact number must start with 09 and be 11 digits long.");
        return;
    }

    // Validation: gmail must end with @gmail.com
    if (!gmail.matches("^[A-Za-z0-9._%+-]+@gmail\\.com$")) {
        showAlert("Gmail must be a valid address ending with @gmail.com.");
        return;
    }

    // If all validations pass, create Admin and save to DB
    Admin admin = new Admin("",firstName, lastName, officialType, password, contact, gmail, "");
    boolean success = DatabaseHandler.addAdmin(admin);

    if (success) {
        showAlert("Account created successfully!");
        if (adminTableController != null) {
        adminTableController.refreshTable();
    }
        addAdminButton.getScene().getWindow().hide();
    } else {
        showAlert("Failed to create account. Please try again.");
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
