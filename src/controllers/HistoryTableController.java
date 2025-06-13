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

public class HistoryTableController implements Initializable {

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
    @FXML
    private TableColumn<Citizen, String> statusColumn;

    @FXML
    private Button returnToHomePageButton;
    @FXML
    private Button goToDistributeButton;
    @FXML
    private Button goToRegisterButton;
    @FXML
    private Button goToInventoryButton;
    @FXML
    private TextField searchTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addGlowEffect(returnToHomePageButton);
        addGlowEffect(goToDistributeButton);
        addGlowEffect(goToInventoryButton);
        addGlowEffect(goToRegisterButton);

        // Set up columns - property names must match Citizen getters (minus "get", lowercase first letter)
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
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadDistributedCitizens();
    }

    private void loadDistributedCitizens() {
        ObservableList<Citizen> citizens = FXCollections.observableArrayList();
        try {
            ResultSet rs = DatabaseHandler.getDistributedCitizens();
            while (rs.next()) {
                Citizen c = new Citizen(
                    rs.getString("citizen_id"),
                    rs.getString("citizenlastname"),
                    rs.getString("citizenfirstname"),
                    rs.getString("contactNum"),
                    rs.getString("marital_status"),
                    rs.getString("household_no"),
                    rs.getString("valid_id_type"),
                    rs.getString("valid_id_code"),
                    rs.getString("house_no"),
                    rs.getString("barangay_name"),
                    rs.getString("city_name"),
                    rs.getString("zip"),
                    rs.getString("region_name"),
                    rs.getString("country_name"),
                    rs.getBoolean("distributed") ? "Distributed" : "Not Distributed"
                );
                citizens.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        citizenTable.setItems(citizens);
    }

    @FXML
    private void goToDistributeButtonHandler(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DistributePage1.fxml"));
            Parent root = loader.load();
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
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToInventoryButtonHandler(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/InventoryTablePage.fxml"));
            Parent root = loader.load();
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
        glow.setColor(Color.web("#010a5b"));
        glow.setRadius(10);
        glow.setSpread(0.4);

        button.setOnMouseEntered((MouseEvent e) -> button.setEffect(glow));
        button.setOnMouseExited((MouseEvent e) -> button.setEffect(null));
    }
}