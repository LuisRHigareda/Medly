package interfaces;

import dtos.BookingDTO;
import enums.BookingStatus;
import java.time.LocalDate;
import java.util.List;

/**
 * Appointment Service contract.
 * @author Leonardo Flores Leyva - 252390
 */
public interface IAppointmentService {
    
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