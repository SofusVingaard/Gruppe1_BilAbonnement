package org.example.bilabonnement_gruppe1.model;

public class User {
    private int id;
    private String userLogin;
    private String name;
    private String password;

    public User(String password, String name, String userLogin, int id) {
        this.password = password;
        this.name = name;
        this.userLogin = userLogin;
        this.id = id;
    }

    public User(String userLogin, String name, String password) {
        this.userLogin = userLogin;
        this.name = name;
        this.password = password;
    }
    public User(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
