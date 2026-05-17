package com.medical.medicalclient.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
/**
 * @author Yuri German Garcia López - 252583
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PatientResponse {
    private Integer id;
    private Integer userId;
    private String email;
    private String name;
    private String lastName;
    private String phone;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    
    private String affiliationNumber;
    private Boolean validity;
    private AddressResponse address;

    public PatientResponse() {
    }

    public PatientResponse(Integer id, Integer userId, String email, String name, String lastName, String phone, LocalDate birthday, String affiliationNumber, Boolean validity, AddressResponse address) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getAffiliationNumber() {
        return affiliationNumber;
    }

    public void setAffiliationNumber(String affiliationNumber) {
        this.affiliationNumber = affiliationNumber;
    }

    public Boolean getValidity() {
        return validity;
    }

    public void setValidity(Boolean validity) {
        this.validity = validity;
    }

    public AddressResponse getAddress() {
        return address;
    }

    public void setAddress(AddressResponse address) {
        this.address = address;
    }
}