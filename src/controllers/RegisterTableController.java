package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Citizen;
import util.DatabaseHandler;

public class RegisterTableController implements Initializable {
    @FXML
    private TextField citizenFistNameField;
    @FXML
    private TextField citizenLastNameField;
    @FXML
    private TextField contactNoField;
    @FXML
    private ComboBox<String> maritalStatusComboBox;
    @FXML
    private TextField householdNumberField;
    @FXML
    private ComboBox<String> validIDTypeComboBox;
    @FXML
    private TextField validIDCodeField;
    @FXML
    private TextField houseNumberField;
    @FXML
    private TextField autoBarangayField;
    @FXML
    private TextField autoCityField;
    @FXML
    private TextField autoZipField;
    @FXML
    private TextField autoRegionField;
    @FXML
    private TextField autoCountryField;

    @FXML
    private Label barangayLocation;
    @FXML
    private Button goToDistributeButton;
    @FXML
    private Button goToHistoryButton;
    @FXML
    private Button goToInventoryButton;
    @FXML
    private Button returnToHomePageButton;
    @FXML
    private Button confirmRegisterButton;
    @FXML
    private Button deleteCitizenButton;

    ObservableList<Citizen> citizenList = FXCollections.observableArrayList();
    @FXML
    private TableView<Citizen> citizenTable;
    @FXML
    private TableColumn<Citizen, String> citizenIDColumn;
    @FXML
    private TableColumn<Citizen, String> citizenLastNameColumn;
     @FXML
    private TableColumn<Citizen, String> citizenFirstNameColumn;
    @FXML
    private TableColumn<Citizen, String> contactColumn;
    @FXML
    private TableColumn<Citizen, String> maritalStatusColumn;
    @FXML
    private TableColumn<Citizen, String> householdColumn;
    @FXML
    private TableColumn<Citizen, String> validIDTypeColumn;
    @FXML
    private TableColumn<Citizen, String> validIDCodeColumn;
    @FXML
    private TableColumn<Citizen, String> houseColumn;
    @FXML
    private TableColumn<Citizen, String> barangayColumn;
    @FXML
    private TableColumn<Citizen, String> cityColumn;
    @FXML
    private TableColumn<Citizen, String> zipColumn;
    @FXML
    private TableColumn<Citizen, String> regionColumn;
    @FXML
    private TableColumn<Citizen, String> countryColumn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Make location fields display-only
        autoBarangayField.setEditable(false);
        autoCityField.setEditable(false);
        autoZipField.setEditable(false);
        autoRegionField.setEditable(false);
        autoCountryField.setEditable(false);
        
        // Initialize the buttons with a glow effect
        addGlowEffect(goToDistributeButton);
        addGlowEffect(goToHistoryButton);
        addGlowEffect(goToInventoryButton);
        addGlowEffect(returnToHomePageButton);
        addGlowEffect(confirmRegisterButton);
        addGlowEffect(deleteCitizenButton);

