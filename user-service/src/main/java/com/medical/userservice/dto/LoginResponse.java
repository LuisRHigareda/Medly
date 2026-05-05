package com.medical.userservice.dto;

public class LoginResponse {
    private boolean success;
    private String message;
    private Integer userId;
    private String email;
    private String userRole;

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

    public String getMessage() {
        return message;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getUserRole() {
        return userRole;
    }
}
