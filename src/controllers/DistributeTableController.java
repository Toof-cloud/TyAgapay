package controllers;

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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DistributeTableController implements Initializable {

    @FXML
    private Button goToHistoryButton;
    @FXML
    private Button goToInventoryButton;
    @FXML
    private Button returnToHomePageButton;
    @FXML
    private TextField searchTextField;

     @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialization code
        addGlowEffect(goToHistoryButton);
        addGlowEffect(goToInventoryButton);
        addGlowEffect(returnToHomePageButton);
    }
    // Other @FXML fields and methods
    @FXML
    private void goToInventoryButtonHandler(ActionEvent event) throws IOException {
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
