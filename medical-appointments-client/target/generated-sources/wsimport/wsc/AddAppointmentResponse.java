
package wsc;

import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * &lt;p&gt;Clase Java para anonymous complex type.&lt;/p&gt;
 * 
 * &lt;p&gt;El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.&lt;/p&gt;
 * 
 * &lt;pre&gt;{&#064;code
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="successMessage" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="appointmentCreatedDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="referenceNumber" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * }&lt;/pre&gt;
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "successMessage",
    "appointmentCreatedDateTime",
    "referenceNumber"
})
@XmlRootElement(name = "AddAppointmentResponse")
public class AddAppointmentResponse {

    @XmlElement(required = true)
    protected String successMessage;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar appointmentCreatedDateTime;
    protected long referenceNumber;

    /**
     * Obtiene el valor de la propiedad successMessage.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSuccessMessage() {
        return successMessage;
    }

    /**
     * Define el valor de la propiedad successMessage.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSuccessMessage(String value) {
        this.successMessage = value;
    }

    /**
     * Obtiene el valor de la propiedad appointmentCreatedDateTime.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getAppointmentCreatedDateTime() {
        return appointmentCreatedDateTime;
    }

    /**
     * Define el valor de la propiedad appointmentCreatedDateTime.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setAppointmentCreatedDateTime(XMLGregorianCalendar value) {
        this.appointmentCreatedDateTime = value;
    }

    /**
     * Obtiene el valor de la propiedad referenceNumber.
     * 
     */
    public long getReferenceNumber() {
        return referenceNumber;
    }

    /**
     * Define el valor de la propiedad referenceNumber.
     * 
     */
    public void setReferenceNumber(long value) {
        this.referenceNumber = value;
    }

}
