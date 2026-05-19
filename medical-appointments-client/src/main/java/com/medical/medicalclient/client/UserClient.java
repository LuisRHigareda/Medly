package com.medical.medicalclient.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medical.medicalclient.dto.DoctorResponse;
import com.medical.medicalclient.dto.PatientResponse;
import com.medical.medicalclient.dto.ReceptionistResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * Service client responsible for querying user, patient, and receptionist 
 * records from the user-service subsystem via the central API Gateway.
 * @author Yuri German Garcia López - 252583
 */
public class UserClient {
    
    // API Gateway base URL endpoint routing toward user management paths
    private static final String BASE_URL = "http://localhost:8080/api/users";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    /**
     * Initializes the native HTTP Client with network timeouts and 
     * wires the shared Jackson ObjectMapper configuration.
     */
    public UserClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = JsonMapperConfig.getMapper();
    }

    /**
     * Retrieves extended profile data for a specific receptionist by their ID.
     * Maps to: GET http://localhost:8080/api/users/receptionists/{id}
     */
    public ReceptionistResponse getReceptionistById(Integer id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/receptionists/" + id))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), ReceptionistResponse.class);
            } else {
                System.err.println("Failed to fetch receptionist. HTTP Code: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Network exception inside getReceptionistById: " + e.getMessage());
            return null;
        }
    }

    /**
     * Queries a patient's core files and validation status using their unique affiliation number.
     * Maps to: GET http://localhost:8080/api/users/patients/{affiliationNumber}
     */
    public PatientResponse getPatientByAffiliationNumber(String affiliationNumber) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/patients/" + affiliationNumber))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), PatientResponse.class);
            } else {
                System.err.println("Patient lookup failed. HTTP Status Code: " + response.statusCode());
                return null;
            }
        } catch (Exception e) {
            System.err.println("Network exception inside getPatientByAffiliationNumber: " + e.getMessage());
            return null;
        }
    }

    /**
     * Fetches a complete collection of all registered medical doctors in the clinic grid.
     * Maps to: GET http://localhost:8080/api/users/doctors
     */
    public List<DoctorResponse> findAllDoctors() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/doctors"))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), 
                        objectMapper.getTypeFactory().constructCollectionType(List.class, DoctorResponse.class));
            } else {
                System.err.println("Failed to list doctors database grid. HTTP Code: " + response.statusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            System.err.println("Network exception inside findAllDoctors execution: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}