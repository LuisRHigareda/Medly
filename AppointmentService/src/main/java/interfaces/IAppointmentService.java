package interfaces;

import dtos.BookingDTO;
import enums.BookingStatus;
import java.time.LocalDate;
import java.util.List;
import mx.edu.itson.soap.appointments.AddAppointmentRequest;
import mx.edu.itson.soap.appointments.AddAppointmentResponse;
import mx.edu.itson.soap.appointments.CancelAppointmentRequest;
import mx.edu.itson.soap.appointments.CancelAppointmentResponse;
import mx.edu.itson.soap.appointments.ConfirmAppointmentRequest;
import mx.edu.itson.soap.appointments.ConfirmAppointmentResponse;

/**
 * Appointment Service contract.
 * @author Leonardo Flores Leyva - 252390
 */
public interface IAppointmentService {
    // Operations
    public AddAppointmentResponse addAppointment(AddAppointmentRequest request);
    public ConfirmAppointmentResponse confirmAppointment(ConfirmAppointmentRequest request);
    public CancelAppointmentResponse cancelAppointment(CancelAppointmentRequest request);
    // Queries
    public BookingDTO getBookingById(Integer id);
    public BookingDTO getBookingByReferenceNumber(Integer reference);
    public List<BookingDTO> getAllBookings();
    public List<BookingDTO> getBookingsByPatient(Integer patientId);
    public List<BookingDTO> getBookingsByPatientStatus(Integer patientId, BookingStatus status);
    public List<BookingDTO> getBookingsByPatientDate(Integer patientId, LocalDate date);
    public List<BookingDTO> getBookingsByPatientStatusDate(Integer patientId, BookingStatus status, LocalDate date);
    public List<BookingDTO> getBookingsByFilterPatient(
            Integer patientId, 
            BookingStatus status, 
            LocalDate date
    );
    public List<BookingDTO> getBookingsByConsultingRoom(Integer consultingRoomId);
    public List<BookingDTO> getBookingsByConsultingRoomStatus(Integer consultingRoomId, BookingStatus status);
    public List<BookingDTO> getBookingsByConsultingRoomDate(Integer consultingRoomId, LocalDate date);
    public List<BookingDTO> getBookingsByConsultingRoomStatusDate(Integer consultingRoomId, BookingStatus status, LocalDate date);
    public List<BookingDTO> getBookingsByFilterConsultingRoom(
            Integer consultingRoomId, 
            BookingStatus status, 
            LocalDate date
    );
}