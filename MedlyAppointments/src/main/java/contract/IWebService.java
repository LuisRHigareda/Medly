package contract;

import exceptions.ServiceException;
import model.AppointmentDTO;
import model.NewAppointmentDTO;

/**
 *
 * @author Leonardo Flores Leyva
 */
public interface IWebService {
    /**
     * Schedules the new appointment.
     * @param appointment Appointment to be scheduled
     * @return A reponse message
     * @throws ServiceException Exception if something goes wrong
     */
    public String newAppointment(NewAppointmentDTO appointment) throws ServiceException;
    /**
     * Cancels the receiving appointment.
     * @param appointment Appointment to be canceled
     * @return A reponse message
     * @throws ServiceException Exception if something goes wrong
     */
    public String cancelAppointment(AppointmentDTO appointment) throws ServiceException;
}