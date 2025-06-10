package model;

public class AdminSession {
    private static AdminSession instance;
    private Admin admin;

    private AdminSession() {}

    public static AdminSession getInstance() {
        if (instance == null) {
            instance = new AdminSession();
        }
        return instance;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Admin getAdmin() {
        return admin;
    }
    public String getAdminBaranggay(){
        if (admin != null) {
            return admin.getBaranggay();
        }
        return null;
    }

}