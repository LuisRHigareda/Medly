package com.medical.medicalclient.client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medical.medicalclient.dto.AppointmentResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
/**
 * Service client responsible for managing the life cycle of medical appointments
 * @author Yuri German Garcia López - 252583
 */
public class AppointmentClient {
    // API Gateway base URL endpoint routing toward appointment service modules
    private static final String BASE_URL = "http://localhost:8080/api/appointments";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    /**
     * Initializes the native HTTP Client with proper network timeouts and 
     * registers the globally shared Jackson ObjectMapper configuration
     */
    public AppointmentClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = JsonMapperConfig.getMapper();
    }

    /**
     * Fetches all upcoming medical appointments associated with a patient's affiliation number
     * @param affiliationNumber The unique identifier assigned to the patient
     * @return A list of AppointmentResponse DTOs, or an empty list if an error occurs
     */
    public List<AppointmentResponse> getAppointmentsByPatientId(Integer patientId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/patient/" + patientId)) // Apunta a /api/appointments/patient/{id}
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                // Deserializa el JSON real de Leonardo
                return objectMapper.readValue(response.body(), 
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

    /**
     * Registers a new medical appointment time slot within the scheduling grid system
     * @param appointment The AppointmentResponse payload detailing date, time, doctor, and room contexts
     * @return true if the booking transaction completes successfully, false otherwise.
     */
    public boolean bookAppointment(AppointmentResponse appointment) {
        try {
            String jsonBody = objectMapper.writeValueAsString(appointment);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/book"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200 || response.statusCode() == 201;
        } catch (Exception e) {
            System.err.println("Network exception while booking a new appointment slot: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates an appointment status state to CONFIRMED.
     * The receptionist has a strict 15-minute window before the slot shifts to MISSED.
     * * @param appointmentId The primary key identifier of the targeted appointment.
     * @return true if the status transition updates successfully on the server side, false otherwise.
     */
    public boolean confirmAppointment(Integer appointmentId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/confirm/" + appointmentId))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("Network exception while executing confirmAppointment: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cancels an appointment from the system database records
     * Operations are restricted if executed beyond a 24-hour post-booking threshold
     * @param appointmentId The primary key identifier of the targeted appointment
     * @return true if the cancellation transaction is successfully processed, false otherwise
     */
    public boolean cancelAppointment(Integer appointmentId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/cancel/" + appointmentId))
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("Network exception while executing cancelAppointment: " + e.getMessage());
            return false;
        }
    }
}
