package com.medical.userservice.repository;

import com.medical.userservice.model.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Leonardo Flores Leyva
 */
public interface ClinicRepository extends JpaRepository<Clinic, Integer>{
    
}