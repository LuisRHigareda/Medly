package com.medical.userservice.service;

import com.medical.userservice.dto.*;
import com.medical.userservice.model.*;
import com.medical.userservice.repository.DoctorRepository;
import com.medical.userservice.repository.PatientRepository;
import com.medical.userservice.repository.ReceptionistRepository;
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

    public UserQueryService(PatientRepository patientRepository, DoctorRepository doctorRepository,
                            ReceptionistRepository receptionistRepository) {
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.receptionistRepository = receptionistRepository;
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
}
