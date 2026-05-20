package com.medical.userservice.repository;

import com.medical.userservice.model.ConsultingRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Leonardo Flores Leyva
 */
public interface ConsultingRoomRepository extends JpaRepository<ConsultingRoom, Integer>{
    @Query("SELECT cr FROM ConsultingRoom cr WHERE cr.clinic.id = :idClinica")
    List<ConsultingRoom> findByClinic(Integer idClinica);
}