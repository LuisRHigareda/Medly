package com.medical.medicalclient.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.time.LocalTime;
/**
 * Data Transfer Object representing a medical appointment record
 * Handles explicit format bindings for standard date and time fields
 * @author Yuri German Garcia López - 252583
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppointmentResponse {
    private Integer id;
    private Integer patientId;
    private Integer doctorId;
    private String doctorName;
    private Integer consultingRoomId;
    private String consultingRoomName;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime time;
    
    private String status; 
    
    /**
     * Default constructor required by Jackson for JSON deserialization.
     */
    public AppointmentResponse() {
    }

    public AppointmentResponse(Integer id, Integer patientId, Integer doctorId, String doctorName, Integer consultingRoomId, String consultingRoomName, LocalDate date, LocalTime time, String status) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.consultingRoomId = consultingRoomId;
        this.consultingRoomName = consultingRoomName;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Integer getConsultingRoomId() {
        return consultingRoomId;
    }

    public void setConsultingRoomId(Integer consultingRoomId) {
        this.consultingRoomId = consultingRoomId;
    }

    public String getConsultingRoomName() {
        return consultingRoomName;
    }

    public void setConsultingRoomName(String consultingRoomName) {
        this.consultingRoomName = consultingRoomName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}