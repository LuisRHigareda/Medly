package com.medical.userservice.dto;

public class DoctorResponse {
    private Integer id;
    private Integer userId;
    private String email;
    private String name;
    private String lastName;
    private String medicalLicenseNumber;
    private String specialization;
    private Integer consultingRoomId;
    private String consultingRoom;
    private Integer clinicId;
    private String clinicName;

    public DoctorResponse(Integer id, Integer userId, String email, String name, String lastName,
                          String medicalLicenseNumber, String specialization, Integer consultingRoomId,
                          String consultingRoom, Integer clinicId, String clinicName) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.medicalLicenseNumber = medicalLicenseNumber;
        this.specialization = specialization;
        this.consultingRoomId = consultingRoomId;
        this.consultingRoom = consultingRoom;
        this.clinicId = clinicId;
        this.clinicName = clinicName;
    }

    public Integer getId() { return id; }
    public Integer getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getLastName() { return lastName; }
    public String getMedicalLicenseNumber() { return medicalLicenseNumber; }
    public String getSpecialization() { return specialization; }
    public Integer getConsultingRoomId() { return consultingRoomId; }
    public String getConsultingRoom() { return consultingRoom; }
    public Integer getClinicId() { return clinicId; }
    public String getClinicName() { return clinicName; }
}
