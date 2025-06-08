package controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Admin;
import model.AdminSession;
import util.DatabaseHandler;

import java.io.IOException;

import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class AgapayLoginController {

    @FXML
    private Button adminLoginButton;
    @FXML
    private Button showPasswordButton;
    @FXML
    private TextField adminUsernameTextField;
    @FXML
    private PasswordField adminPasswordTextField;
    @FXML
    private TextField adminPasswordVisibleTextField;

    private boolean isPasswordVisible = false;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Stage createAccountStage = null;

    @FXML
    private AnchorPane homeRoot;

    @FXML
    public void initialize() {
        //initialize the buttons with glow effects
        addGlowEffect(adminLoginButton);
 
        addGlowEffect(showPasswordButton);
       
        adminPasswordTextField.textProperty().addListener((obs, oldVal, newVal) -> {
        if (!isPasswordVisible) {
            adminPasswordVisibleTextField.setText(newVal);
        }
    });
        if (homeRoot != null) {
        homeRoot.setOnMouseClicked(e -> {
            if (createAccountStage != null && createAccountStage.isShowing()) {
                createAccountStage.close();
            }
        });
        }
    }

    @FXML
    private void togglePasswordVisibility(ActionEvent event) throws IOException{
    if (isPasswordVisible) {
        // Switch to PasswordField
        adminPasswordTextField.setText(adminPasswordVisibleTextField.getText());
        adminPasswordTextField.setVisible(true);
        adminPasswordTextField.setManaged(true);

        adminPasswordVisibleTextField.setVisible(false);
        adminPasswordVisibleTextField.setManaged(false);
    } else {
        // Switch to TextField
        adminPasswordVisibleTextField.setText(adminPasswordTextField.getText());
        adminPasswordVisibleTextField.setVisible(true);
        adminPasswordVisibleTextField.setManaged(true);

        adminPasswordTextField.setVisible(false);
        adminPasswordTextField.setManaged(false);
    }

    isPasswordVisible = !isPasswordVisible;
}
    public void loginButtonHandler(ActionEvent event) throws IOException{
        String username = adminUsernameTextField.getText().trim();
        String password = isPasswordVisible ? adminPasswordVisibleTextField.getText().trim() : adminPasswordTextField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Please enter both username and password.");
            return;
        }
        
        // Email and contact validation
        boolean isGmail = username.matches("^[A-Za-z0-9._%+-]+@gmail\\.com$");
        boolean isContact = username.matches("^09\\d{9}$");

        if (!isGmail && !isContact) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Username must be a valid Gmail address or a contact number starting with 09 and 11 digits long.");
            return;
        }
        Admin admin = DatabaseHandler.validateLogin(username, password);

        if (admin != null) {
        // Store the logged-in admin in the session
        AdminSession.getInstance().setAdmin(admin);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/HomePage.fxml"));
        Parent root = loader.load();

        HomePageController homeController = loader.getController();
        homeController.setAdminFirstName(admin.getAdminfirstname());

        stage = (Stage) adminLoginButton.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } else {
        showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect email/contact or password.");
}
}    //go to create account page
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    // This method adds a glow effect to the specified button when hovered over
    private void addGlowEffect(Button button) {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#010a5b")); // Change to any color you want
        glow.setRadius(10);
        glow.setSpread(0.4);

        button.setOnMouseEntered((MouseEvent e) -> button.setEffect(glow));
        button.setOnMouseExited((MouseEvent e) -> button.setEffect(null));
    }

}
