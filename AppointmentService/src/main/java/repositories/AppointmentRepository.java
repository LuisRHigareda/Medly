package repositories;

import entities.Booking;
import enums.BookingStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Database operations with bookings and their medical appointments.
 * @author Leonardo Flores Leyva - 252390
 */
public interface AppointmentRepository extends JpaRepository<Booking, Integer> {
    /**
     * 
     * @param referenceNumber
     * @return 
     */
    public Optional<Booking> findByReferenceNumber(Long referenceNumber);
    // Queries based on the patient's id
    /**
     * 
     * @param patientId
     * @param dateTime
     * @return 
     */
    @Query("SELECT b FROM Booking b WHERE b.patientId = :patientId AND b.appointment.dateTime = :dateTime")
    public Optional<Booking> findByPatientIdDateTime(Integer patientId, LocalDateTime dateTime);
    /**
     * 
     * @param patientId
     * @return 
     */
    public List<Booking> findByPatientId(Integer patientId);
    /**
     * 
     * @param patientId
     * @param status
     * @return 
     */
    @Query("SELECT b FROM Booking b WHERE b.patientId = :patientId AND b.status = :status")
    public List<Booking> findByPatientStatus(Integer patientId, BookingStatus status);
    /**
     * 
     * @param patientId
     * @param date
     * @return 
     */
    @Query("SELECT b FROM Booking b WHERE b.patientId = :patientId AND FUNCTION('DATE', b.appointment.dateTime) = :date")
    public List<Booking> findByPatientDate(Integer patientId, LocalDate date);
    /**
     * 
     * @param patientId
     * @param date
     * @param status
     * @return 
     */
    @Query("SELECT b FROM Booking b WHERE b.patientId = :patientId AND FUNCTION('DATE', b.appointment.dateTime) = :date AND b.status = :status")
    public List<Booking> findByPatientDateStatus(Integer patientId, LocalDate date, BookingStatus status);
    /**
     * 
     * @param patientId
     * @param date
     * @param status
     * @return 
     */
    @Query("SELECT b FROM Booking b WHERE b.patientId = :patientId AND (FUNCTION('DATE', b.appointment.dateTime) = :date OR b.status = :status)")
    public List<Booking> findByFilterPatient(Integer patientId, LocalDate date, BookingStatus status);
    
    // Queries based on the consulting room's id
    /**
     * 
     * @param consultingRoomId
     * @param dateTime
     * @return 
     */
    @Query("SELECT b FROM Booking b WHERE b.appointment.consultingRoomId = :consultingRoomId AND b.appointment.dateTime = :dateTime")
    public Optional<Booking> findByConsultingRoomDateTime(Integer consultingRoomId, LocalDateTime dateTime);
    /**
     * 
     * @param consultingRoomId
     * @return 
     */
    @Query("SELECT b FROM Booking b WHERE b.appointment.consultingRoomId = :consultingRoomId")
    public List<Booking> findByConsultingRoomId(Integer consultingRoomId);
    /**
     * 
     * @param consultingRoomId
     * @param status
     * @return 
     */
    @Query("SELECT b FROM Booking b WHERE b.appointment.consultingRoomId = :consultingRoomId AND b.status = :status")
    public List<Booking> findByConsultingRoomStatus(Integer consultingRoomId, BookingStatus status);
    /**
     * 
     * @param consultingRoomId
     * @param date
     * @return 
     */
    @Query("SELECT b FROM Booking b WHERE b.appointment.consultingRoomId = :consultingRoomId AND FUNCTION('DATE', b.appointment.dateTime) = :date")
    public List<Booking> findByConsultingRoomDate(Integer consultingRoomId, LocalDate date);
    /**
     * 
     * @param consultingRoomId
     * @param date
     * @param status
     * @return 
     */
    @Query("SELECT b FROM Booking b WHERE b.appointment.consultingRoomId = :consultingRoomId AND FUNCTION('DATE', b.appointment.dateTime) = :date AND b.status = :status")
    public List<Booking> findByConsultingRoomDateStatus(Integer consultingRoomId, LocalDate date, BookingStatus status);
    /**
     * 
     * @param consultingRoomId
     * @param date
     * @param status
     * @return 
     */
    @Query("SELECT b FROM Booking b WHERE b.appointment.consultingRoomId = :consultingRoomId AND (FUNCTION('DATE', b.appointment.dateTime) = :date OR b.status = :status)")
    public List<Booking> findByFilterConsultingRoom(Integer consultingRoomId, LocalDate date, BookingStatus status);
}