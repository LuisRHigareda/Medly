package com.medical.userservice.service;

import com.medical.userservice.dto.*;
import com.medical.userservice.model.*;
import com.medical.userservice.repository.ClinicRepository;
import com.medical.userservice.repository.ConsultingRoomRepository;
import com.medical.userservice.repository.DoctorRepository;
import com.medical.userservice.repository.PatientRepository;
import com.medical.userservice.repository.ReceptionistRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserQueryService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ReceptionistRepository receptionistRepository;
    private final ClinicRepository clinicRepository;
    private final ConsultingRoomRepository consultingRoomRepository;

    public UserQueryService(
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            ReceptionistRepository receptionistRepository,
            ClinicRepository clinicRepository,
            ConsultingRoomRepository consultingRoomRepository
    ) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.receptionistRepository = receptionistRepository;
        this.clinicRepository = clinicRepository;
        this.consultingRoomRepository = consultingRoomRepository;
    }

    public Optional<PatientResponse> findPatientById(Integer id) {
        return patientRepository.findById(id).map(this::toPatientResponse);
    }

    public Optional<PatientResponse> findPatientByAffiliationNumber(String affiliationNumber) {
        return patientRepository.findByAffiliationNumber(affiliationNumber).map(this::toPatientResponse);
    }

    public List<DoctorResponse> findAllDoctors() {
        return doctorRepository.findAll().stream().map(this::toDoctorResponse).toList();
    }

    public Optional<DoctorResponse> findDoctorById(Integer id) {
        return doctorRepository.findById(id).map(this::toDoctorResponse);
    }

    public Optional<ReceptionistResponse> findReceptionistById(Integer id) {
        return receptionistRepository.findById(id).map(this::toReceptionistResponse);
    }

    @Transactional
    public Optional<ReceptionistResponse> findReceptionistByUserIdOrId(Integer id) {
        return receptionistRepository.findByUserId(id)
                .or(() -> receptionistRepository.findById(id))
                .map(this::toReceptionistResponse);
    }

    public List<ClinicResponse> findAllClinics(){
        return clinicRepository.findAll().stream().map(this::toClinicResponse).toList();
    }
    
    public ConsultingRoomResponse findConsultingRoomById(Integer id) {
        ConsultingRoom consultingRoom = consultingRoomRepository.findById(id).orElse(null);
        if (consultingRoom != null) {
            Doctor doctor = doctorRepository.findByConsultingRoom(id).orElse(null);
            return toConsultingRoomResponse(consultingRoom, doctor);
        } else {
            return null;
        }
    }

    public List<ConsultingRoomResponse> findConsultingRoomsByClinic(Integer idClinic) {
        List<ConsultingRoom> consultingRooms = consultingRoomRepository.findByClinic(idClinic);
        if (consultingRooms != null && !consultingRooms.isEmpty()) {
            List<ConsultingRoomResponse> response = new ArrayList<>();
            for (ConsultingRoom room : consultingRooms) {
                // I know that a query for each entity isn't performance frendly, but
                // the consulting room's id is critical
                Doctor doctor = doctorRepository.findByConsultingRoom(room.getId()).orElse(null);
                response.add(toConsultingRoomResponse(room, doctor));
            }
            return response;
        } else
            return null;
    }

    public boolean patientExists(Integer id) {
        return patientRepository.existsById(id);
    }

    public boolean doctorExists(Integer id) {
        return doctorRepository.existsById(id);
    }

    private PatientResponse toPatientResponse(Patient patient) {
        AddressResponse addressResponse = null;
        Address address = patient.getAddress();
        if (address != null) {
            addressResponse = new AddressResponse(address.getId(), address.getStreet(), address.getCity(), address.getState());
        }

        return new PatientResponse(
                patient.getId(),
                patient.getUser().getId(),
                patient.getUser().getEmail(),
                patient.getName(),
                patient.getLastName(),
                patient.getPhone(),
                patient.getBirthday(),
                patient.getAffiliationNumber(),
                patient.getValidity(),
                addressResponse
        );
    }

    private DoctorResponse toDoctorResponse(Doctor doctor) {
        ConsultingRoom room = doctor.getConsultingRoom();
        Clinic clinic = room.getClinic();
        Specialization specialization = doctor.getSpecialization();

        return new DoctorResponse(
                doctor.getId(),
                doctor.getUser().getId(),
                doctor.getUser().getEmail(),
                doctor.getName(),
                doctor.getLastName(),
                doctor.getMedicalLicenseNumber(),
                specialization.getName(),
                room.getId(),
                room.getRoom(),
                clinic.getId(),
                clinic.getName()
        );
    }

    private ReceptionistResponse toReceptionistResponse(Receptionist receptionist) {
        Integer userId = null;
        String email = null;
        Integer clinicId = null;
        String clinicName = "Sin Clínica";

        if (receptionist.getUser() != null) {
            userId = receptionist.getUser().getId();
            email = receptionist.getUser().getEmail();
        }

        if (receptionist.getClinic() != null) {
            clinicId = receptionist.getClinic().getId();
            clinicName = receptionist.getClinic().getName();
        }

        return new ReceptionistResponse(
                receptionist.getId(),
                userId,
                email,
                receptionist.getName(),
                receptionist.getLastName(),
                clinicId,
                clinicName
        );
    }

    private ClinicResponse toClinicResponse(Clinic clinic){
        String street = null;
        String state = null;
        String city = null;
        
        Location location = clinic.getLocation();
        if(location != null){
            street = location.getStreet();
            state = location.getState();
            city = location.getCity();
        }
        
        return new ClinicResponse(
                clinic.getId(), 
                clinic.getName(), 
                street, 
                state, 
                city
        );
    }
    
    private ConsultingRoomResponse toConsultingRoomResponse(ConsultingRoom consultingRoom, Doctor doctor) {

        String specializationName = null;

        if (doctor != null) {
            Specialization specialization = doctor.getSpecialization();
            if (specialization != null) {
                specializationName = specialization.getName();
            }
        }

        return new ConsultingRoomResponse(
                consultingRoom.getId(),
                consultingRoom.getRoom(),
                String.format(
                        "%s %s",
                        (doctor != null) ? doctor.getName() : "Not",
                        (doctor != null) ? doctor.getLastName() : "available doctor"
                ),
                (specializationName != null) ? specializationName : "N/A"
        );
    }
}