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
import soapExceptions.ConsultingRoomHasExistingAppointmentException;
import soapExceptions.NotUpdatableAppointmentException;
import soapExceptions.PatientHasExistingAppointmentException;
import soapExceptions.AppointmentNotFoundException;
import util.ReferenceMaker;
import util.TimeValidator;

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
        
        // Formats the date and time of the request to a LocalDateTime object
        XMLGregorianCalendar requestDateTime = request.getDateTime();
        LocalDateTime dateTime = LocalDateTime.of(
                requestDateTime.getDay(),
                requestDateTime.getMonth(), 
                requestDateTime.getDay(), 
                requestDateTime.getHour(), 
                requestDateTime.getMinute(), 
                requestDateTime.getSecond()
        );
        
        // Checks whether the patient has another appointment with the same given date and time
        Booking existingBookingPatient = appointmentRepository.findByPatientIdDateTime(request.getPatientId(), dateTime).orElse(null);
        if(existingBookingPatient != null) {throw new PatientHasExistingAppointmentException();}
        
        // Checks whether there is another appointment with the same given date and time associated to the consulting room
        Booking existingBookingConsultingRoom = appointmentRepository.findByConsultingRoomDateTime(request.getConsultingRoomId(), dateTime).orElse(null);
        if(existingBookingConsultingRoom != null) {throw new ConsultingRoomHasExistingAppointmentException();}
        
        // Checks if the given time is valid
        TimeValidator.verifyTime(dateTime.toLocalTime());
        
        // Arranges the appointment entity with the request's information
        Appointment appointment = new Appointment();
        appointment.setDateTime(dateTime);
        appointment.setPatientId(request.getPatientId());
        appointment.setConsultingRoomId(request.getConsultingRoomId());
        // Arranges the booking entity with the request's information
        Booking booking = new Booking();
        booking.setAppointment(appointment);
        booking.setPatientId(request.getPatientId());
        booking.setReferenceNumber(ReferenceMaker.generateReferenceNumber());
        booking.setStatus(BookingStatus.TO_BE_CONFIRMED);
        
        try {
            // Attempts to create a current datetime XMLGregorianCalendar before any database operation
            GregorianCalendar gregorianCalendar = GregorianCalendar.from(ZonedDateTime.now());
            XMLGregorianCalendar dateCreated = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
            
            // Stores the new booking and its related appointment onto the database
            booking = appointmentRepository.save(booking);
                        
            // Creates and returns a response with its corresponding information
            AddAppointmentResponse response = new AddAppointmentResponse();
            response.setAppointmentCreatedDateTime(dateCreated);
            response.setReferenceNumber(booking.getReferenceNumber());
            response.setSuccessMessage("Success!");
            return response;
        } 
        catch (DatatypeConfigurationException ex) {throw new RuntimeException("This shouldn't happened!");} 
        catch(Exception e){throw new RuntimeException("Unexpected error!");}
    }

    @Override
    public ConfirmAppointmentResponse confirmAppointment(ConfirmAppointmentRequest request) {
        // Attempts to obtain the original booking based on the id
        Booking originalBooking = appointmentRepository.findById(request.getAppointmentId()).orElse(null);
        
        // Verifies if there's an existing booking to be confirmed based on the receiving id
        if(originalBooking == null) {throw new AppointmentNotFoundException();}
        
        // Verifies if the original booking has not been confirmed, canceled, or missed
        if(originalBooking.getStatus() != BookingStatus.TO_BE_CONFIRMED) {throw new NotUpdatableAppointmentException();}
        
        // Sets the booking status to "CONFIRMED"
        originalBooking.setStatus(BookingStatus.CONFIRMED);
        
        // Updates the booking in the database
        try {appointmentRepository.save(originalBooking);} 
        catch (Exception e) {throw new RuntimeException("Unexpected error!");}
        
        // Creates and returns a response with a success message
        ConfirmAppointmentResponse response = new ConfirmAppointmentResponse();
        response.setSuccessMessage("Appointment confirmed!");
        return response;
    }

    @Override
    public CancelAppointmentResponse cancelAppointment(CancelAppointmentRequest request) {
        // Attempts to obtain the original booking based on the id
        Booking originalBooking = appointmentRepository.findById(request.getAppointmentId()).orElse(null);
        
        // Verifies if there's an existing booking to be confirmed based on the receiving id
        if(originalBooking == null) {throw new AppointmentNotFoundException();}
        
        // Verifies if the original booking has not been confirmed, canceled, or missed
        if(originalBooking.getStatus() != BookingStatus.TO_BE_CONFIRMED) {throw new NotUpdatableAppointmentException();}
        
        // Sets the booking status to "CANCELED"
        originalBooking.setStatus(BookingStatus.CANCELED);
        
        // Updates the booking in the database
        try {appointmentRepository.save(originalBooking);} 
        catch (Exception e) {throw new RuntimeException("Unexpected error!");}
        
        // Creates and returns a response with a success message
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