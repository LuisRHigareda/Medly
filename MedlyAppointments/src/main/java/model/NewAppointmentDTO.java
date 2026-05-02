package model;

import java.time.LocalDateTime;

/**
 *
 * @author Leonardo Flores Leyva
 */
public class NewAppointmentDTO {
    
    private Integer patientId;
    
    private Integer consultingRoomId;
    
    private LocalDateTime dateTime;

    public NewAppointmentDTO() {}

    public Integer getPatientId() {return patientId;}

    public Integer getConsultingRoomId() {return consultingRoomId;}

    public LocalDateTime getDateTime() {return dateTime;}

    public void setPatientId(Integer patientId) {this.patientId = patientId;}

    public void setConsultingRoomId(Integer consultingRoomId) {this.consultingRoomId = consultingRoomId;}

    public void setDateTime(LocalDateTime dateTime) {this.dateTime = dateTime;}
}