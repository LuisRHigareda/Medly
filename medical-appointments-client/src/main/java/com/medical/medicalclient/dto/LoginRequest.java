package com.medical.medicalclient.dto;
/**
 * @author Yuri German Garcia López - 252583
 */
public class LoginRequest {
    private String email;
    private String password;
    
    public LoginRequest(){
    }

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
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