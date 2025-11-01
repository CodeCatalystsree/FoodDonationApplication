package com.hackathon.donation.model;

import java.time.LocalDateTime;

public class Donation {
    private int id;
    private int donorId;
    private Integer homeId; 
    private String foodType;
    private String quantity;
    private LocalDateTime pickupTime; // Changed from Timestamp
    private LocalDateTime postDate;   // Changed from Timestamp
    private String status;

    public Donation() {}

    // Constructor for creating a new post (DB sets ID and PostDate)
    public Donation(int donorId, String foodType, String quantity, LocalDateTime pickupTime, String status) {
        this.donorId = donorId;
        this.foodType = foodType;
        this.quantity = quantity;
        this.pickupTime = pickupTime;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getDonorId() { return donorId; }
    public void setDonorId(int donorId) { this.donorId = donorId; }

    public Integer getHomeId() { return homeId; }
    public void setHomeId(Integer homeId) { this.homeId = homeId; }

    public String getFoodType() { return foodType; }
    public void setFoodType(String foodType) { this.foodType = foodType; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }

    public LocalDateTime getPickupTime() { return pickupTime; }
    public void setPickupTime(LocalDateTime pickupTime) { this.pickupTime = pickupTime; }

    public LocalDateTime getPostDate() { return postDate; }
    public void setPostDate(LocalDateTime postDate) { this.postDate = postDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
