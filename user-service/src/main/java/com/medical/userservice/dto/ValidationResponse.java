package com.medical.userservice.dto;

public class ValidationResponse {
    private String entity;
    private Integer id;
    private boolean exists;

    public ValidationResponse(String entity, Integer id, boolean exists) {
        this.entity = entity;
        this.id = id;
        this.exists = exists;
    }

    public String getEntity() { return entity; }
    public Integer getId() { return id; }
    public boolean isExists() { return exists; }
}
