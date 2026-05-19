package com.medical.medicalclient.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * Data Transfer Object representing a doctor's complete profile response
 * Maps relational grid fields including clinic and consulting room contexts
 * @author Yuri German Garcia López - 252583
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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

    /**
     * Default constructor required by Jackson for JSON deserialization.
     */
    public DoctorResponse() {
    }

    public DoctorResponse(Integer id, Integer userId, String email, String name, String lastName, String medicalLicenseNumber, String specialization, Integer consultingRoomId, String consultingRoom, Integer clinicId, String clinicName) {
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

    public String getMedicalLicenseNumber() {
        return medicalLicenseNumber;
    }

    public void setMedicalLicenseNumber(String medicalLicenseNumber) {
        this.medicalLicenseNumber = medicalLicenseNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Integer getConsultingRoomId() {
        return consultingRoomId;
    }

    public void setConsultingRoomId(Integer consultingRoomId) {
        this.consultingRoomId = consultingRoomId;
    }

    public String getConsultingRoom() {
        return consultingRoom;
    }

    public void setConsultingRoom(String consultingRoom) {
        this.consultingRoom = consultingRoom;
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