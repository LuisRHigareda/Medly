package com.medical.medicalclient.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * @author Yuri German Garcia López - 252583
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {
    private boolean success;
    private String message;
    private Integer userId;
    private String email;
    private String userRole;

    public LoginResponse(){
    }

    public LoginResponse(boolean success, String message, Integer userId, String email, String userRole) {
        this.success = success;
        this.message = message;
        this.userId = userId;
        this.email = email;
        this.userRole = userRole;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}