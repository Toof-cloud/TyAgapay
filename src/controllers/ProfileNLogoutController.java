package controllers;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.AdminSession;

public class ProfileNLogoutController{

    @FXML
    private Button logoutButton;

    @FXML
    private Button profileButton;

    private Stage homePageStage;

    public void setHomePageStage(Stage stage) {
    this.homePageStage = stage;
}

    @FXML
    public void initialize() {
        // Initialize the buttons with a glow effect
        addGlowEffect(logoutButton);
        addGlowEffect(profileButton);
    }

    @FXML
    private void profileButtonHandler(ActionEvent event) throws IOException {
       System.out.println("Profile button clicked");
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminProfilePage.fxml"));
        Parent root = loader.load();

        Stage profileStage = new Stage();
        profileStage.setTitle("Admin Profile");
        profileStage.setScene(new Scene(root));
        profileStage.setResizable(false);
        profileStage.show();

        // Close the small profileNLogout window
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.close();

        // Close the homepage window
        if (homePageStage != null) {
            homePageStage.close();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    @FXML
    private void logoutButtonHandler(ActionEvent event) throws IOException {
    // Show confirmation dialog
    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
    alert.setTitle("Logout Confirmation");
    alert.setHeaderText(null);
    alert.setContentText("Are you sure you want to log out?");
    java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();

    if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
        // Clear the session
        AdminSession.getInstance().setAdmin(null);

        // Load the login page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AgapayLoginPage.fxml"));
        Parent root = loader.load();

        // Show the login page in a new stage
        Stage loginStage = new Stage();
        loginStage.setTitle("Login");
        loginStage.setScene(new Scene(root));
        loginStage.setResizable(false);
        loginStage.show();

        // Close the small profileNLogout window
        Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        currentStage.close();

        // Close the homepage window
        if (homePageStage != null) {
            homePageStage.close();
        }
    }
}

    private void addGlowEffect(Button button) {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#010a5b")); // Match your theme
        glow.setRadius(10);
        glow.setSpread(0.4);

        button.setOnMouseEntered((MouseEvent e) -> button.setEffect(glow));
        button.setOnMouseExited((MouseEvent e) -> button.setEffect(null));
    }
}
