package soapExceptions;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

/**
 *
 * @author Leonardo Flores Leyva - 252390
 */
@SoapFault(
        faultCode = FaultCode.CLIENT,
        faultStringOrReason = "Unexisting consulting room!"
)
public class ConsultingRoomNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>ConsultingRoomException</code> without
     * detail message.
     */
    public ConsultingRoomNotFoundException() {}

    /**
     * Constructs an instance of <code>ConsultingRoomException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ConsultingRoomNotFoundException(String msg) {super(msg);}
}