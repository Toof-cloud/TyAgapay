package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Citizen;
import util.DatabaseHandler;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class DistributeController2 implements Initializable {

    @FXML private TableView<Citizen> citizenTable;
    @FXML private TableColumn<Citizen, String> citizenIDColumn;
    @FXML private TableColumn<Citizen, String> citizenFirstNameColumn;
    @FXML private TableColumn<Citizen, String> citizenLastNameColumn;
    @FXML private TableColumn<Citizen, String> contactColumn;
    @FXML private TableColumn<Citizen, String> maritalStatusColumn;
    @FXML private TableColumn<Citizen, String> validIDTypeColumn;
    @FXML private TableColumn<Citizen, String> validIDCodeColumn;
    @FXML private TableColumn<Citizen, Integer> householdColumn;
    @FXML private TableColumn<Citizen, String> houseColumn;
    @FXML private TableColumn<Citizen, String> barangayColumn;
    @FXML private TableColumn<Citizen, String> cityColumn;
    @FXML private TableColumn<Citizen, String> regionColumn;
    @FXML private TableColumn<Citizen, String> zipColumn;
    @FXML private TableColumn<Citizen, String> countryColumn;

    @FXML private TextField searchTextField;
    @FXML private Label disasterTypeLabel;
    @FXML private Button distributebutton;

    private final ObservableList<Citizen> citizenList = FXCollections.observableArrayList();
    private FilteredList<Citizen> filteredData;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeColumns();
        displayCitizens();
        setupSearch();
    }

    private void initializeColumns() {
        citizenIDColumn.setCellValueFactory(new PropertyValueFactory<>("citizenID"));
        citizenFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("citizenFirstname"));
        citizenLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("citizenLastname"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contactNum"));
        maritalStatusColumn.setCellValueFactory(new PropertyValueFactory<>("maritalStatus"));
        householdColumn.setCellValueFactory(new PropertyValueFactory<>("householdNo"));
        validIDTypeColumn.setCellValueFactory(new PropertyValueFactory<>("validIdType"));
        validIDCodeColumn.setCellValueFactory(new PropertyValueFactory<>("validIdCode"));
        houseColumn.setCellValueFactory(new PropertyValueFactory<>("houseNo"));
        barangayColumn.setCellValueFactory(new PropertyValueFactory<>("barangayName"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("cityName"));
        regionColumn.setCellValueFactory(new PropertyValueFactory<>("regionName"));
        zipColumn.setCellValueFactory(new PropertyValueFactory<>("zip"));
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("countryName"));

        // Mask valid ID code
        validIDCodeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : "*".repeat(item.length()));
            }
        });
    }

    private void displayCitizens() {
        citizenList.clear();
        try (ResultSet result = DatabaseHandler.getCitizens()) {
            while (result.next()) {
                Citizen citizen = new Citizen(
                    result.getString("citizen_id"),
                    result.getString("citizenfirstname"),
                    result.getString("citizenlastname"),
                    result.getString("contactNum"),
                    result.getString("marital_status"),
                    result.getString("household_no"),
                    result.getString("valid_id_type"),
                    result.getString("valid_id_code"),
                    result.getString("house_no"),
                    result.getString("barangay_name"),
                    result.getString("city_name"),
                    result.getString("region_name"),
                    result.getString("zip"),
                    result.getString("country_name")
                );
                citizenList.add(citizen);
            }
            citizenTable.setItems(citizenList);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Error", "Could not load citizens: " + e.getMessage());
        }
    }

    private void setupSearch() {
        filteredData = new FilteredList<>(citizenList, p -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(citizen -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return citizen.getCitizenFirstname().toLowerCase().contains(lowerCaseFilter)
                        || citizen.getCitizenLastname().toLowerCase().contains(lowerCaseFilter)
                        || citizen.getBarangayName().toLowerCase().contains(lowerCaseFilter)
                        || citizen.getCitizenID().toLowerCase().contains(lowerCaseFilter);
            });
        });
        SortedList<Citizen> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(citizenTable.comparatorProperty());
        citizenTable.setItems(sortedData);
    }

        private String mapDisasterType(String label) {
        switch (label.trim().toUpperCase()) {
            case "TYPHOON / FLOOD":
            case "TYPHOON AND FLOOD":
                return "Typhoon and Flood";
            case "FIRE":
                return "Fire";
            case "EARTHQUAKE":
                return "Earthquake";
            case "LANDSLIDE":
                return "Landslide";
            case "VOLCANIC ERUPTION":
                return "Volcanic Eruption";
            case "DROUGHT":
                return "Drought";
            case "EPIDEMIC":
                return "Epidemic";
            default:
                return label; // fallback
        }
    }

    @FXML
    private void distributeToSelected(ActionEvent event) {
    try {
        Citizen selectedCitizen = citizenTable.getSelectionModel().getSelectedItem();
        if (selectedCitizen == null) {
            showError("Selection Required", "Please select a citizen from the table.");
            return;
        }

        // Use the mapping here!
        String disasterType = mapDisasterType(disasterTypeLabel.getText());
        if (disasterType == null || disasterType.trim().isEmpty()) {
            showError("Missing Data", "Disaster type is not set");
            return;
        }

        String disasterId = getDisasterIdFromType(disasterType);
        if (disasterId == null) {
            // Error already shown in getDisasterIdFromType
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DistributePage3.fxml"));
        Parent root = loader.load();

        DistributeController3 controller = loader.getController();
        controller.setDistributionData(
            selectedCitizen.getCitizenID(),
            selectedCitizen.getCitizenFirstname() + " " + selectedCitizen.getCitizenLastname(),
            selectedCitizen.getHouseholdNo(),
            disasterId,
            disasterType
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

    } catch (Exception e) {
        e.printStackTrace();
        showError("Distribution Error", "Could not process distribution: " + e.getMessage());
    }
}

    /**
     * Gets the disaster ID from the database for the given disaster type.
     * Returns null and shows an error if not found.
     */
    private String getDisasterIdFromType(String disasterType) {
        String disasterId = null;
        String query = "SELECT disaster_id FROM disaster_types WHERE disaster_name = ?";
        try (
            Connection conn = DatabaseHandler.getDBConnection();
            PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setString(1, disasterType.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    disasterId = rs.getString("disaster_id");
                } else {
                    showError("Invalid Disaster Type", "No matching disaster type found for: " + disasterType);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Error", "Could not retrieve disaster ID: " + e.getMessage());
        }
        return disasterId;
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Navigation methods
    @FXML
    private void goToInventoryButtonHandler(ActionEvent event) {
        navigateToPage("/fxml/InventoryTablePage.fxml", event);
    }

    @FXML
    private void goToRegisterButtonHandler(ActionEvent event) {
        navigateToPage("/fxml/RegisterTablePage.fxml", event);
    }

    @FXML
    private void returnToDistributePage1ButtonHandler(ActionEvent event) {
        navigateToPage("/fxml/DistributePage1.fxml", event);
    }

    private void navigateToPage(String fxmlPath, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Navigation Error", "Could not load page: " + e.getMessage());
        }
    }

    // To be called by previous controller to set the disaster type
    public void setDisasterType(String disasterType) {
        if (disasterType == null || disasterType.trim().isEmpty()) {
            throw new IllegalArgumentException("Disaster type cannot be null or empty");
        }
        disasterTypeLabel.setText(disasterType);
    }
}