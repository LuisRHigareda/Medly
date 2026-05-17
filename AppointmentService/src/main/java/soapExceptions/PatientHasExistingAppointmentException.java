package soapExceptions;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

/**
 *
 * @author Leonardo Flores Leyva
 */
@SoapFault(
        faultCode = FaultCode.CLIENT,
        faultStringOrReason = "The patient has another existing appointment in the same given date and time."
)
public class PatientHasExistingAppointmentException extends RuntimeException {

    public PatientHasExistingAppointmentException() {}

    public PatientHasExistingAppointmentException(String message) {super(message);}
    
}