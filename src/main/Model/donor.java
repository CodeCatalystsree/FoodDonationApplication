package com.hackathon.donation.model;

/**
 * Model class representing a Donor (restaurant, business, etc.) user in the 'donors' table.
 */
public class Donor {

    // Fields mirror the 'donors' PostgreSQL table
    private int id;
    private String name;
    private String email;
    private String passwordHash; // For authentication
    private String phone;
    private String address;

    // --- Constructors ---

    public Donor() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    // NOTE: For login forms, we often use setPassword and getPassword 
    // for the plaintext input, and handle hashing separately. 
    // Here, we use 'passwordHash' to strictly represent the stored value.
    public void setPassword(String password) {
        // In a real application, this is where you would hash the password.
        // For the hackathon, you might just store the plain password for simplicity, 
        // but keep the field named 'passwordHash' for good practice.
        this.passwordHash = password; 
    }
    
    public String getPassword() {
        return this.passwordHash; // Returning the stored value for form binding
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
