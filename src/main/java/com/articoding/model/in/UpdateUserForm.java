package com.articoding.model.in;

public class UpdateUserForm {

    private String password;

    private Boolean enabled;

    public UpdateUserForm() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
