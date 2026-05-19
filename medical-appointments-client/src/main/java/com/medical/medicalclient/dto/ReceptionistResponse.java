package com.medical.medicalclient.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Yuri German Garcia López - 252583
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceptionistResponse {
    private Integer id;
    private Integer userId;
    private String email;
    private String name;
    private String lastName;
    private Integer clinicId;
    private String clinicName;

    public ReceptionistResponse() {
    }

    public ReceptionistResponse(Integer id, Integer userId, String email, String name, String lastName, Integer clinicId, String clinicName) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.clinicId = clinicId;
        this.clinicName = clinicName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }
}