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

public class InventoryTableController implements Initializable {
    @FXML
    private Button goToDistributeButton;
    @FXML
    private Button goToRegisterButton;
    @FXML
    private Button goToHistoryButton;
    @FXML
    private Button returnToHomePageButton;
    @FXML
    private TextField searchTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization code
        addGlowEffect(goToDistributeButton);
        addGlowEffect(goToHistoryButton);
        addGlowEffect(returnToHomePageButton);
        addGlowEffect(goToRegisterButton);
    }
    
    @FXML
    private void goToDistributeButtonHandler(ActionEvent event) throws IOException {
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
    private void goToRegisterButtonHandler(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RegisterTablePage.fxml"));
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
    private void goToHistoryButtonHandler(ActionEvent event) throws IOException {
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
    private void returnToHomePageButtonHandler(ActionEvent event) throws IOException {
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
        glow.setColor(Color.web("#010a5b")); // You can match this with your theme
        glow.setRadius(10);
        glow.setSpread(0.4);

        button.setOnMouseEntered((MouseEvent e) -> button.setEffect(glow));
        button.setOnMouseExited((MouseEvent e) -> button.setEffect(null));
    }

}
