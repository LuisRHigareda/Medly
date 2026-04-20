package services;

import dtos.BookingDTO;
import entities.Appointment;
import entities.Booking;
import enums.BookingStatus;
import interfaces.IAppointmentService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import mx.edu.itson.soap.appointments.AddAppointmentRequest;
import mx.edu.itson.soap.appointments.AddAppointmentResponse;
import mx.edu.itson.soap.appointments.CancelAppointmentRequest;
import mx.edu.itson.soap.appointments.CancelAppointmentResponse;
import mx.edu.itson.soap.appointments.ConfirmAppointmentRequest;
import mx.edu.itson.soap.appointments.ConfirmAppointmentResponse;
import org.springframework.stereotype.Service;
import repositories.AppointmentRepository;
import util.GeneradorReferencia;

/**
 *
 * @author Leonardo Flores Leyva - 252390
 */
@Service
public class AppointmentService implements IAppointmentService{
    
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public AddAppointmentResponse addAppointment(AddAppointmentRequest request) {
        
        // Happy path temporal implementation
        Appointment appointment = new Appointment();
        
        XMLGregorianCalendar requestDateTime = request.getDateTime();
        LocalDateTime dateTime = LocalDateTime.of(
                requestDateTime.getDay(),
                requestDateTime.getMonth(), 
                requestDateTime.getDay(), 
                requestDateTime.getHour(), 
                requestDateTime.getMinute(), 
                requestDateTime.getSecond()
        );
        
        appointment.setDateTime(dateTime);
        appointment.setPatientId(request.getPatientId());
        appointment.setConsultingRoomId(request.getConsultingRoomId());
        
        Booking booking = new Booking();
        booking.setAppointment(appointment);
        booking.setPatientId(request.getPatientId());
        booking.setReferenceNumber(GeneradorReferencia.generarFolioUnico());
        booking.setStatus(BookingStatus.TO_BE_CONFIRMED);
        
        booking = appointmentRepository.save(booking);
        
        AddAppointmentResponse response = new AddAppointmentResponse();
        try {
            
            GregorianCalendar gregorianCalendar = GregorianCalendar.from(ZonedDateTime.now());
            
            response.setAppointmentCreatedDateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
            response.setReferenceNumber(booking.getReferenceNumber());
            response.setSuccessMessage("Success!");
            
            return response;
            
        } catch (DatatypeConfigurationException ex) {
            throw new RuntimeException("Something went wrong haha!");
        }
    }

    @Override
    public ConfirmAppointmentResponse confirmAppointment(ConfirmAppointmentRequest request) {
        Booking originalBooking = appointmentRepository.findById(request.getAppointmentId()).orElse(null);
        if(originalBooking == null)
            throw new RuntimeException("Unexistent appointment!");
        
        originalBooking.setStatus(BookingStatus.CONFIRMED);
        appointmentRepository.save(originalBooking);
        
        ConfirmAppointmentResponse response = new ConfirmAppointmentResponse();
        response.setSuccessMessage("Appointment confirmed!");
        return response;
    }

    @Override
    public CancelAppointmentResponse cancelAppointment(CancelAppointmentRequest request) {
        Booking originalBooking = appointmentRepository.findById(request.getAppointmentId()).orElse(null);
        if(originalBooking == null)
            throw new RuntimeException("Unexistent appointment!");
        
        originalBooking.setStatus(BookingStatus.CANCELED);
        appointmentRepository.save(originalBooking);
        
        CancelAppointmentResponse response = new CancelAppointmentResponse();
        response.setSuccessMessage("Appointment canceled!");
        return response;
    }

    @Override
    public BookingDTO getBookingById(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public BookingDTO getBookingByReferenceNumber(Integer reference) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<BookingDTO> getAllBookings() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<BookingDTO> getBookingsByPatient(Integer patientId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<BookingDTO> getBookingsByPatientStatus(Integer patientId, BookingStatus status) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<BookingDTO> getBookingsByPatientDate(Integer patientId, LocalDate date) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<BookingDTO> getBookingsByPatientStatusDate(Integer patientId, BookingStatus status, LocalDate date) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<BookingDTO> getBookingsByFilterPatient(Integer patientId, BookingStatus status, LocalDate date) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<BookingDTO> getBookingsByConsultingRoom(Integer consultingRoomId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<BookingDTO> getBookingsByConsultingRoomStatus(Integer consultingRoomId, BookingStatus status) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<BookingDTO> getBookingsByConsultingRoomDate(Integer consultingRoomId, LocalDate date) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<BookingDTO> getBookingsByConsultingRoomStatusDate(Integer consultingRoomId, BookingStatus status, LocalDate date) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<BookingDTO> getBookingsByFilterConsultingRoom(Integer consultingRoomId, BookingStatus status, LocalDate date) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
}