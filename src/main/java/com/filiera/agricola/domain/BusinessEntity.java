package com.filiera.agricola.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class BusinessEntity {
    protected String id;
    protected String companyName;
    protected String VATNumber;
    protected String address;
    protected String email;
    protected String phoneNumber;
    protected LocalDateTime registrationDate;
    protected List<UUID> authorizedUserIds;
    protected DefaultCoordinates coordinates;


    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";


    public BusinessEntity (
            String companyName,
            String VATNumber,
            String address,
            String email,
            String phoneNumber,
            DefaultCoordinates coordinates
    ) {
        this.id = UUID.randomUUID().toString();
        this.companyName = Objects.requireNonNull(companyName,"Company name cannot be null");
        this.VATNumber = Objects.requireNonNull(VATNumber, "VAT number cannot be null");
        this.address = Objects.requireNonNull(address, "Address cannot be null");
        this.email = validateEmail(email);
        this.phoneNumber = Objects.requireNonNull(phoneNumber,   "Phone number cannot be null");
        this.coordinates = Objects.requireNonNull(coordinates, "Coordinates cannot be null");
    }

    public String getId() {return id;}
    public String getCompanyName() {return companyName;}
    public String getVATNumber() {return VATNumber;}
    public String getAddress() {return address;}
    public String getEmail() {return email;}
    public String getPhoneNumber() {return phoneNumber;}
    public LocalDateTime getRegistrationDate() {return registrationDate;}
    public List<UUID> getAuthorizedUserIds() {return authorizedUserIds;}
    public String getCoordinates() {
        return "{lat: " + coordinates.getLat() + ", lng: " + coordinates.getLng() + "}";
    }

    void setEmail (String email) {
        this.email = email;
    }

    private String validateEmail(String email) {
        Objects.requireNonNull(email, "Email cannot be null");
        String trimmed = email.trim();

        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!trimmed.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }

        return trimmed;
    }
}
