package repositories;

import entities.Booking;
import enums.BookingStatus;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Database operations with bookings and their medical appointments.
 * @author Leonardo Flores Leyva - 252390
 */
public interface AppointmentRepository extends JpaRepository<Booking, Integer> {
    public Booking findByReferenceNumber(Integer referenceNumber);
    // Queries based on the patient's id
    public List<Booking> findByPatientId(Integer patientId);
    @Query("SELECT b FROM Booking b WHERE b.patientId = :patientId AND b.status = :status")
    public List<Booking> findByPatientStatus(Integer patientId, BookingStatus status);
    @Query("SELECT b FROM Booking b WHERE b.patientId = :patientId AND FUNCTION('DATE', b.appointment.dateTime) = :date")
    public List<Booking> findByPatientDate(Integer patientId, LocalDate date);
    @Query("SELECT b FROM Booking b WHERE b.patientId = :patientId AND FUNCTION('DATE', b.appointment.dateTime) = :date AND b.status = :status")
    public List<Booking> findByPatientDateStatus(Integer patientId, LocalDate date, BookingStatus status);
    @Query("SELECT b FROM Booking b WHERE b.patientId = :patientId AND (FUNCTION('DATE', b.appointment.dateTime) = :date OR b.status = :status)")
    public List<Booking> findByFilterPatient(Integer patientId, LocalDate date, BookingStatus status);
    // Queries based on the consulting room's id
    @Query("SELECT b FROM Booking b WHERE b.appointment.consultingRoomId = :consultingRoomId")
    public List<Booking> findByConsultingRoomId(Integer consultingRoomId);
    @Query("SELECT b FROM Booking b WHERE b.appointment.consultingRoomId = :consultingRoomId AND b.status = :status")
    public List<Booking> findByConsultingRoomStatus(Integer consultingRoomId, BookingStatus status);
    @Query("SELECT b FROM Booking b WHERE b.appointment.consultingRoomId = :consultingRoomId AND FUNCTION('DATE', b.appointment.dateTime) = :date")
    public List<Booking> findByConsultingRoomDate(Integer consultingRoomId, LocalDate date);
    @Query("SELECT b FROM Booking b WHERE b.appointment.consultingRoomId = :consultingRoomId AND FUNCTION('DATE', b.appointment.dateTime) = :date AND b.status = :status")
    public List<Booking> findByConsultingRoomDateStatus(Integer consultingRoomId, LocalDate date, BookingStatus status);
    @Query("SELECT b FROM Booking b WHERE b.appointment.consultingRoomId = :consultingRoomId AND (FUNCTION('DATE', b.appointment.dateTime) = :date OR b.status = :status)")
    public List<Booking> findByFilterConsultingRoom(Integer consultingRoomId, LocalDate date, BookingStatus status);
}