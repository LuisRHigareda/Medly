package com.medical.medicalclient.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * @author Yuri German Garcia López - 252583
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressResponse {
    private Integer id;
    private String street;
    private String city;
    private String state;

    public AddressResponse() {
    }

    public AddressResponse(Integer id, String street, String city, String state) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}