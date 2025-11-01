package com.hackathon.donation.repository;

import com.hackathon.donation.model.Donor;
import com.hackathon.donation.model.Home;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource; 
import org.springframework.stereotype.Repository; 

/**
 * Data Access Object (DAO) for user authentication (Donor and Home login).
 * [REFACTORED] Uses a Spring-managed DataSource for connection pooling.
 */
@Repository 
public class AuthRepository {

    // --- THIS LINE WAS LIKELY MISSING ---
    private final DataSource dataSource; // <-- This field declaration is required

    /**
     * Create a constructor to INJECT the DataSource (the pool).
     * This is line 32 (approx) where your error occurred.
     */
    public AuthRepository(DataSource dataSource) {
        this.dataSource = dataSource; // This line will now work
    }

    /** Helper method now gets a connection from the pool. */
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection(); // This will also now work
    }
    
    // --- Helper Mappers (No changes needed) ---
    
    private Donor mapRowToDonor(ResultSet rs) throws SQLException {
        Donor donor = new Donor();
        donor.setId(rs.getInt("id"));
        donor.setName(rs.getString("name"));
        donor.setEmail(rs.getString("email"));
        donor.setPasswordHash(rs.getString("password_hash"));
        donor.setPhone(rs.getString("phone"));
        donor.setAddress(rs.getString("address"));
        return donor;
    }
    
    private Home mapRowToHome(ResultSet rs) throws SQLException {
        Home home = new Home();
        home.setId(rs.getInt("id"));
        home.setName(rs.getString("name"));
        home.setType(rs.getString("type"));
        home.setContactEmail(rs.getString("contact_email"));
        home.setPasswordHash(rs.getString("password_hash"));
        home.setContactPhone(rs.getString("contact_phone"));
        home.setAddress(rs.getString("address"));
        return home;
    }

    // --- Authentication Methods (No changes needed) ---

    /** Retrieves a Donor by email. Used during the login process. */
    public Donor findDonorByEmail(String email) {
        String sql = "SELECT id, name, email, password_hash, phone, address FROM donors WHERE email = ?";
        Donor donor = null;
        
        try (Connection conn = getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    donor = mapRowToDonor(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error occurred while finding donor: " + e.getMessage());
        }
        return donor;
    }
    
    /** Retrieves a Home by contact_email. Used during the login process. */
    public Home findHomeByEmail(String email) {
        String sql = "SELECT id, name, type, contact_email, password_hash, contact_phone, address FROM homes WHERE contact_email = ?";
        Home home = null;
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    home = mapRowToHome(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error occurred while finding home: " + e.getMessage());
        }
        return home;
    }
    
    // --- Registration Methods (No changes needed) ---

    /** Saves a new Donor record to the database. */
    public void saveDonor(Donor donor) {
        String sql = "INSERT INTO donors (name, email, password_hash, phone, address) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, donor.getName());
            ps.setString(2, donor.getEmail());
            ps.setString(3, donor.getPasswordHash());
            ps.setString(4, donor.getPhone());
            ps.setString(5, donor.getAddress());
            
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Database error saving new donor: " + e.getMessage());
            throw new RuntimeException("Could not register donor.", e);
        }
    }

    /** Saves a new Home record to the database. */
    public void saveHome(Home home) {
        String sql = "INSERT INTO homes (name, type, contact_email, password_hash, contact_phone, address) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, home.getName());
            ps.setString(2, home.getType());
            ps.setString(3, home.getContactEmail());
            ps.setString(4, home.getPasswordHash());
            ps.setString(5, home.getContactPhone());
            ps.setString(6, home.getAddress());
            
            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Database error saving new home: " + e.getMessage());
            throw new RuntimeException("Could not register home.", e);
        }
    }
}
