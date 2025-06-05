package controllers;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.Admin;
import util.DatabaseHandler;

import java.net.URL;
import java.util.ResourceBundle;



public class CreateAccountController implements Initializable {

    @FXML
    private ComboBox<String> bgryOfficialTypeComboBox;

    @FXML
    private TextField contactTextfield;

    @FXML
    private Button createAccountButton;

    @FXML
    private TextField firstNametextfield;

    @FXML
    private TextField gmailTextfield;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField passwordTextfield;

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        bgryOfficialTypeComboBox.getItems().addAll(
            "Baranggay Captain",
            "Baranggay Kagawad",
            "Baranggay Secretary",
            "Baranggay Treasurer",
            "SK Chairperson"
        );
        
        // Initialize the button with a glow effect
        addGlowEffect(createAccountButton);
    }
    @FXML
    private void createAccountButtonHandler() throws IOException {
    String firstName = firstNametextfield.getText().trim();
    String lastName = lastNameTextField.getText().trim();
    String officialType = bgryOfficialTypeComboBox.getValue();
    String password = passwordTextfield.getText().trim();
    String contact = contactTextfield.getText().trim();
    String gmail = gmailTextfield.getText().trim();

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
        // Optionally clear fields or close window
        createAccountButton.getScene().getWindow().hide();
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