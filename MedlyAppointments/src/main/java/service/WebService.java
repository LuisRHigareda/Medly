package service;

import contract.IWebService;
import exceptions.ServiceException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import model.CancelAppointmentDTO;
import model.NewAppointmentDTO;
import org.springframework.stereotype.Service;
import wsc.AddAppointmentRequest;
import wsc.AddAppointmentResponse;
import wsc.AppointmentsPort;
import wsc.AppointmentsPortService;
import wsc.CancelAppointmentRequest;
import wsc.CancelAppointmentResponse;

/**
 *
 * @author Leonardo Flores Leyva - 252390
 */
@Service
public class WebService implements IWebService {

    @Override
    public String newAppointment(NewAppointmentDTO appointment) throws ServiceException {
        
        // Validates the receiving dto (simple validation)
        if (appointment == null) { throw new ServiceException("Empty data receiveid!"); }

        // Retrieves its data
        Integer patientId = appointment.getPatientId();
        Integer consultingRoomId = appointment.getConsultingRoomId();
        LocalDateTime datetime = appointment.getDateTime();

        // Validates the data (simple validation)
        if (patientId == null) { throw new ServiceException("Missing the appointment's associated patient!"); }
        if (consultingRoomId == null) { throw new ServiceException("Missing the appointment's associated consulting room or doctor!"); }
        if (datetime == null) { throw new ServiceException("Missing selected date and time of the appointment!"); }

        // The request is built
        AddAppointmentRequest request = new AddAppointmentRequest();
        request.setPatientId(patientId);
        request.setConsultingRoomId(consultingRoomId);
        
        try {
            // The date and time of the appointment is converted to a transferable format (the timezone is omitted)
            GregorianCalendar gregorianCalendar = GregorianCalendar.from(ZonedDateTime.of(datetime, ZoneId.systemDefault()));
            XMLGregorianCalendar dateCreated = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
            request.setDateTime(dateCreated);

            // Executes the operation and returns a response
            AppointmentsPortService appointmentsService = new AppointmentsPortService();
            AppointmentsPort port = appointmentsService.getAppointmentsPortSoap11();
            AddAppointmentResponse response = port.addAppointment(request);

            // Builds up the success message
            String referencesNumber, successMsg, createdTime;
            referencesNumber = String.valueOf(response.getReferenceNumber());
            successMsg = response.getSuccessMessage();
            createdTime = response.getAppointmentCreatedDateTime().toString();

            // Returns the success message
            return String.format("%s!\n\nDetails:\nDate and time of creation: %s\nReference number: %s", successMsg, createdTime, referencesNumber);

        } catch (Exception e) { // Any possible error is catched.
            throw new ServiceException("Unknown error occured! Please, try again later.");
        }
    }

    @Override
    public String cancelAppointment(CancelAppointmentDTO appointment) throws ServiceException {

        // Validates the receiving dto (simple validation)
        if (appointment == null) { throw new ServiceException("Empty data receiveid!"); }

        // Retrieves the appointment's id
        Integer appointmentId = appointment.getAppointmentId();
        // Validates the id (simple validation)
        if (appointmentId == null) { throw new ServiceException("Missing the appointment to be cancelled!"); }

        // The request is built
        CancelAppointmentRequest request = new CancelAppointmentRequest();
        request.setAppointmentId(appointmentId);
        try {
            // Executes the operation and returns a response
            AppointmentsPortService appointmentsService = new AppointmentsPortService();
            AppointmentsPort port = appointmentsService.getAppointmentsPortSoap11();
            CancelAppointmentResponse response = port.cancelAppointment(request);

            // Returns the success message
            return response.getSuccessMessage();

        } catch (Exception e) { // Any possible error is catched.
            throw new ServiceException("Unknown error occured! Please, try again later.");
        }
    }
}
