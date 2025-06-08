package controllers;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Admin;
import model.AdminSession;
import util.DatabaseHandler;

public class AdminProfileController implements Initializable{
    @FXML
    private TextField contactField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField gmailField;

    @FXML
    private TextField lastNameField;

    @FXML
    private ComboBox<String> officialTypeBox;

    @FXML
    private TextField passwordField;

    @FXML
    private Button returnToHomePageButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label displayAdminID;

    @FXML
    private Label displayLocation;

    private Admin originalAdmin;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        officialTypeBox.getItems().addAll(
            "Baranggay Captain",
            "Baranggay Kagawad",
            "Baranggay Secretary",
            "Baranggay Treasurer",
            "SK Chairperson"
        );

        originalAdmin = AdminSession.getInstance().getAdmin();
        if (originalAdmin != null) {
            loadAdminData(originalAdmin);
        }
        addGlowEffect(returnToHomePageButton);
        addGlowEffect(saveButton);
        addGlowEffect(cancelButton);
    }

    private void loadAdminData(Admin admin) {
        displayAdminID.setText(admin.getAdminID());
        displayLocation.setText(admin.getBaranggay());
        firstNameField.setText(admin.getAdminfirstname());
        lastNameField.setText(admin.getAdminlastname());
        contactField.setText(admin.getContact());
        gmailField.setText(admin.getGmail());
        passwordField.setText(admin.getAdminpassword());
        officialTypeBox.setValue(admin.getOfficial_type());
    }
    
    @FXML
    void returnToHomePageButtonHandler(ActionEvent event) {
       try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/HomePage.fxml"));
        Parent root = loader.load();

        // Get the current stage from the event source (the button)
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    @FXML
    void cancelButtonHandler(ActionEvent event) {
    if (originalAdmin == null) return;

        boolean changed = 
            !firstNameField.getText().trim().equals(originalAdmin.getAdminfirstname()) ||
            !lastNameField.getText().trim().equals(originalAdmin.getAdminlastname()) ||
            !contactField.getText().trim().equals(originalAdmin.getContact()) ||
            !gmailField.getText().trim().equals(originalAdmin.getGmail()) ||
            !passwordField.getText().trim().equals(originalAdmin.getAdminpassword()) ||
            !officialTypeBox.getValue().equals(originalAdmin.getOfficial_type());

    if (changed) {
        showInfo("Changes have been canceled. All fields reverted.");
        loadAdminData(originalAdmin);
        System.out.println("Changes reverted.");
    } else {
        showInfo("No changes to cancel.");
    }
}
    @FXML
void saveButtonHandler(ActionEvent event) {
    Admin admin = AdminSession.getInstance().getAdmin();
    if (admin == null) return;

    String firstName = firstNameField.getText().trim();
    String lastName = lastNameField.getText().trim();
    String contact = contactField.getText().trim();
    String gmail = gmailField.getText().trim();
    String password = passwordField.getText().trim();
    String officialType = officialTypeBox.getValue();

    if (firstName.isEmpty() || lastName.isEmpty() || contact.isEmpty() ||
        gmail.isEmpty() || password.isEmpty() || officialType == null) {
        showInfo("Please fill out all fields.");
        return;
    }

    boolean confirm = showConfirmation("Are you sure you want to save these changes?");
    if (!confirm) return;

    Admin updatedAdmin = new Admin(
        admin.getAdminID(),
        firstName,
        lastName,
        officialType,
        password,
        contact,
        gmail,
        admin.getCreatedAt()
    );

    boolean success = DatabaseHandler.editAdmin(updatedAdmin);

    if (success) {
        AdminSession.getInstance().setAdmin(updatedAdmin);
        originalAdmin = updatedAdmin;
        showInfo("Admin profile updated successfully!");
    } else {
        showInfo("Failed to update admin profile.");
    }
}
    private boolean showConfirmation(String message) {
    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmation");
    alert.setHeaderText(null);
    alert.setContentText(message);
    java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
    return result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK;
}

    private void showInfo(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
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