        //marital status options
        maritalStatusComboBox.setItems(FXCollections.observableArrayList(
        "Single", "Married", "Widowed", "Divorced"
    ));
        // valid ID type options
        // Set valid ID type options from database
        ObservableList<String> validIdTypes = FXCollections.observableArrayList();
        try (ResultSet rs = DatabaseHandler.getValidIdTypes()) {
            while (rs.next()) {
                validIdTypes.add(rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        validIDTypeComboBox.setItems(validIdTypes);

        // Initialize the table columns
        initializeCol();
        // Display the citizens in the table
        displayCitizens();

        // Set barangay location label
        model.Admin admin = model.AdminSession.getInstance().getAdmin();
        if (admin != null) {
        Map<String, String> info = util.DatabaseHandler.getBarangayLocationInfo(admin.getBaranggay());
        autoBarangayField.setText(info.getOrDefault("barangay_name", ""));
        autoCityField.setText(info.getOrDefault("city_name", ""));
        autoZipField.setText(info.getOrDefault("zip", ""));
        autoRegionField.setText(info.getOrDefault("region_name", ""));
        autoCountryField.setText(info.getOrDefault("country_name", ""));
        barangayLocation.setText("Barangay " + info.getOrDefault("barangay_name", admin.getBaranggay()));
        
        }
    
}

    public void refreshTable() {
    displayCitizens();
}

    private void initializeCol(){
        citizenIDColumn.setCellValueFactory(new PropertyValueFactory<>("citizenID"));
        citizenLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("citizenLastname"));
        citizenFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("citizenFirstname"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactNum"));
        maritalStatusColumn.setCellValueFactory(new PropertyValueFactory<>("maritalStatus"));
        householdColumn.setCellValueFactory(new PropertyValueFactory<>("householdNo"));
        validIDTypeColumn.setCellValueFactory(new PropertyValueFactory<>("validIdType"));
        validIDCodeColumn.setCellValueFactory(new PropertyValueFactory<>("validIdCode"));
        validIDCodeColumn.setCellFactory(column -> {
        return new TableCell<Citizen, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("#".repeat(item.length())); 
                }
            }
        };
    });
        houseColumn.setCellValueFactory(new PropertyValueFactory<>("houseNo"));
        barangayColumn.setCellValueFactory(new PropertyValueFactory<>("barangayName"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("cityName"));
        zipColumn.setCellValueFactory(new PropertyValueFactory<>("zip"));
        regionColumn.setCellValueFactory(new PropertyValueFactory<>("regionName"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("countryName"));
    }
    private void displayCitizens(){
        citizenList.clear();
        ResultSet result = DatabaseHandler.getCitizens();

        try {
            while(result.next()) {
                String citizenID = result.getString("citizen_id");
                String citizenLastName = result.getString("citizenlastname");
                String citizenFirstName = result.getString("citizenfirstname");
                String contactNum = result.getString("contactNum");
                String maritalStatus = result.getString("marital_status");
                String householdNo = result.getString("household_no");
                String validIDType = result.getString("valid_id_type");
                String validIDCode = result.getString("valid_id_code");
                String houseNo = result.getString("house_no");
                String barangayName = result.getString("barangay_name");
                String cityName = result.getString("city_name");
                String zip = result.getString("zip");
                String regionName = result.getString("region_name");
                String countryName = result.getString("country_name");

                Citizen citizen = new Citizen(citizenID, citizenLastName, citizenFirstName, contactNum, maritalStatus, householdNo, validIDType, validIDCode, houseNo, barangayName, cityName, zip, regionName, countryName);
                citizenList.add(citizen);
            }
            citizenTable.setItems(citizenList);
        } catch (Exception e) {
        }
    }

    @FXML
    void confirmRegisterButtonHandler(ActionEvent event) throws IOException {
        String citizenFirstName = citizenFistNameField.getText().trim();
        String citizenLastName = citizenLastNameField.getText().trim();
        String contactNo = contactNoField.getText().trim();
        String maritalStatus = maritalStatusComboBox.getValue();
        String householdNo = householdNumberField.getText().trim();
        String validIDType = validIDTypeComboBox.getValue();
        String validIDCode = validIDCodeField.getText().trim();
        String houseNo = houseNumberField.getText().trim();

        // Validate required fields
        if (citizenFirstName.isEmpty() || citizenLastName.isEmpty() || contactNo.isEmpty() ||
            maritalStatus == null || householdNo.isEmpty() || validIDType == null ||
            validIDCode.isEmpty() || houseNo.isEmpty()) {
            showAlert("Please fill in all required fields.");
            return;
        }

        // Validate contact number
        if (!contactNo.matches("^09\\d{9}$")) {
            showAlert("Contact number must start with 09 and be 11 digits long.");
            return;
        }

        // Validate household number (must be numeric)
        if (!householdNo.matches("\\d+")) {
            showAlert("Household number must be numeric.");
            return;
        }

        // Validate house number (must be 1-4 characters)
        if (houseNo.length() > 4) {
            showAlert("House number must be up to 4 characters.");
            return;
        }

        // Validate validIDCode format based on validIDType
        boolean validFormat = false;
        switch (validIDType) {
            case "Philippine Passport":
                // 1 letter + 7 digits + optional 1 letter suffix (old passports)
                validFormat = validIDCode.matches("^[A-Z]\\d{7}[A-Z]?$");
                break;
            case "Driver’s License":
                // N01-23-456789 (N: region, 2 digits; 2 digits; 6 digits)
                validFormat = validIDCode.matches("^[A-Z]\\d{2}-\\d{2}-\\d{6}$");
                break;
            case "SSS ID":
            case "UMID":
                // 2 digits-7 digits-1 digit (01-2345678-9)
                validFormat = validIDCode.matches("^\\d{2}-\\d{7}-\\d{1}$");
                break;
            case "Voter’s ID":
                // 5 letters + 7 digits (ABCDE1234567)
                validFormat = validIDCode.matches("^[A-Z]{5}\\d{7}$");
                break;
            case "PhilHealth ID":
                // 2 digits-9 digits-1 digit (12-345678901-2)
                validFormat = validIDCode.matches("^\\d{2}-\\d{9}-\\d{1}$");
                break;
            case "Postal ID":
                // PHL + 9 digits (PHL123456789)
                validFormat = validIDCode.matches("^PHL\\d{9}$");
                break;
            case "Senior Citizen ID":
                // 6 digits-4 digits-4 digits (123456-7890-0000)
                validFormat = validIDCode.matches("^\\d{6}-\\d{4}-\\d{4}$");
                break;
            case "PWD ID":
                // PWD-XX-123456 or PWD-XX-12345678 (XX: disability type, 6-8 digits)
                validFormat = validIDCode.matches("^PWD-[A-Z]{2}-\\d{6,8}$");
                break;
        }
        if (!validFormat) {
            showAlert("The code format for " + validIDType + " is invalid.\nPlease follow the required format.");
            return;
}

    // Get location info from display fields
    String barangayName = autoBarangayField.getText();
    String cityName = autoCityField.getText();
    String zip = autoZipField.getText();
    String regionName = autoRegionField.getText();
    String countryName = autoCountryField.getText();

    // Create Citizen object (citizenID can be null or empty for new)
    Citizen citizen = new Citizen(
        "", // citizenID (auto-increment)
        citizenLastName,
        citizenFirstName,
        contactNo,
        maritalStatus,
        householdNo,
        validIDType,
        validIDCode,
        houseNo,
        barangayName,
        cityName,
        zip,
        regionName,
        countryName
    );

    boolean success = DatabaseHandler.addCitizen(citizen);
    if (success) {
        showAlert("Citizen registered successfully!");
        displayCitizens();
        // Clear form fields
        citizenFistNameField.clear();
        citizenLastNameField.clear();
        contactNoField.clear();
        maritalStatusComboBox.setValue(null);
        householdNumberField.clear();
        validIDTypeComboBox.setValue(null);
        validIDCodeField.clear();
        houseNumberField.clear();
    } else {
        showAlert("Failed to register citizen. Please check your input or try again.");
    }
}

    @FXML
    void deleteCitizenButtonHandler(ActionEvent event) throws IOException {
        Citizen selectedCitizen = citizenTable.getSelectionModel().getSelectedItem();
        if (selectedCitizen == null) {
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
        boolean success = DatabaseHandler.deleteCitizen(selectedCitizen.getCitizenID());
        if (success) {
            showAlert("Admin deleted successfully!");
            displayCitizens(); // Refresh the table
        } else {
            showAlert("Failed to delete admin. Please try again.");
        }
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
    private void addGlowEffect(Button button) {
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#010a5b")); // Match your theme
        glow.setRadius(10);
        glow.setSpread(0.4);

        button.setOnMouseEntered((MouseEvent e) -> button.setEffect(glow));
        button.setOnMouseExited((MouseEvent e) -> button.setEffect(null));
    }

}