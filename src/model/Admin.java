package model;
import javafx.beans.property.SimpleStringProperty;
public class Admin {
    private SimpleStringProperty admin_id;
    private SimpleStringProperty adminfirstname;
    private SimpleStringProperty adminlastname;
    private SimpleStringProperty barangay_name;
    private SimpleStringProperty official_type;
    private SimpleStringProperty adminpassword;
    private SimpleStringProperty contact;
    private SimpleStringProperty gmail;
    private SimpleStringProperty created_at;

    public Admin(String admin_id, String adminfirstname, String adminlastname, String barangay_name, String official_type, String adminpassword, String contact, String gmail, String created_at) {
        this.admin_id = new SimpleStringProperty(admin_id);
        this.adminfirstname = new SimpleStringProperty(adminfirstname);
        this.adminlastname = new SimpleStringProperty(adminlastname);
        this.barangay_name = new SimpleStringProperty(barangay_name);
        this.official_type = new SimpleStringProperty(official_type);
        this.adminpassword = new SimpleStringProperty(adminpassword);
        this.contact = new SimpleStringProperty(contact);
        this.gmail = new SimpleStringProperty(gmail);
        this.created_at = new SimpleStringProperty(created_at);
    }
    public String getAdminID() {
        return admin_id.get();
    }
    public String getAdminfirstname() {
        return adminfirstname.get();
    }
    public String getAdminlastname() {
        return adminlastname.get();
    }
    public String getBaranggay() {
        return barangay_name.get();
    }
    public String getOfficial_type() {
        return official_type.get();
    }
    public String getAdminpassword() {
        return adminpassword.get();
    }
    public String getContact() {
        return contact.get();
    }
    public String getGmail() {
        return gmail.get();
    }
    public String getCreatedAt() {
        return created_at.get();
    }
}
