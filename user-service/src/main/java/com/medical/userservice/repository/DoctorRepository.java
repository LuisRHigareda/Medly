package com.medical.userservice.repository;

import com.medical.userservice.model.Doctor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    @Query("SELECT d FROM Doctor d WHERE d.consultingRoom.id = :consultingRoomId")
    Optional<Doctor> findByConsultingRoom(Integer consultingRoomId);
}