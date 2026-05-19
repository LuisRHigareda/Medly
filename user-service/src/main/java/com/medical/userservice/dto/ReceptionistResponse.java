package com.medical.userservice.dto;

public class ReceptionistResponse {
    private Integer id;
    private Integer userId;
    private String email;
    private String name;
    private String lastName;
    private Integer clinicId;
    private String clinicName;

    public ReceptionistResponse(Integer id, Integer userId, String email, String name, String lastName,
                                Integer clinicId, String clinicName) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.clinicId = clinicId;
        this.clinicName = clinicName;
    }

    public Integer getId() { return id; }
    public Integer getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getLastName() { return lastName; }
    public Integer getClinicId() { return clinicId; }
    public String getClinicName() { return clinicName; }
}
