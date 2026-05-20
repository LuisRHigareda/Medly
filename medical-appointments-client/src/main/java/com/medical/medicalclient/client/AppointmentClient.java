package com.medical.medicalclient.client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medical.medicalclient.dto.AppointmentResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
// SOAP Classes
import wsc.AddAppointmentRequest;
import wsc.AddAppointmentResponse;
import wsc.CancelAppointmentRequest;
import wsc.CancelAppointmentResponse;
import wsc.ConfirmAppointmentRequest;
import wsc.ConfirmAppointmentResponse;
import wsc.AppointmentsPort;
import wsc.AppointmentsPortService;
/**
 * Service client responsible for managing the life cycle of medical appointments
 * Uses native JAX-WS SOAP proxies generated from the corporate WSDL contract
 * @author Yuri German Garcia López - 252583
 */
public class AppointmentClient {
    
    private static final String REST_BASE_URL = "http://localhost:8080/api/appointments";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public AppointmentClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = JsonMapperConfig.getMapper();
    }

    public List<AppointmentResponse> getAppointmentsByPatientId(Integer patientId) {
        try {
            String bypassUrl = "http://localhost:8081/appointments/patient/" + patientId;

            System.out.println("Disparando petición REST Directa: " + bypassUrl);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(bypassUrl))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("REST Direct Response - Status Code: " + response.statusCode());

            if (response.statusCode() == 200) {
                String body = response.body();
                if (body == null || body.trim().isEmpty()) {
                    System.out.println("No appointments found for patient ID: " + patientId);
                    return Collections.emptyList();
                }
                return objectMapper.readValue(body, 
                        objectMapper.getTypeFactory().constructCollectionType(List.class, AppointmentResponse.class));
            } else {
                System.err.println("Failed to fetch appointments. HTTP Status Code: " + response.statusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            System.err.println("Network exception inside getAppointmentsByPatientId: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public String bookAppointment(AppointmentResponse appointment) {
        try {
            AddAppointmentRequest request = new AddAppointmentRequest();
            request.setPatientId(appointment.getPatientId());
            request.setConsultingRoomId(appointment.getConsultingRoomId());

            LocalDateTime datetime = LocalDateTime.of(appointment.getDate(), appointment.getTime());
            GregorianCalendar gCalendar = GregorianCalendar.from(ZonedDateTime.of(datetime, ZoneId.systemDefault()));
            XMLGregorianCalendar xmlDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
            request.setDateTime(xmlDateTime);

            AppointmentsPortService service = new AppointmentsPortService();
            AppointmentsPort port = service.getAppointmentsPortSoap11();
            AddAppointmentResponse response = port.addAppointment(request);

            return String.format("%s\nReference number: %s", 
                    response.getSuccessMessage(), 
                    response.getReferenceNumber());
        } catch (Exception e) {
            System.err.println("SOAP Exception inside bookAppointment: " + e.getMessage());
            return null;
        }
    }

    public boolean confirmAppointment(Integer appointmentId) {
        try {
            ConfirmAppointmentRequest request = new ConfirmAppointmentRequest();
            request.setAppointmentId(appointmentId);

            AppointmentsPortService service = new AppointmentsPortService();
            AppointmentsPort port = service.getAppointmentsPortSoap11();
            ConfirmAppointmentResponse response = port.confirmAppointment(request);

            return response.getSuccessMessage() != null;
        } catch (Exception e) {
            System.err.println("SOAP Exception inside confirmAppointment: " + e.getMessage());
            return false;
        }
    }

    public boolean cancelAppointment(Integer appointmentId) {
        try {
            CancelAppointmentRequest request = new CancelAppointmentRequest();
            request.setAppointmentId(appointmentId);

            AppointmentsPortService service = new AppointmentsPortService();
            AppointmentsPort port = service.getAppointmentsPortSoap11();
            CancelAppointmentResponse response = port.cancelAppointment(request);

            return response.getSuccessMessage() != null;
        } catch (Exception e) {
            System.err.println("SOAP Exception inside cancelAppointment: " + e.getMessage());
            return false;
        }
    }
}