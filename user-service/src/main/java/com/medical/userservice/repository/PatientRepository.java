package com.medical.userservice.repository;

import com.medical.userservice.model.Patient;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Optional<Patient> findByAffiliationNumber(String affiliationNumber);
}
