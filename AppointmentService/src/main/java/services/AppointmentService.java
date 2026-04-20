package services;

import dtos.BookingDTO;
import entities.Appointment;
import entities.Booking;
import enums.BookingStatus;
import interfaces.IAppointmentService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import mappers.BookingMapper;
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
import soapExceptions.NotFutureDateException;
import soapExceptions.OriginalAppointmentNotInThePastException;
import util.ReferenceMaker;
import util.TimeValidator;
import util.XMLDateFormatter;

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
        // Retrieves the patient's id
        Integer patientId = request.getPatientId();
        // Patient validation pending. UserService not created yet...
        
        // Retrieves the consulting room's id
        Integer consultingRoomId = request.getConsultingRoomId();
        // Consulting room validation pending. UserService not created yet...
        
        // Formats the date and time of the request to a LocalDateTime object
        LocalDateTime dateTime = XMLDateFormatter.toLocalDateTime(request.getDateTime());
        
        // Checks if the appointment's date is in the future
        if(!dateTime.isAfter(LocalDateTime.now())) {throw new NotFutureDateException();}
        
        // Checks if the given time is valid
        TimeValidator.verifyTime(dateTime.toLocalTime());
        
        // Checks whether the patient has another appointment with the same given date and time
        Booking existingBookingPatient = appointmentRepository.findByPatientIdDateTime(request.getPatientId(), dateTime).orElse(null);
        if(existingBookingPatient != null) {throw new PatientHasExistingAppointmentException();}
        
        // Checks whether there is another appointment with the same given date and time associated to the consulting room
        Booking existingBookingConsultingRoom = appointmentRepository.findByConsultingRoomDateTime(request.getConsultingRoomId(), dateTime).orElse(null);
        if(existingBookingConsultingRoom != null) {throw new ConsultingRoomHasExistingAppointmentException();}
        
        // Arranges the appointment entity with the request's information
        Appointment appointment = new Appointment(patientId, consultingRoomId, dateTime);
        // Arranges the booking entity with the request's information
        Booking booking = new Booking(patientId, appointment, ReferenceMaker.generateReferenceNumber(), BookingStatus.TO_BE_CONFIRMED);
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
        catch (DatatypeConfigurationException ex) {throw new RuntimeException("Unexpected error!");} 
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
        
        // Verifies that the retrieved appointment is still set in the past
        if(!originalBooking.getAppointment().getDateTime().isAfter(LocalDateTime.now()))
            throw new OriginalAppointmentNotInThePastException();
        
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
        Booking entity = appointmentRepository.findById(id).orElse(null);
        return (entity != null) ? BookingMapper.toDTO(entity) : null;
    }

    @Override
    public BookingDTO getBookingByReferenceNumber(Long reference) {
        Booking entity = appointmentRepository.findByReferenceNumber(reference).orElse(null);
        return (entity != null) ? BookingMapper.toDTO(entity) : null;
    }

    @Override
    public List<BookingDTO> getAllBookings() {
        List<Booking> entities = appointmentRepository.findAll();
        return (!entities.isEmpty()) ? entities.stream().map(entity -> BookingMapper.toDTO(entity)).toList() : new ArrayList<>();
    }

    @Override
    public List<BookingDTO> getBookingsByPatient(Integer patientId) {
        List<Booking> entities = appointmentRepository.findByPatientId(patientId);
        return (!entities.isEmpty()) ? entities.stream().map(entity -> BookingMapper.toDTO(entity)).toList() : new ArrayList<>();
    }

    @Override
    public List<BookingDTO> getBookingsByPatientStatus(Integer patientId, BookingStatus status) {
        List<Booking> entities = appointmentRepository.findByPatientStatus(patientId, status);
        return (!entities.isEmpty()) ? entities.stream().map(entity -> BookingMapper.toDTO(entity)).toList() : new ArrayList<>();
    }

    @Override
    public List<BookingDTO> getBookingsByPatientDate(Integer patientId, LocalDate date) {
        List<Booking> entities = appointmentRepository.findByPatientDate(patientId, date);
        return (!entities.isEmpty()) ? entities.stream().map(entity -> BookingMapper.toDTO(entity)).toList() : new ArrayList<>();
    }

    @Override
    public List<BookingDTO> getBookingsByPatientStatusDate(Integer patientId, BookingStatus status, LocalDate date) {
        List<Booking> entities = appointmentRepository.findByPatientDateStatus(patientId, date, status);
        return (!entities.isEmpty()) ? entities.stream().map(entity -> BookingMapper.toDTO(entity)).toList() : new ArrayList<>();
    }

    @Override
    public List<BookingDTO> getBookingsByFilterPatient(Integer patientId, BookingStatus status, LocalDate date) {
        List<Booking> entities = appointmentRepository.findByFilterPatient(patientId, date, status);
        return (!entities.isEmpty()) ? entities.stream().map(entity -> BookingMapper.toDTO(entity)).toList() : new ArrayList<>();
    }

    @Override
    public List<BookingDTO> getBookingsByConsultingRoom(Integer consultingRoomId) {
        List<Booking> entities = appointmentRepository.findByConsultingRoomId(consultingRoomId);
        return (!entities.isEmpty()) ? entities.stream().map(entity -> BookingMapper.toDTO(entity)).toList() : new ArrayList<>();
    }

    @Override
    public List<BookingDTO> getBookingsByConsultingRoomStatus(Integer consultingRoomId, BookingStatus status) {
        List<Booking> entities = appointmentRepository.findByConsultingRoomStatus(consultingRoomId, status);
        return (!entities.isEmpty()) ? entities.stream().map(entity -> BookingMapper.toDTO(entity)).toList() : new ArrayList<>();
    }

    @Override
    public List<BookingDTO> getBookingsByConsultingRoomDate(Integer consultingRoomId, LocalDate date) {
        List<Booking> entities = appointmentRepository.findByConsultingRoomDate(consultingRoomId, date);
        return (!entities.isEmpty()) ? entities.stream().map(entity -> BookingMapper.toDTO(entity)).toList() : new ArrayList<>();
    }

    @Override
    public List<BookingDTO> getBookingsByConsultingRoomStatusDate(Integer consultingRoomId, BookingStatus status, LocalDate date) {
        List<Booking> entities = appointmentRepository.findByConsultingRoomDateStatus(consultingRoomId, date, status);
        return (!entities.isEmpty()) ? entities.stream().map(entity -> BookingMapper.toDTO(entity)).toList() : new ArrayList<>();
    }

    @Override
    public List<BookingDTO> getBookingsByFilterConsultingRoom(Integer consultingRoomId, BookingStatus status, LocalDate date) {
        List<Booking> entities = appointmentRepository.findByFilterConsultingRoom(consultingRoomId, date, status);
        return (!entities.isEmpty()) ? entities.stream().map(entity -> BookingMapper.toDTO(entity)).toList() : new ArrayList<>();
    }
}