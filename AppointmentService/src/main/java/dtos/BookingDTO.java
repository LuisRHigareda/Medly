package dtos;

import enums.BookingStatus;

/**
 *
 * @author Leonardo Flores Leyva - 252390
 */
public class BookingDTO {
    private Integer id;
    
    private Integer patientId;
    
    private AppointmentDTO appointment;
    
    private Long referenceNumber;
    
    private BookingStatus status;

    public BookingDTO() {}

    public BookingDTO(
            Integer id, 
            Integer patientId, 
            AppointmentDTO appointment, 
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

    public AppointmentDTO getAppointment() {return appointment;}

    public void setAppointment(AppointmentDTO appointment) {this.appointment = appointment;}

    public Long getReferenceNumber() {return referenceNumber;}

    public void setReferenceNumber(Long referenceNumber) {this.referenceNumber = referenceNumber;}

    public BookingStatus getStatus() {return status;}

    public void setStatus(BookingStatus status) {this.status = status;}
}