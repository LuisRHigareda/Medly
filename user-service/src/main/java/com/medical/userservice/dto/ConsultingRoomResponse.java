package com.medical.userservice.dto;

/**
 *
 * @author Leonardo Flores Leyva
 */
public class ConsultingRoomResponse {
    
    private Integer id;
    private String room;
    private String doctorName;
    private String specialization;

    public ConsultingRoomResponse() {}

    public ConsultingRoomResponse(Integer id, String room, String doctorName, String specialization) {
        this.id = id;
        this.room = room;
        this.doctorName = doctorName;
        this.specialization = specialization;
    }

    public Integer getId() {return id;}

    public String getRoom() {return room;}

    public String getDoctorName() {return doctorName;}

    public String getSpecialization() {return specialization;}

    public void setId(Integer id) {this.id = id;}

    public void setRoom(String room) {this.room = room;}

    public void setDoctorName(String doctorName) {this.doctorName = doctorName;}

    public void setSpecialization(String specialization) {this.specialization = specialization;}
}