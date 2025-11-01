package com.hackathon.donation.repository;

import com.hackathon.donation.model.Donation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource; // 1. Import DataSource
import org.springframework.stereotype.Repository; // 2. Import Repository

@Repository // 3. Add @Repository annotation
public class DonationRepository {

    // 4. REMOVE all static DB_URL, DB_USER, DB_PASSWORD, and DB_DRIVER constants
    // 5. REMOVE the static { ... } driver loading block

    // 6. ADD THE MISSING FIELD
    private final DataSource dataSource; 

    // 7. ADD THE CONSTRUCTOR to inject the DataSource
    public DonationRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    // 8. UPDATE getConnection() to use the pool
    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    // --- Helper to map ResultSet to Donation object (No changes) ---
    private Donation mapRowToDonation(ResultSet rs) throws SQLException {
        Donation donation = new Donation();
        donation.setId(rs.getInt("id"));
        donation.setDonorId(rs.getInt("donor_id"));
        donation.setFoodType(rs.getString("food_type"));
        donation.setQuantity(rs.getString("quantity"));
        
        donation.setPickupTime(rs.getTimestamp("pickup_time").toLocalDateTime()); 
        donation.setPostDate(rs.getTimestamp("post_date").toLocalDateTime()); 
        
        donation.setStatus(rs.getString("status"));
        
        if (rs.getObject("home_id") != null) {
            donation.setHomeId(rs.getInt("home_id"));
        }
        return donation;
    }

    // --- Module 2: Post New Donation (CREATE) ---
    public void save(Donation donation) {
        String sql = "INSERT INTO donations (donor_id, food_type, quantity, pickup_time, status) " +
                     "VALUES (?, ?, ?, ?, 'AVAILABLE')";

        try (Connection conn = getConnection(); // This now uses the pool
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, donation.getDonorId());
            ps.setString(2, donation.getFoodType());
            ps.setString(3, donation.getQuantity());
            ps.setTimestamp(4, Timestamp.valueOf(donation.getPickupTime())); 

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error saving new donation to PostgreSQL", e);
        }
    }
    
    // --- Module 3: Home Dashboard (READ - All Available) ---
    public List<Donation> findAllAvailable() {
        List<Donation> donations = new ArrayList<>();
        String sql = "SELECT * FROM donations WHERE status = 'AVAILABLE' ORDER BY post_date DESC";

        try (Connection conn = getConnection(); // This now uses the pool
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) { 

            while (rs.next()) {
                donations.add(mapRowToDonation(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding available donations", e);
        }
        return donations;
    }

    // --- Module 1: Donor Dashboard (READ - By Donor) ---
    public List<Donation> findByDonorId(int donorId) {
        List<Donation> donations = new ArrayList<>();
        String sql = "SELECT * FROM donations WHERE donor_id = ? ORDER BY post_date DESC";

        try (Connection conn = getConnection(); // This now uses the pool
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, donorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    donations.add(mapRowToDonation(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding donations by donor ID", e);
        }
        return donations;
    }

    // --- Module 4: Claim Donation (UPDATE - Status) ---
    public int updateStatusToClaimed(int donationId, int homeId) {
        String sql = "UPDATE donations SET status = 'CLAIMED', home_id = ?, claim_date = CURRENT_TIMESTAMP WHERE id = ? AND status = 'AVAILABLE'";

        try (Connection conn = getConnection(); // This now uses the pool
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, homeId);
            ps.setInt(2, donationId);

            return ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error claiming donation in PostgreSQL", e);
        }
    }

    /**
     * [DEPRECATED-STYLE] This 'update' method is unsafe.
     * We should only use the 'update(Donation, int donorId)' method.
     */
    public int update(Donation donation) {
        String sql = "UPDATE donations SET food_type = ?, quantity = ?, pickup_time = ? " +
                     "WHERE id = ? AND status = 'AVAILABLE'";

        try (Connection conn = getConnection(); // This now uses the pool
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, donation.getFoodType());
            ps.setString(2, donation.getQuantity());
            ps.setTimestamp(3, Timestamp.valueOf(donation.getPickupTime())); 
            ps.setInt(4, donation.getId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating donation details", e);
        }
    }

    // --- Module 5: Donation Management (DELETE) ---
    public int delete(int donationId) {
        String sql = "DELETE FROM donations WHERE id = ? AND status = 'AVAILABLE'";

        try (Connection conn = getConnection(); // This now uses the pool
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, donationId);

            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting donation", e);
        }
    }
    
    /**
     * Finds a single Donation by its unique ID.
     */
    public Donation findDonationById(int donationId) {
        String sql = "SELECT * FROM donations WHERE id = ?";
        
        try (Connection conn = getConnection(); // This now uses the pool
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, donationId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToDonation(rs); 
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding donation by ID", e);
        }
        return null;
    }

    /**
     * [UPDATED] Updates an existing donation's details with security.
     */
    public int update(Donation donation, int donorId) {
        String sql = "UPDATE donations SET food_type = ?, quantity = ?, pickup_time = ? " +
                     "WHERE id = ? AND status = 'AVAILABLE' AND donor_id = ?";

        try (Connection conn = getConnection(); // This now uses the pool
             PreparedStatement ps = conn.prepareStatement(sql)) {
          

            ps.setString(1, donation.getFoodType());
            ps.setString(2, donation.getQuantity());
            ps.setTimestamp(3, Timestamp.valueOf(donation.getPickupTime())); 
            ps.setInt(4, donation.getId());
            ps.setInt(5, donorId); // Security check

            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating donation details", e);
        }
    }
}
