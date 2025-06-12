package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;

public class DistributeController1 {

    @FXML private Button droughtButton;
    @FXML private Button earthquakeButton;
    @FXML private Button epidemicButton;
    @FXML private Button fireButton;
    @FXML private Button landslideButton;
    @FXML private Button returnToHomePageButton;
    @FXML private Button typhoonandfloodButton;
    @FXML private Button volcanicButton;

    private static final String THEME_COLOR = "#001F54";
    private static final String BORDER_STYLE = "-fx-background-color: transparent; -fx-border-color: " + THEME_COLOR + 
                                            "; -fx-border-width: 2.5; -fx-border-radius: 10;";
    private static final String HOVER_BORDER_STYLE = "-fx-background-color: transparent; -fx-border-color: " + THEME_COLOR + 
                                                "; -fx-border-width: 3.5; -fx-border-radius: 10;";

    @FXML
    public void initialize() {
        initializeButtons();
    }

    private void initializeButtons() {
        Button[] buttons = {
            droughtButton, epidemicButton, fireButton, landslideButton,
            earthquakeButton, typhoonandfloodButton, volcanicButton
        };
        
        for (Button button : buttons) {
            if (button != null) {
                addGlowEffect(button);
            }
        }
    }

    private void addGlowEffect(Button button) {
        // Create enhanced border glow effect
        DropShadow borderGlow = new DropShadow();
        borderGlow.setColor(Color.web(THEME_COLOR));
        borderGlow.setOffsetX(0);
        borderGlow.setOffsetY(0);
        borderGlow.setRadius(15);
        borderGlow.setSpread(0.4);
        
        // Set initial state with stronger glow
        button.setStyle(BORDER_STYLE);
        button.setEffect(new DropShadow(8, Color.web(THEME_COLOR)));

        // Enhanced hover effects
        button.setOnMouseEntered(e -> {
            borderGlow.setRadius(25);
            borderGlow.setSpread(0.6);
            button.setEffect(borderGlow);
            button.setStyle(HOVER_BORDER_STYLE);
        });

        button.setOnMouseExited(e -> {
            button.setEffect(new DropShadow(8, Color.web(THEME_COLOR)));
            button.setStyle(BORDER_STYLE);
        });

        button.setOnMousePressed(e -> {
            borderGlow.setRadius(30);
            borderGlow.setSpread(0.8);
            button.setEffect(borderGlow);
        });

        button.setOnMouseReleased(e -> {
            borderGlow.setRadius(25);
            button.setEffect(new DropShadow(8, Color.web(THEME_COLOR)));
        });
    }

    private void navigateToDistributePage2(ActionEvent event, String disasterType) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DistributePage2.fxml"));
            Parent root = loader.load();
            
            // Get the controller and set the disaster type
            DistributeController2 controller = loader.getController();
            controller.setDisasterType(disasterType);
            
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void droughtButtonHandler(ActionEvent event) {
        navigateToDistributePage2(event, "DROUGHT");
    }

    @FXML
    private void earthquakeButtonHandler(ActionEvent event) {
        navigateToDistributePage2(event, "EARTHQUAKE");
    }

    @FXML
    private void epidemicButtonHandler(ActionEvent event) {
        navigateToDistributePage2(event, "EPIDEMIC / PANDEMIC");
    }

    @FXML
    private void fireButtonHandler(ActionEvent event) {
        navigateToDistributePage2(event, "FIRE");
    }

    @FXML
    private void landslideButtonHandler(ActionEvent event) {
        navigateToDistributePage2(event, "LANDSLIDE");
    }

    @FXML
    private void typhoonandfloodButtonHandler(ActionEvent event) {
        navigateToDistributePage2(event, "TYPHOON / FLOOD");
    }

    @FXML
    private void volcanicButtonHandler(ActionEvent event) {
        navigateToDistributePage2(event, "VOLCANIC ERUPTION");
    }

    @FXML
    private void returnToHomePageButtonHandler(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/HomePage.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}