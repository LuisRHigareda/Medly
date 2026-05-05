package com.medical.userservice.repository;

import com.medical.userservice.model.Receptionist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceptionistRepository extends JpaRepository<Receptionist, Integer> {
}
