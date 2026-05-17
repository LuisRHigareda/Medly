package mappers;

import dtos.AppointmentDTO;
import entities.Appointment;
import java.util.List;

/**
 *
 * @author Leonardo Flores Leyva - 252390
 */
public class AppointmentMapper {
    
    public static AppointmentDTO toDTO(Appointment entity){
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(entity.getId());
        dto.setPatientId(entity.getId());
        dto.setConsultingRoomId(entity.getId());
        dto.setDateTime(entity.getDateTime());
        return dto;
    }
    
    public static List<AppointmentDTO> toDTOList(List<Appointment> entities){
        return entities.stream().map(entity -> toDTO(entity)).toList();
    }
}