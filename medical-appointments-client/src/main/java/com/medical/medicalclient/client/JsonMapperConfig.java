package com.medical.medicalclient.client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
/**
 * Configuration class that provides a centralized and pre-configured 
 * ObjectMapper instance for JSON serialization and deserialization
 * @author Yuri German Garcia López - 252583
 */
public class JsonMapperConfig {
    // Singleton instance of the Jackson ObjectMapper
    private static final ObjectMapper mapper = new ObjectMapper();
    
    static {
        // Register the JavaTimeModule to handle Java 8 date types like LocalDate
        mapper.registerModule(new JavaTimeModule());
    }
        
    /**
     * This provides access to the globally shared ObjectMapper instance
     * @return Pre-configured ObjectMapper instance
     */
    public static ObjectMapper getMapper(){
        return mapper;
    }
}