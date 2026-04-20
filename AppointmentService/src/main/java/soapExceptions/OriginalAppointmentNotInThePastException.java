package soapExceptions;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

/**
 *
 * @author Leonardo Flores Leyva - 252390
 */
@SoapFault(
        faultCode = FaultCode.CLIENT,
        faultStringOrReason = "Appointments set in the past cannot be canceled!"
)
public class OriginalAppointmentNotInThePastException extends RuntimeException {

    /**
     * Creates a new instance of
     * <code>OriginalAppointmentNotInThePastException</code> without detail
     * message.
     */
    public OriginalAppointmentNotInThePastException() {}

    /**
     * Constructs an instance of
     * <code>OriginalAppointmentNotInThePastException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public OriginalAppointmentNotInThePastException(String msg) {super(msg);}
}