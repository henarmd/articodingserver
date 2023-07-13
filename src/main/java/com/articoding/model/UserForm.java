package com.articoding.model;

import java.util.List;

public class UserForm {

    private String username;

    private String password;

    private List<String> roles;

    private List<Long> classes;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<Long> getClasses() {
        return classes;
    }

    public void setClasses(List<Long> classes) {
        this.classes = classes;
    }
}
