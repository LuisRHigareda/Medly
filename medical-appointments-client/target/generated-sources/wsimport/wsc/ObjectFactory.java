
package wsc;

import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the wsc package. 
 * <p>An ObjectFactory allows you to programmatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: wsc
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AddAppointmentRequest }
     * 
     * @return
     *     the new instance of {@link AddAppointmentRequest }
     */
    public AddAppointmentRequest createAddAppointmentRequest() {
        return new AddAppointmentRequest();
    }

    /**
     * Create an instance of {@link AddAppointmentResponse }
     * 
     * @return
     *     the new instance of {@link AddAppointmentResponse }
     */
    public AddAppointmentResponse createAddAppointmentResponse() {
        return new AddAppointmentResponse();
    }

    /**
     * Create an instance of {@link CancelAppointmentRequest }
     * 
     * @return
     *     the new instance of {@link CancelAppointmentRequest }
     */
    public CancelAppointmentRequest createCancelAppointmentRequest() {
        return new CancelAppointmentRequest();
    }

    /**
     * Create an instance of {@link CancelAppointmentResponse }
     * 
     * @return
     *     the new instance of {@link CancelAppointmentResponse }
     */
    public CancelAppointmentResponse createCancelAppointmentResponse() {
        return new CancelAppointmentResponse();
    }

    /**
     * Create an instance of {@link ConfirmAppointmentRequest }
     * 
     * @return
     *     the new instance of {@link ConfirmAppointmentRequest }
     */
    public ConfirmAppointmentRequest createConfirmAppointmentRequest() {
        return new ConfirmAppointmentRequest();
    }

    /**
     * Create an instance of {@link ConfirmAppointmentResponse }
     * 
     * @return
     *     the new instance of {@link ConfirmAppointmentResponse }
     */
    public ConfirmAppointmentResponse createConfirmAppointmentResponse() {
        return new ConfirmAppointmentResponse();
    }

}
