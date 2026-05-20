package com.medical.userservice.dto;

/**
 *
 * @author Leonardo Flores Leyva
 */
public class ClinicResponse {
    
    private Integer id;
    private String name;
    private String street;
    private String state;
    private String city;

    public ClinicResponse() {}

    public ClinicResponse(Integer id, String name, String street, String state, String city) {
        this.id = id;
        this.name = name;
        this.street = street;
        this.state = state;
        this.city = city;
    }

    public Integer getId() {return id;}

    public String getName() {return name;}

    public String getStreet() {return street;}

    public String getState() {return state;}

    public String getCity() {return city;}

    public void setId(Integer id) {this.id = id;}

    public void setName(String name) {this.name = name;}

    public void setStreet(String street) {this.street = street;}

    public void setState(String state) {this.state = state;}

    public void setCity(String city) {this.city = city;}    
}