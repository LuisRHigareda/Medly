package dtos;

import java.time.LocalDateTime;

/**
 *
 * @author Leonardo Flores Leyva - 252390
 */
public class AppointmentDTO {
    
    private Integer id;
    
    private Integer patientId;
    
    private Integer consultingRoomId;
    
    private LocalDateTime dateTime;

    public AppointmentDTO() {}

    public AppointmentDTO(
            Integer id, 
            Integer patientId, 
            Integer consultingRoomId, 
            LocalDateTime dateTime
    ) {
        this.id = id;
        this.patientId = patientId;
        this.consultingRoomId = consultingRoomId;
        this.dateTime = dateTime;
    }

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public Integer getPatientId() {return patientId;}

    public void setPatientId(Integer patientId) {this.patientId = patientId;}

    public Integer getConsultingRoomId() {return consultingRoomId;}

    public void setConsultingRoomId(Integer consultingRoomId) {this.consultingRoomId = consultingRoomId;}

    public LocalDateTime getDateTime() {return dateTime;}

    public void setDateTime(LocalDateTime dateTime) {this.dateTime = dateTime;}
}