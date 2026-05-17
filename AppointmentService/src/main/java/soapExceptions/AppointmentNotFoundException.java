package soapExceptions;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

/**
 *
 * @author Leonardo Flores Leyva - 252390
 */
@SoapFault(
        faultCode = FaultCode.CLIENT,
        faultStringOrReason = "Unexistent appointment!"
)
public class AppointmentNotFoundException extends RuntimeException {

    public AppointmentNotFoundException() {}

    public AppointmentNotFoundException(String message) {super(message);}
}