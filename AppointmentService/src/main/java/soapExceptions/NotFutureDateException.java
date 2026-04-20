package soapExceptions;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

/**
 *
 * @author Leonardo Flores Leyva - 252390
 */
@SoapFault(
        faultCode = FaultCode.CLIENT,
        faultStringOrReason = "The appointment's date must be set in the future!"
)
public class NotFutureDateException extends RuntimeException {

    /**
     * Creates a new instance of <code>NotFutureDateException</code> without
     * detail message.
     */
    public NotFutureDateException() {}

    /**
     * Constructs an instance of <code>NotFutureDateException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NotFutureDateException(String msg) {super(msg);}
}