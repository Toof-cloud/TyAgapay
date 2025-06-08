package controllers;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Admin;
import javafx.fxml.Initializable;
import util.DatabaseHandler;

public class AdminTableController implements Initializable {
    @FXML
    private Label barangayLocation;
    @FXML
    private Button deleteAdminButton;
    @FXML
    private Button returnToHomePageButton;
   
    ObservableList<Admin> adminList = FXCollections.observableArrayList();

    @FXML
    private TableView<Admin> adminTable;
    @FXML
    private TableColumn<Admin, String> adminIdColumn;
    @FXML
    private TableColumn<Admin, String> firstNameColumn;
    @FXML
    private TableColumn<Admin, String> LastNameColumn;
    @FXML
    private TableColumn<Admin, String> baranggayColumn;
    @FXML
    private TableColumn<Admin, String> officialTypeColumn;
    @FXML
    private TableColumn<Admin, String> passwordColumn;
    @FXML
    private TableColumn<Admin, String> contactColumn;
    @FXML
    private TableColumn<Admin, String> gmailColumn;
    @FXML
    private TableColumn<Admin, String> createdAtColumn;

    @FXML
    private AnchorPane homeRoot;

    private Stage addOfficialStage = null;
    private Stage editOfficialStage = null;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize the admin table with data
        initializeCol();
        displayAdmins();

        addGlowEffect(deleteAdminButton);
        addGlowEffect(returnToHomePageButton);

        // Set the barangay location label
        model.Admin admin = model.AdminSession.getInstance().getAdmin();
        if (admin != null) {
            barangayLocation.setText("Barangay " + admin.getBaranggay());
             // or getBarangayName() if that's your getter
        }
        
        if (homeRoot != null) {
        homeRoot.setOnMouseClicked(e -> {
        System.out.println("homeRoot clicked!");
        if (addOfficialStage != null && addOfficialStage.isShowing()) {
            addOfficialStage.close();
        }
        if (editOfficialStage != null && editOfficialStage.isShowing()) {
            editOfficialStage.close();
        }
    });
        }
    }
    public void refreshTable() {
    displayAdmins();
}
    private void initializeCol(){
    adminIdColumn.setCellValueFactory(new PropertyValueFactory<>("adminID"));
    firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("adminfirstname"));
    LastNameColumn.setCellValueFactory(new PropertyValueFactory<>("adminlastname"));
    baranggayColumn.setCellValueFactory(new PropertyValueFactory<>("baranggay"));
    officialTypeColumn.setCellValueFactory(new PropertyValueFactory<>("official_type"));

    // Show masked password using bullet characters
    passwordColumn.setCellValueFactory(new PropertyValueFactory<>("adminpassword"));
    passwordColumn.setCellFactory(column -> {
        return new TableCell<Admin, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("•".repeat(item.length())); // Use "*" if preferred
                }
            }
        };
    });

    contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
    gmailColumn.setCellValueFactory(new PropertyValueFactory<>("gmail"));
    createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
}

    private void displayAdmins(){
        adminList.clear();
        ResultSet result = DatabaseHandler.getAdmins();

        try {
            while(result.next()) {
                String adminId = result.getString("admin_id");
                String firstName = result.getString("adminfirstname");
                String lastName = result.getString("adminlastname");
                String baranggay = result.getString("barangay_name");
                String officialType = result.getString("official_type");
                String password = result.getString("adminpassword");
                String contact = result.getString("contact");
                String gmail = result.getString("gmail");
                String createdAt = result.getString("created_at");

                Admin admin = new Admin(adminId, firstName, lastName, baranggay, officialType, password, contact, gmail, createdAt);
                adminList.add(admin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adminTable.setItems(adminList);
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
    @FXML
    private void deleteBaranggayOfficialHandler(ActionEvent event) {
        Admin selectedAdmin = adminTable.getSelectionModel().getSelectedItem();
        if (selectedAdmin == null) {
            // Show alert if no admin is selected
            showAlert("Please select an admin to delete.");
            return;
        }

        // Confirm deletion
        boolean confirmation = showConfirmation("Are you sure you want to delete this admin?");
        if (!confirmation) {
            return; // User cancelled the deletion
        }

        // Delete the selected admin
        boolean success = DatabaseHandler.deleteAdmin(selectedAdmin.getAdminID());
        if (success) {
            showAlert("Admin deleted successfully!");
            displayAdmins(); // Refresh the table
        } else {
            showAlert("Failed to delete admin. Please try again.");
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
    private void showAlert(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
