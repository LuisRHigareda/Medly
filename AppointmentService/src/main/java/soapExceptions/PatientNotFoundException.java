package soapExceptions;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

/**
 *
 * @author Leonardo Flores Leyva - 252390
 */
@SoapFault(
        faultCode = FaultCode.CLIENT,
        faultStringOrReason = "Unexisting patient!"
)
public class PatientNotFoundException extends RuntimeException {

    /**
     * Creates a new instance of <code>PatientNotFoundException</code> without
     * detail message.
     */
    public PatientNotFoundException() {}

    /**
     * Constructs an instance of <code>PatientNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PatientNotFoundException(String msg) {super(msg);}
}