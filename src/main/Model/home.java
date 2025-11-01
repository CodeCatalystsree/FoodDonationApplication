package com.hackathon.donation.model;

/**
 * Model class representing a Home (Child Care / Old Age) user in the 'homes' table.
 */
public class Home {
    
    // Fields mirror the 'homes' PostgreSQL table
    private int id;
    private String name;
    private String type; // 'Child Care' or 'Old Age'
    private String contactEmail;
    private String passwordHash; // For authentication
    private String contactPhone;
    private String address;

    // --- Constructors ---

    public Home() {
        // Default constructor for Spring/Thymeleaf form binding
    }

    // --- Getters and Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
