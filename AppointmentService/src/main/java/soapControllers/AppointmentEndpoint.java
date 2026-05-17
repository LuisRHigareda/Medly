package soapControllers;

import interfaces.IAppointmentService;
import mx.edu.itson.soap.appointments.AddAppointmentRequest;
import mx.edu.itson.soap.appointments.AddAppointmentResponse;
import mx.edu.itson.soap.appointments.CancelAppointmentRequest;
import mx.edu.itson.soap.appointments.CancelAppointmentResponse;
import mx.edu.itson.soap.appointments.ConfirmAppointmentRequest;
import mx.edu.itson.soap.appointments.ConfirmAppointmentResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

/**
 *
 * @author Leonardo Flores Leyva - 252390
 */
@Endpoint
public class AppointmentEndpoint {
    
    private static final String NAMESPACE_URI = "http://itson.edu.mx/soap/appointments";
    
    private final IAppointmentService appointmentService;

    public AppointmentEndpoint(IAppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AddAppointmentRequest")
    @ResponsePayload
    public AddAppointmentResponse addAppointment(@RequestPayload AddAppointmentRequest request){
        AddAppointmentResponse response = appointmentService.addAppointment(request);
        return response;
    }
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "CancelAppointmentRequest")
    @ResponsePayload
    public CancelAppointmentResponse cancelAppointment(@RequestPayload CancelAppointmentRequest request){
        CancelAppointmentResponse response = appointmentService.cancelAppointment(request);
        return response;
    }
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ConfirmAppointmentRequest")
    @ResponsePayload
    public ConfirmAppointmentResponse confirmAppointment(@RequestPayload ConfirmAppointmentRequest request){
        ConfirmAppointmentResponse response = appointmentService.confirmAppointment(request);
        return response;
    }
}