package model;

import javafx.beans.property.SimpleStringProperty;

public class Citizen {
    private SimpleStringProperty citizen_id;
    private SimpleStringProperty citizenlastname;
    private SimpleStringProperty citizenfirstname;
    private SimpleStringProperty contactNum;
    private SimpleStringProperty marital_status;
    private SimpleStringProperty household_no;
    private SimpleStringProperty valid_id_type;
    private SimpleStringProperty valid_id_code;
    private SimpleStringProperty house_no;
    private SimpleStringProperty barangay_name;
    private SimpleStringProperty city_name;
    private SimpleStringProperty zip;
    private SimpleStringProperty region_name;
    private SimpleStringProperty country_name;
    private SimpleStringProperty status;

    // 14-parameter constructor (no status)
    public Citizen(
        String citizen_id,
        String citizenlastname,
        String citizenfirstname,
        String contactNum,
        String marital_status,
        String household_no,
        String valid_id_type,
        String valid_id_code,
        String house_no,
        String barangay_name,
        String city_name,
        String zip,
        String region_name,
        String country_name
    ) {
        this(citizen_id, citizenlastname, citizenfirstname, contactNum, marital_status, household_no,
            valid_id_type, valid_id_code, house_no, barangay_name, city_name, zip, region_name, country_name, "");
    }

    // 15-parameter constructor (with status)
    public Citizen(
        String citizen_id,
        String citizenlastname,
        String citizenfirstname,
        String contactNum,
        String marital_status,
        String household_no,
        String valid_id_type,
        String valid_id_code,
        String house_no,
        String barangay_name,
        String city_name,
        String zip,
        String region_name,
        String country_name,
        String status
    ) {
        this.citizen_id = new SimpleStringProperty(citizen_id);
        this.citizenlastname = new SimpleStringProperty(citizenlastname);
        this.citizenfirstname = new SimpleStringProperty(citizenfirstname);
        this.contactNum = new SimpleStringProperty(contactNum);
        this.marital_status = new SimpleStringProperty(marital_status);
        this.household_no = new SimpleStringProperty(household_no);
        this.valid_id_type = new SimpleStringProperty(valid_id_type);
        this.valid_id_code = new SimpleStringProperty(valid_id_code);
        this.house_no = new SimpleStringProperty(house_no);
        this.barangay_name = new SimpleStringProperty(barangay_name);
        this.city_name = new SimpleStringProperty(city_name);
        this.zip = new SimpleStringProperty(zip);
        this.region_name = new SimpleStringProperty(region_name);
        this.country_name = new SimpleStringProperty(country_name);
        this.status = new SimpleStringProperty(status);
    }

    public String getCitizenID() {
        return citizen_id.get();
    }

    public String getCitizenLastname() {
        return citizenlastname.get();
    }

    public String getCitizenFirstname() {
        return citizenfirstname.get();
    }

    public String getContactNum() {
        return contactNum.get();
    }

    public String getMaritalStatus() {
        return marital_status.get();
    }

    public String getHouseholdNo() {
        return household_no.get();
    }

    public String getValidIdType() {
        return valid_id_type.get();
    }

    public String getValidIdCode() {
        return valid_id_code.get();
    }

    public String getHouseNo() {
        return house_no.get();
    }

    public String getBarangayName() {
        return barangay_name.get();
    }

    public String getCityName() {
        return city_name.get();
    }

    public String getZip() {
        return zip.get();
    }

    public String getRegionName() {
        return region_name.get();
    }

    public String getCountryName() {
        return country_name.get();
    }

    public String getStatus() {
        return status.get();
    }
}