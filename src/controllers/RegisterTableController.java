package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RegisterTableController implements Initializable {
    
    @FXML
    private Button goToDistributeButton;

    @FXML
    private Button goToHistoryButton;

    @FXML
    private Button goToInventoryButton;

    @FXML
    private Button returnToHomePageButton;

    @FXML
    private TextField searchTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the buttons with a glow effect
        addGlowEffect(goToDistributeButton);
        addGlowEffect(goToHistoryButton);
        addGlowEffect(goToInventoryButton);
        addGlowEffect(returnToHomePageButton);
    }

    @FXML
    void goToDistributeButtonHandler(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DistributeTablePage.fxml"));
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
    void goToInventoryButtonHandler(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/InventoryTablePage.fxml"));
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
    void goToHistoryButtonHandler(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/HistoryTablePage.fxml"));
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
    void returnToHomePageButtonHandler(ActionEvent event) throws IOException {
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
    private void addGlowEffect(Button button) {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#010a5b")); // Match your theme
        glow.setRadius(10);
        glow.setSpread(0.4);

        button.setOnMouseEntered((MouseEvent e) -> button.setEffect(glow));
        button.setOnMouseExited((MouseEvent e) -> button.setEffect(null));
    }
}