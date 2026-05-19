package com.medical.userservice.dto;

public class AddressResponse {
    private Integer id;
    private String street;
    private String city;
    private String state;

    public AddressResponse(Integer id, String street, String city, String state) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }
}
