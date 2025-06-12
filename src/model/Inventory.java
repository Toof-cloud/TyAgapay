package model;
import javafx.beans.property.SimpleStringProperty;

public class Inventory {
    private SimpleStringProperty inventoryId;
    private SimpleStringProperty distributor;
    private SimpleStringProperty receivedBy;   // full name as string
    private SimpleStringProperty barangay;
    private SimpleStringProperty itemName;
    private SimpleStringProperty quantity;
    private SimpleStringProperty inventoryDate;
    private int receivedById;
    private int barangayId;

    public Inventory(String inventoryId, String distributor, String receivedBy, String barangay, String itemName, String quantity, String inventoryDate, int receivedById, int barangayId) {
        this.inventoryId = new SimpleStringProperty(inventoryId);
        this.distributor = new SimpleStringProperty(distributor);
        this.receivedBy = new SimpleStringProperty(receivedBy);
        this.barangay = new SimpleStringProperty(barangay);
        this.itemName = new SimpleStringProperty(itemName);
        this.quantity = new SimpleStringProperty(quantity);
        this.inventoryDate = new SimpleStringProperty(inventoryDate); // Automatically set to current date
        this.receivedById = receivedById;
        this.barangayId = barangayId;
    }

    public String getInventoryId() {
        return inventoryId.get();
    }

    public String getDistributor() {
        return distributor.get();
    }

    public String getReceivedBy() {
        return receivedBy.get();
    }

    public String getBarangay() {
        return barangay.get();
    }

    public String getItemName() {
        return itemName.get();
    }

    public String getQuantity() {
        return quantity.get();
    }
    public String getInventoryDate() {
        return inventoryDate.get();
    }
    public int getReceivedById() {
        return receivedById;
    }
    public int getBarangayId() {
        return barangayId;
    }
}