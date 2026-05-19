package com.medical.userservice.dto;

import java.time.LocalDate;

public class PatientResponse {
    private Integer id;
    private Integer userId;
    private String email;
    private String name;
    private String lastName;
    private String phone;
    private LocalDate birthday;
    private String affiliationNumber;
    private Boolean validity;
    private AddressResponse address;

    public PatientResponse(Integer id, Integer userId, String email, String name, String lastName, String phone,
                           LocalDate birthday, String affiliationNumber, Boolean validity, AddressResponse address) {
        this.id = id;
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.birthday = birthday;
        this.affiliationNumber = affiliationNumber;
        this.validity = validity;
        this.address = address;
    }

    public Integer getId() { return id; }
    public Integer getUserId() { return userId; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getLastName() { return lastName; }
    public String getPhone() { return phone; }
    public LocalDate getBirthday() { return birthday; }
    public String getAffiliationNumber() { return affiliationNumber; }
    public Boolean getValidity() { return validity; }
    public AddressResponse getAddress() { return address; }
}
