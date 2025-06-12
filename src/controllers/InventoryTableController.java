package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.time.LocalDateTime;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Citizen;
import model.Inventory;
import util.DatabaseHandler;

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
    private Button addInventoryButton;

    //display fields for inventory items
    @FXML
    private TextField riceDisplayField;
    @FXML
    private TextField sardinesDisplayField;
    @FXML
    private TextField cornedBeefDisplayField;
    @FXML
    private TextField tunaDisplayField;
    @FXML
    private TextField noodlesDisplayField;
    @FXML
    private TextField biscuitsDisplayField;
    @FXML
    private TextField bottledWaterDisplayField;
    @FXML
    private TextField barSoapDisplayField;
    @FXML
    private TextField toothpasteDisplayField;
    @FXML
    private TextField shampooDisplayField;
    @FXML
    private TextField napkinDisplayField;
    @FXML
    private TextField powderedMilkDisplayField;

    ObservableList<Inventory> inventoryList = FXCollections.observableArrayList();
    //table view
    @FXML
    private TableView<Inventory> inventoryTable;
    @FXML
    private TableColumn<Inventory, String> inventoyIDColumn;
    @FXML
    private TableColumn<Inventory, String> distributorColumn;
    @FXML
    private TableColumn<Inventory, String> receivedByColumn;
    @FXML
    private TableColumn<Inventory, String> barangayColumn;
    @FXML
    private TableColumn<Inventory, String> inventoryListColumn;
    @FXML
    private TableColumn<Inventory, String> quantityColumn;
    @FXML
    private TableColumn<Inventory, String> dateColumn;

    // for inputting new inventory items
    @FXML
    private TextField newRiceField;
    @FXML
    private TextField newCannedSardinesField;
    @FXML
    private TextField newCannedBeef;
    @FXML
    private TextField newCannnedTunaField;
    @FXML
    private TextField newNoodlesField;
    @FXML
    private TextField newBiscuitsField;
    @FXML
    private TextField newBottledWaterField;
    @FXML
    private TextField newBarSoapField;
    @FXML
    private TextField newToothPasteField;
    @FXML
    private TextField newShampooField;
    @FXML
    private TextField newNapkinField;
    @FXML
    private TextField newPowderedMilkField;
    
    // combobox for distributor, receiver, and date
    @FXML
    private ComboBox<String> fromComboBox;
    @FXML
    private ComboBox<String> selectReceiverComboBox;
    @FXML
    private ComboBox<String> dateComboBox;

    // label
    @FXML
    private Label displayDateLabel;
    private Map<String, Integer> nameToIdMap = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the inventory table
        initializeCol();
        displayInventory();
        displayItemQuantities();
        
        // items quantity display-only
        riceDisplayField.setEditable(false);
        sardinesDisplayField.setEditable(false);
        cornedBeefDisplayField.setEditable(false);
        tunaDisplayField.setEditable(false);
        noodlesDisplayField.setEditable(false);
        biscuitsDisplayField.setEditable(false);
        bottledWaterDisplayField.setEditable(false);
        barSoapDisplayField.setEditable(false);
        toothpasteDisplayField.setEditable(false);
        shampooDisplayField.setEditable(false);
        napkinDisplayField.setEditable(false);
        powderedMilkDisplayField.setEditable(false);
    
        // Initialization code
        addGlowEffect(goToDistributeButton);
        addGlowEffect(goToHistoryButton);
        addGlowEffect(returnToHomePageButton);
        addGlowEffect(goToRegisterButton);
        addGlowEffect(addInventoryButton);

        fromComboBox.setItems(FXCollections.observableArrayList("GO", "NGO", "PRIVATE"));
        populateReceiverComboBox();
        startClock();
    }

    
    //to display the list of the officials in the receiver combobox
    private void populateReceiverComboBox() {
    ObservableList<String> receivers = FXCollections.observableArrayList();
    nameToIdMap.clear(); // Clear previous data
    String barangayName = model.AdminSession.getInstance().getAdminBaranggay();
    try {
        ResultSet rs = DatabaseHandler.getBarangayOfficials(barangayName);
        while (rs.next()) {
            String fullName = rs.getString("adminfirstname") + " " + rs.getString("adminlastname");
            int adminId = rs.getInt("admin_id");
            receivers.add(fullName);
            nameToIdMap.put(fullName, adminId);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    selectReceiverComboBox.setItems(receivers);
}

    // to display the current date and time
    private void startClock() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    // Set the label immediately
    displayDateLabel.setText(LocalDateTime.now().format(formatter));
    Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
        displayDateLabel.setText(LocalDateTime.now().format(formatter));
    }));
    clock.setCycleCount(Timeline.INDEFINITE);
    clock.play();
}
    public void refreshTable() {
        displayInventory();
    }
    private void initializeCol(){
        inventoyIDColumn.setCellValueFactory(new PropertyValueFactory<>("inventoryId"));
        distributorColumn.setCellValueFactory(new PropertyValueFactory<>("distributor"));
        receivedByColumn.setCellValueFactory(new PropertyValueFactory<>("receivedBy"));
        barangayColumn.setCellValueFactory(new PropertyValueFactory<>("barangay"));
        inventoryListColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("inventoryDate"));

    }

    private void displayInventory(){
    inventoryList.clear();
    String barangayName = model.AdminSession.getInstance().getAdminBaranggay();
    ResultSet result = DatabaseHandler.getInventoryForBarangay(barangayName);

    try {
        while(result.next()) {
            String inventoryId = result.getString("inventory_id");
            String distributor = result.getString("distributor");
            String receivedBy = result.getString("received_by"); // name from SQL JOIN
            String barangay = result.getString("barangay");      // name from SQL JOIN
            String itemName = result.getString("item_name");
            String quantity = result.getString("quantity");
            String inventoryDate = result.getString("inventory_date");

            Inventory inventoryItem = new Inventory(
                inventoryId, distributor, receivedBy, barangay, itemName, quantity, inventoryDate, 0,0
                // You can add receivedById and barangayId if you want, but don't display them
            );
            inventoryList.add(inventoryItem);
        }
        inventoryTable.setItems(inventoryList);
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    private void displayItemQuantities() {
    // Clear all fields first
    riceDisplayField.setText("0");
    sardinesDisplayField.setText("0");
    cornedBeefDisplayField.setText("0");
    tunaDisplayField.setText("0");
    noodlesDisplayField.setText("0");
    biscuitsDisplayField.setText("0");
    bottledWaterDisplayField.setText("0");
    barSoapDisplayField.setText("0");
    toothpasteDisplayField.setText("0");
    shampooDisplayField.setText("0");
    napkinDisplayField.setText("0");
    powderedMilkDisplayField.setText("0");

    String barangayName = model.AdminSession.getInstance().getAdminBaranggay();
    ResultSet rs = DatabaseHandler.getItemStockForBarangay(barangayName);

    try {
        while (rs.next()) {
            String itemName = rs.getString("item_name");
            String quantity = rs.getString("quantity");
            switch (itemName) {
                case "Rice":
                    riceDisplayField.setText(quantity);
                    break;
                case "Canned Sardines":
                    sardinesDisplayField.setText(quantity);
                    break;
                case "Canned Beef":
                    cornedBeefDisplayField.setText(quantity);
                    break;
                case "Canned Tuna":
                    tunaDisplayField.setText(quantity);
                    break;
                case "Noodles":
                    noodlesDisplayField.setText(quantity);
                    break;
                case "Biscuits":
                    biscuitsDisplayField.setText(quantity);
                    break;
                case "Bottle Water":
                    bottledWaterDisplayField.setText(quantity);
                    break;
                case "Bar Soap":
                    barSoapDisplayField.setText(quantity);
                    break;
                case "Toothpaste":
                    toothpasteDisplayField.setText(quantity);
                    break;
                case "Shampoo":
                    shampooDisplayField.setText(quantity);
                    break;
                case "Napkin":
                    napkinDisplayField.setText(quantity);
                    break;
                case "Powdered Milk":
                    powderedMilkDisplayField.setText(quantity);
                    break;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    @FXML
private void addInventoryButtonHandler(ActionEvent event) {
    try {
        String distributor = fromComboBox.getValue();
        String receiverName = selectReceiverComboBox.getValue();
        String barangayName = model.AdminSession.getInstance().getAdminBaranggay();
        String inventoryDate = displayDateLabel.getText();

        if (distributor == null || receiverName == null || barangayName == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all required fields.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        // Confirmation dialog
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to add this inventory?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("Confirm Inventory Addition");
        if (confirm.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) {
            return;
        }

        // Use the map for robust ID lookup
        Integer receivedById = nameToIdMap.get(receiverName);
        if (receivedById == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Selected official does not exist in the database.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        int barangayId = DatabaseHandler.getBarangayIdByName(barangayName);

        String[] itemNames = {
            "Rice", "Canned Sardines", "Canned Beef", "Canned Tuna", "Noodles",
            "Biscuits", "Bottle Water", "Bar Soap", "Toothpaste", "Shampoo", "Napkin", "Powdered Milk"
        };
        TextField[] fields = {
            newRiceField, newCannedSardinesField, newCannedBeef, newCannnedTunaField, newNoodlesField,
            newBiscuitsField, newBottledWaterField, newBarSoapField, newToothPasteField, newShampooField, newNapkinField, newPowderedMilkField
        };

        Map<Integer, Integer> itemQuantities = new java.util.HashMap<>();
        for (int i = 0; i < itemNames.length; i++) {
            TextField field = fields[i];
            String text = field.getText();
            if (text != null && !text.trim().isEmpty()) {
                try {
                    int quantity = Integer.parseInt(text.trim());
                    if (quantity > 0) {
                        int itemId = DatabaseHandler.getItemIdByName(itemNames[i]);
                        itemQuantities.put(itemId, quantity);
                    }
                } catch (NumberFormatException ignored) {}
            }
        }

        if (itemQuantities.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "No items to add.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        Inventory inventory = new Inventory(
            null, distributor, receiverName, barangayName, "", "", inventoryDate, receivedById, barangayId
        );

        boolean success = DatabaseHandler.addInventory(inventory, itemQuantities);

        if (success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Inventory added successfully!", ButtonType.OK);
            alert.showAndWait();
            // Clear only the input fields, not the display fields
            newRiceField.clear();
            newCannedSardinesField.clear();
            newCannedBeef.clear();
            newCannnedTunaField.clear();
            newNoodlesField.clear();
            newBiscuitsField.clear();
            newBottledWaterField.clear();
            newBarSoapField.clear();
            newToothPasteField.clear();
            newShampooField.clear();
            newNapkinField.clear();
            newPowderedMilkField.clear();
            // Refresh inventory table and item quantities
            displayInventory();
            displayItemQuantities();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to add inventory.", ButtonType.OK);
            alert.showAndWait();
        }
    } catch (Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred: " + e.getMessage(), ButtonType.OK);
        alert.showAndWait();
    }
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
