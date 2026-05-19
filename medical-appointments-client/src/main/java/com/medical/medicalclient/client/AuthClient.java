package com.medical.medicalclient.client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.medical.medicalclient.dto.LoginRequest;
import com.medical.medicalclient.dto.LoginResponse;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
/**
 * Service client responsible for handling authentication requests 
 * by communicating directly with the API Gateway
 * * @author Yuri German Garcia López - 252583
 */
public class AuthClient {
    // API Gateway base URL endpoint for authentication routes
    private static final String BASE_URL = "http://localhost:8080/api/auth";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    
    /**
     * Initializes the HTTP Client with a connection timeout and 
     * retrieves the shared Jackson ObjectMapper instance
     */
    public AuthClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = JsonMapperConfig.getMapper();
    }
    
    /**
     * Sends user credentials to the API Gateway to perform a login operation
     * @param email The user's login email address
     * @param password The user's login password
     * @return A LoginResponse DTO indicating the result of the operation
     */
    public LoginResponse login(String email, String password) {
        try {
            // Create the request data transfer object with user input
            LoginRequest requestDto = new LoginRequest(email, password);

            // Convert the Java object payload into a JSON string format
            String jsonBody = objectMapper.writeValueAsString(requestDto);

            // Build the HTTP POST request targeted at the API Gateway routing path
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            // Dispatch the HTTP request synchronously to the server
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Validate if the server returned an HTTP 200 OK status code
            if (response.statusCode() == 200) {
                // Deserialize the response JSON body back into a Java LoginResponse object
                return objectMapper.readValue(response.body(), LoginResponse.class);
            } else {
                System.err.println("Login failed. Server HTTP status code: " + response.statusCode());
                return new LoginResponse(false, "Server error: " + response.statusCode(), null, null, null);
            }

        } catch (Exception e) {
            System.err.println("Network error while attempting to log in: " + e.getMessage());
            return new LoginResponse(false, "Could not connect to the gateway server.", null, null, null);
        }
    }
    
}