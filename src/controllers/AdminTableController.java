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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Admin;
import javafx.fxml.Initializable;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import util.DatabaseHandler;

public class AdminTableController implements Initializable {
    @FXML
    private Button addAdminButton;
    @FXML
    private Button EditAdminButton;
    @FXML
    private Button deleteAdminButton;
    @FXML
    private Button returnToHomePageButton;
    @FXML
    private TextField searchTextField;

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

        // Add glow effects to buttons
        addGlowEffect(addAdminButton);
        addGlowEffect(EditAdminButton);
        addGlowEffect(deleteAdminButton);
        addGlowEffect(returnToHomePageButton);

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
        
        // Set up search functionality
        FilteredList<Admin> filteredData = new FilteredList<>(adminList, b -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
        filteredData.setPredicate(admin -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            String lowerCaseFilter = newValue.toLowerCase();

            return admin.getAdminID().toLowerCase().contains(lowerCaseFilter)
                || admin.getAdminfirstname().toLowerCase().contains(lowerCaseFilter)
                || admin.getAdminlastname().toLowerCase().contains(lowerCaseFilter)
                || admin.getOfficial_type().toLowerCase().contains(lowerCaseFilter)
                || admin.getAdminpassword().toLowerCase().contains(lowerCaseFilter)
                || admin.getContact().toLowerCase().contains(lowerCaseFilter)
                || admin.getGmail().toLowerCase().contains(lowerCaseFilter)
                || admin.getCreatedAt().toLowerCase().contains(lowerCaseFilter);
        });
    });
        SortedList<Admin> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(adminTable.comparatorProperty());
        adminTable.setItems(sortedData);

}
    public void refreshTable() {
    displayAdmins();
}
    private void initializeCol(){
        adminIdColumn.setCellValueFactory(new PropertyValueFactory<>("adminID"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("adminfirstname"));
        LastNameColumn.setCellValueFactory(new PropertyValueFactory<>("adminlastname"));
        officialTypeColumn.setCellValueFactory(new PropertyValueFactory<>("official_type"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("adminpassword"));
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
                String officialType = result.getString("official_type");
                String password = result.getString("adminpassword");
                String contact = result.getString("contact");
                String gmail = result.getString("gmail");
                String createdAt = result.getString("created_at");

                Admin admin = new Admin(adminId, firstName, lastName, officialType, password, contact, gmail, createdAt);
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
    private void addBaranggayOfficialHandler(ActionEvent event) throws IOException {
    try {
        if (addOfficialStage != null && addOfficialStage.isShowing()) {
            addOfficialStage.toFront();
            return;
    }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddNewBaranggayOfficial.fxml"));
        Parent root = loader.load();
    
        AddOfficialController controller = loader.getController();
        controller.setAdminTableController(this);

        // Create a new stage (window)
        addOfficialStage = new Stage();
        addOfficialStage.setTitle("Add New Baranggay Official");
        addOfficialStage.initStyle(javafx.stage.StageStyle.UNDECORATED);
        addOfficialStage.setScene(new Scene(root));
        addOfficialStage.setResizable(false);

        addOfficialStage.setOnHidden(e -> addOfficialStage = null);
        
        addOfficialStage.show();
        addOfficialStage.toFront();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    @FXML
    private void editBaranggayOfficialHandler(ActionEvent event) throws IOException {
    Admin selectedAdmin = adminTable.getSelectionModel().getSelectedItem();
    if (selectedAdmin == null) {
        showAlert("Please select an admin to edit.");
        return;
    }

    try {
        if (editOfficialStage != null && editOfficialStage.isShowing()) {
            editOfficialStage.toFront();
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditBaranggayOfficial.fxml"));
        Parent root = loader.load();

        // Pass the selected admin to the EditBaranggayOfficial controller
        EditBaranggayController controller = loader.getController();
        controller.setAdminData(selectedAdmin);
        controller.setAdminTableController(this);

        // Create a new stage (window)
        editOfficialStage = new Stage();
        editOfficialStage.setTitle("Edit Baranggay Official");
        editOfficialStage.initStyle(javafx.stage.StageStyle.UNDECORATED);
        editOfficialStage.setScene(new Scene(root));
        editOfficialStage.setResizable(false);

        // Set to null when closed so it can be reopened
        editOfficialStage.setOnHidden(e -> editOfficialStage = null);

        editOfficialStage.show();
        editOfficialStage.toFront();
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
