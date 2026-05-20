package restControllers;

import dtos.BookingDTO;
import enums.BookingStatus;
import interfaces.IAppointmentService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Retrieves bookings and their associated appointments.
 * @author Leonardo Flores Leyva - 252390
 */
@RestController
@RequestMapping("api/appointments/bookings")
public class AppointmentController {
    
    private final IAppointmentService appointmentService;

    public AppointmentController(IAppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
    
    @GetMapping
    public List<BookingDTO> findAll(){
        return appointmentService.getAllBookings();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> findById(@PathVariable Integer id){
        return ResponseEntity.ok(appointmentService.getBookingById(id));
    }
    
    @GetMapping("/reference_number/{reference}")
    public ResponseEntity<BookingDTO> findByReferenceNumber(@PathVariable Long reference){
        return ResponseEntity.ok(appointmentService.getBookingByReferenceNumber(reference));
    }
    
    @GetMapping("/patient/{id}")
    public ResponseEntity<List<BookingDTO>> findByPatient(@PathVariable Integer id){
        List<BookingDTO> appointments = appointmentService.getBookingsByPatient(id);
        return (!appointments.isEmpty()) ? ResponseEntity.ok(appointments) : ResponseEntity.ok().build();
    }
    
    @GetMapping("/patient/status")
    public ResponseEntity<List<BookingDTO>> findByPatientSatus(
            @RequestParam(name = "status", required = true) BookingStatus status, 
            @RequestParam(name = "id", required = true) Integer patientId
    ){
        List<BookingDTO> appointments = appointmentService.getBookingsByPatientStatus(patientId, status);
        return (!appointments.isEmpty()) ? ResponseEntity.ok(appointments) : ResponseEntity.ok().build();
    }
    
    @GetMapping("/patient/date")
    public ResponseEntity<List<BookingDTO>> findByPatientDate(
            @RequestParam(name = "date", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, 
            @RequestParam(name = "id", required = true) Integer patientId
    ){
        List<BookingDTO> appointments = appointmentService.getBookingsByPatientDate(patientId, date);
        return (!appointments.isEmpty()) ? ResponseEntity.ok(appointments) : ResponseEntity.ok().build();
    }
    
    @GetMapping("/patient/status/date")
    public ResponseEntity<List<BookingDTO>> findByPatientStatusDate(
            @RequestParam(name = "date", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(name = "status", required = true) BookingStatus status, 
            @RequestParam(name = "id", required = true) Integer patientId
    ){
        List<BookingDTO> appointments = appointmentService.getBookingsByPatientStatusDate(patientId, status, date);
        return (!appointments.isEmpty()) ? ResponseEntity.ok(appointments) : ResponseEntity.ok().build();
    }
    
    @GetMapping("/patient/filter")
    public ResponseEntity<List<BookingDTO>> findByPatientFilter(
            @RequestParam(name = "date", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(name = "status", required = true) BookingStatus status, 
            @RequestParam(name = "id", required = true) Integer patientId
    ){
        List<BookingDTO> appointments = appointmentService.getBookingsByFilterPatient(patientId, status, date);
        return (!appointments.isEmpty()) ? ResponseEntity.ok(appointments) : ResponseEntity.ok().build();
    }
    
    @GetMapping("/consulting_room/{id}")
    public ResponseEntity<List<BookingDTO>> findByConsultingRoom(@PathVariable Integer id){
        List<BookingDTO> appointments = appointmentService.getBookingsByConsultingRoom(id);
        return (!appointments.isEmpty()) ? ResponseEntity.ok(appointments) : ResponseEntity.ok().build();
    }
    
    @GetMapping("/consulting_room/status")
    public ResponseEntity<List<BookingDTO>> findByConsultingRoomSatus(
            @RequestParam(name = "status", required = true) BookingStatus status, 
            @RequestParam(name = "id", required = true) Integer patientId
    ){
        List<BookingDTO> appointments = appointmentService.getBookingsByConsultingRoomStatus(patientId, status);
        return (!appointments.isEmpty()) ? ResponseEntity.ok(appointments) : ResponseEntity.ok().build();
    }
    
    @GetMapping("/consulting_room/date")
    public ResponseEntity<List<BookingDTO>> findByConsultingRoomDate(
            @RequestParam(name = "date", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, 
            @RequestParam(name = "id", required = true) Integer patientId
    ){
        List<BookingDTO> appointments = appointmentService.getBookingsByConsultingRoomDate(patientId, date);
        return (!appointments.isEmpty()) ? ResponseEntity.ok(appointments) : ResponseEntity.ok().build();
    }
    
    @GetMapping("/consulting_room/status/date")
    public ResponseEntity<List<BookingDTO>> findByConsultingRoomStatusDate(
            @RequestParam(name = "date", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(name = "status", required = true) BookingStatus status, 
            @RequestParam(name = "id", required = true) Integer patientId
    ){
        List<BookingDTO> appointments = appointmentService.getBookingsByConsultingRoomStatusDate(patientId, status, date);
        return (!appointments.isEmpty()) ? ResponseEntity.ok(appointments) : ResponseEntity.ok().build();
    }
    
    @GetMapping("/consulting_room/filter")
    public ResponseEntity<List<BookingDTO>> findByConsultingRoomFilter(
            @RequestParam(name = "date", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(name = "status", required = true) BookingStatus status, 
            @RequestParam(name = "id", required = true) Integer patientId
    ){
        List<BookingDTO> appointments = appointmentService.getBookingsByFilterConsultingRoom(patientId, status, date);
        return (!appointments.isEmpty()) ? ResponseEntity.ok(appointments) : ResponseEntity.ok().build();
    }
}