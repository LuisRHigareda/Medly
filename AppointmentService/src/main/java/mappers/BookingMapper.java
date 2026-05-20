package mappers;

import dtos.BookingDTO;
import entities.Booking;
import java.util.List;

/**
 *
 * @author Leonardo Flores Leyva - 252390
 */
public class BookingMapper {
    
    public static BookingDTO toDTO(Booking entity){
        BookingDTO dto = new BookingDTO();
        dto.setId(entity.getId());
        dto.setPatientId(entity.getPatientId());
        dto.setReferenceNumber(entity.getReferenceNumber());
        dto.setStatus(entity.getStatus());
        dto.setAppointment(AppointmentMapper.toDTO(entity.getAppointment()));
        return dto;
    }
    
    public static List<BookingDTO> toDTO(List<Booking> entities){
        return entities.stream().map(entity -> toDTO(entity)).toList();
    }
}