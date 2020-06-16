package com.app.dto;

import javax.validation.constraints.NotBlank;

public class LoginDto {

    @NotBlank
    private String password;

    @NotBlank    
    private String userName;

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    
}