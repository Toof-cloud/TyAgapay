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
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private TextField searchTextField;
    
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
        // Initialize the buttons with a glow effect
        addGlowEffect(goToDistributeButton);
        addGlowEffect(goToHistoryButton);
        addGlowEffect(goToInventoryButton);
        addGlowEffect(returnToHomePageButton);

        // Initialize the table columns
        initializeCol();
        // Display the citizens in the table
        displayCitizens();

        // Set barangay location label
        model.Admin admin = model.AdminSession.getInstance().getAdmin();
        if (admin != null) {
            barangayLocation.setText("Barangay " + admin.getBaranggay());
             // or getBarangayName() if that's your getter
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