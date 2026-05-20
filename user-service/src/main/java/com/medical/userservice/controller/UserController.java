package com.medical.userservice.controller;
import com.medical.userservice.dto.ClinicResponse;
import com.medical.userservice.dto.ConsultingRoomResponse;
import com.medical.userservice.dto.DoctorResponse;
import com.medical.userservice.dto.PatientResponse;
import com.medical.userservice.dto.ReceptionistResponse;
import com.medical.userservice.service.UserQueryService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserQueryService userQueryService;

    public UserController(UserQueryService userQueryService) {
        this.userQueryService = userQueryService;
    }

    @GetMapping("/receptionists/{id}")
    public ResponseEntity<ReceptionistResponse> findReceptionistByUserIdOrId(@PathVariable Integer id) {
        return userQueryService.findReceptionistByUserIdOrId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/patients/{affiliationNumber}")
    public ResponseEntity<PatientResponse> findPatientByAffiliationNumber(@PathVariable String affiliationNumber) {
        return userQueryService.findPatientByAffiliationNumber(affiliationNumber)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponse>> findAllDoctors() {
        return ResponseEntity.ok(userQueryService.findAllDoctors());
    }

    @GetMapping("/doctors/{id}")
    public ResponseEntity<DoctorResponse> findDoctorById(@PathVariable Integer id) {
        return userQueryService.findDoctorById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @GetMapping("/clinics")
    public ResponseEntity<List<ClinicResponse>> findAllClinics(){
        return ResponseEntity.ok(userQueryService.findAllClinics());
    }
    
    @GetMapping("/consulting_room/{id}")
    public ResponseEntity<ConsultingRoomResponse> findConsultingRoomById(@PathVariable Integer id){
        ConsultingRoomResponse response = userQueryService.findConsultingRoomById(id);
        return (response != null) ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/consulting_room/clinic/{id}")
    public ResponseEntity<List<ConsultingRoomResponse>> findConsultingRoomsByClinic(@PathVariable Integer id){
        List<ConsultingRoomResponse> response = userQueryService.findConsultingRoomsByClinic(id);
        return (response != null) ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
    }
}