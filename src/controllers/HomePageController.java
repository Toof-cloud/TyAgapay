package controllers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import model.Admin;
import model.AdminSession;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class HomePageController {

    @FXML
    private Label adminNameLabel;

    @FXML
    private Button distributeButton;

    @FXML
    private Button historyButton;

    @FXML
    private Button adminButton;

    @FXML
    private Button inventoryButton;

    @FXML
    private Button profileLogoutButton;

    @FXML
    private ImageView slideshowImageView;

    private Stage profileStage = null;

    @FXML
    private AnchorPane homeRoot;

    private final List<Image> images = new ArrayList<>();
    private int currentIndex = 0;

    public void setAdminFirstName(String firstName) {
        adminNameLabel.setText("Welcome, " + firstName + "!");
    }

    @FXML
    public void initialize() {
        // Set admin name from session
        Admin currentAdmin = AdminSession.getInstance().getAdmin();
        if (currentAdmin != null) {
            adminNameLabel.setText("Welcome, " + currentAdmin.getAdminfirstname() + "!");
        }

        // Load images into list
        images.add(new Image(getClass().getResource("/images/image1.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/images/image2.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/images/image3.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/images/image4.png").toExternalForm()));
        images.add(new Image(getClass().getResource("/images/image5.png").toExternalForm()));
        // Set the first image
        slideshowImageView.setImage(images.get(0));
        // Start the slideshow
        startSlideshow();

        //initialize buttons with glow effects
        addGlowEffect(adminButton);
        addGlowEffect(inventoryButton);
        addGlowEffect(distributeButton);
        addGlowEffect(historyButton);
        addGlowEffect(profileLogoutButton);

        if (homeRoot != null) {
        homeRoot.setOnMouseClicked(e -> {
            if (profileStage != null && profileStage.isShowing()) {
                profileStage.close();
            }
        });
    }
}
    //images transition effects
    private void startSlideshow() {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(3), e -> { 
                currentIndex = (currentIndex + 1) % images.size();
                
                // Set next image with fade-in effect
                slideshowImageView.setOpacity(0); // start transparent
                slideshowImageView.setImage(images.get(currentIndex));
                
                FadeTransition ft = new FadeTransition(Duration.seconds(3), slideshowImageView);
                ft.setFromValue(0);
                ft.setToValue(1);
                ft.play();
            })
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    
    @FXML
    public void goToDistributeTableHandler(ActionEvent event) throws IOException {
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
    public void goToInventoryTableHandler(ActionEvent event) throws IOException {
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
    public void goToHistoryTableHandler(ActionEvent event) throws IOException {
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
    public void goToAdminCrudHandler(ActionEvent event) throws IOException {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminTablePage.fxml"));
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
    public void checkProfileLogoutHandler(ActionEvent event) throws IOException {
    try {
        // If the profileStage already exists and is showing, bring it to front
        if (profileStage != null && profileStage.isShowing()) {
            profileStage.toFront();
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/profileNLogout.fxml"));
        Parent root = loader.load();

        profileStage = new Stage();
        profileStage.initStyle(javafx.stage.StageStyle.UNDECORATED);
        profileStage.setTitle("Profile");
        profileStage.setScene(new Scene(root));
        profileStage.setResizable(false);
        profileStage.setX(1123);
        profileStage.setY(95);
        profileStage.setOnHidden(e -> profileStage = null);

        // Pass the homepage stage to the profileNLogout controller
        ProfileNLogoutController controller = loader.getController();
        controller.setHomePageStage((Stage) ((Button) event.getSource()).getScene().getWindow());
        
        profileStage.show();
        profileStage.toFront();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    // Add glow effect to buttons
    private void addGlowEffect(Button button) {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#010a5b")); // You can match this with your theme
        glow.setRadius(10);
        glow.setSpread(0.4);

        button.setOnMouseEntered((MouseEvent e) -> button.setEffect(glow));
        button.setOnMouseExited((MouseEvent e) -> button.setEffect(null));
    }
}