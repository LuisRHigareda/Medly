package entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author Leonardo Flores Leyva - 252390
 */
@Entity
@Table(name = "appointments")
public class Appointment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "patient_id", nullable = false, unique = true)
    private Integer patientId;
    
    @Column(name = "consulting_room_id", nullable = false, unique = true)
    private Integer consultingRoomId;
    
    @Column(name="date_time", nullable = false)
    private LocalDateTime dateTime;

    public Appointment() {}

    public Appointment(
            Integer patientId, 
            Integer consultingRoomId, 
            LocalDateTime dateTime
    ) {
        this.patientId = patientId;
        this.consultingRoomId = consultingRoomId;
        this.dateTime = dateTime;
    }

    public Appointment(
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