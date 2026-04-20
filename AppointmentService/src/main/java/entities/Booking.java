package entities;

import enums.BookingStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author Leonardo Flores Leyva - 252390
 */
@Entity
@Table(name = "bookings")
public class Booking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "patient_id", nullable = false, unique = true)
    private Integer patientId;
    
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_appointment", nullable = false, unique = true)
    private Appointment appointment;
    
    @Column(name = "reference_number", nullable = false, unique = true)
    private Long referenceNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    public Booking() {}

    public Booking(
            Integer patientId, 
            Appointment appointment, 
            Long referenceNumber, 
            BookingStatus status
    ) {
        this.patientId = patientId;
        this.appointment = appointment;
        this.referenceNumber = referenceNumber;
        this.status = status;
    }
    
    public Booking(
            Integer id, 
            Integer patientId, 
            Appointment appointment, 
            Long referenceNumber, 
            BookingStatus status
    ) {
        this.id = id;
        this.patientId = patientId;
        this.appointment = appointment;
        this.referenceNumber = referenceNumber;
        this.status = status;
    }

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public Integer getPatientId() {return patientId;}

    public void setPatientId(Integer patientId) {this.patientId = patientId;}

    public Appointment getAppointment() {return appointment;}

    public void setAppointment(Appointment appointment) {this.appointment = appointment;}

    public Long getReferenceNumber() {return referenceNumber;}

    public void setReferenceNumber(Long referenceNumber) {this.referenceNumber = referenceNumber;}

    public BookingStatus getStatus() {return status;}

    public void setStatus(BookingStatus status) {this.status = status;}    
}