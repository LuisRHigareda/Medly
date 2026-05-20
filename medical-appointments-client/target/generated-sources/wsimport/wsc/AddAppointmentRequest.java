
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
 *         &lt;element name="patientId" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="consultingRoomId" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="dateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
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
    "patientId",
    "consultingRoomId",
    "dateTime"
})
@XmlRootElement(name = "AddAppointmentRequest")
public class AddAppointmentRequest {

    protected int patientId;
    protected int consultingRoomId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateTime;

    /**
     * Obtiene el valor de la propiedad patientId.
     * 
     */
    public int getPatientId() {
        return patientId;
    }

    /**
     * Define el valor de la propiedad patientId.
     * 
     */
    public void setPatientId(int value) {
        this.patientId = value;
    }

    /**
     * Obtiene el valor de la propiedad consultingRoomId.
     * 
     */
    public int getConsultingRoomId() {
        return consultingRoomId;
    }

    /**
     * Define el valor de la propiedad consultingRoomId.
     * 
     */
    public void setConsultingRoomId(int value) {
        this.consultingRoomId = value;
    }

    /**
     * Obtiene el valor de la propiedad dateTime.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateTime() {
        return dateTime;
    }

    /**
     * Define el valor de la propiedad dateTime.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateTime(XMLGregorianCalendar value) {
        this.dateTime = value;
    }

}
