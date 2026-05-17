package soapExceptions;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

/**
 *
 * @author Leonardo Flores Leyva - 252390
 */
@SoapFault(
        faultCode = FaultCode.CLIENT,
        faultStringOrReason = "The original booking has already been either confirmed, canceled, or missed!"
)
public class NotUpdatableAppointmentException extends RuntimeException {

    public NotUpdatableAppointmentException() {}

    public NotUpdatableAppointmentException(String message) {super(message);}
}