package model;

/**
 *
 * @author Leonardo Flores Leyva - 252390
 */
public class BookingDTO {
    private Integer id;
    
    private Integer patientId;
    
    private AppointmentDTO appointment;
    
    private Long referenceNumber;
    
    private String status;

    public BookingDTO() {}

    public BookingDTO(
            Integer id, 
            Integer patientId, 
            AppointmentDTO appointment, 
            Long referenceNumber, 
            String status
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

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}
}