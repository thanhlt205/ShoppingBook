package com.example.soppingbook_api.models;

public class AcountModel {
    String email;
    String password;

    public AcountModel() {
    }

    public AcountModel(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public AcountModel(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
