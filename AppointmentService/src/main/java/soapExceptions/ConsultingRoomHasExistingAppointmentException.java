package soapExceptions;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

/**
 *
 * @author Leonardo Flores Leyva - 252390
 */
@SoapFault(
        faultCode = FaultCode.CLIENT,
        faultStringOrReason = "There's an existing appointment with the same given date and time in the consulting room."
)
public class ConsultingRoomHasExistingAppointmentException extends RuntimeException {
    
    public ConsultingRoomHasExistingAppointmentException() {}
    
    public ConsultingRoomHasExistingAppointmentException(String message) {super(message);}
}