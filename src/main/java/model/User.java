package model;

public class User {
    private String username;
    private String password;
    private boolean doctor;


    public boolean isDoctor() {
        return doctor;
    }

    public void setDoctor(boolean doctor) {
        this.doctor = doctor;
    }



    public User() {

    }

    public User(String user, String s) {
        setUsername(user);
    }

    public User(LoginData loginData) {
        setUsername(loginData.getUsername());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
