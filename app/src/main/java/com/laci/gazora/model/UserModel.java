package com.laci.gazora.model;

import java.util.HashMap;
import java.util.Map;

public class UserModel {
    private String userName;
    private String email;
    private String name;


    public UserModel(String userName, String email, String name) {
        this.userName = userName;
        this.email = email;
        this.name = name;

    }

    public UserModel() {}


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
