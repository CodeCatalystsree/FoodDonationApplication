package com.hackathon.donation.service;

import com.hackathon.donation.model.Donation;
import com.hackathon.donation.repository.DonationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service layer for implementing business logic related to Donations.
 * Handles the logic for all five core modules.
 */
@Service
public class DonationService {

    private final DonationRepository donationRepository;

    // Spring will automatically inject DonationRepository here
    public DonationService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    // --- Module 2: Post New Donation (CREATE) ---

    /**
     * Handles the logic for a donor posting a new donation.
     */
    public void createNewDonation(Donation donation) {
        // Basic validation before saving
        if (donation.getDonorId() == 0 || donation.getQuantity() == null || donation.getPickupTime() == null) {
            throw new IllegalArgumentException("Missing required donation details.");
        }
        // Repository handles saving with default status 'AVAILABLE'
        donationRepository.save(donation);
    }
    
    // --- Module 3: Home Dashboard (READ - All Available) ---

    /**
     * Fetches the list of all currently AVAILABLE donations for the marketplace view.
     */
    public List<Donation> getAllAvailableDonations() {
        return donationRepository.findAllAvailable();
    }
    
    // --- Module 1 & 5: Donor Dashboard and Management (READ, UPDATE, DELETE) ---

    /**
     * Fetches all donations belonging to a specific Donor (Module 1).
     */
    public List<Donation> getDonationsByDonor(int donorId) {
        return donationRepository.findByDonorId(donorId);
    }

    /**
     * Handles updating an available donation (Module 5: Edit).
     * @return true if update was successful (i.e., donation was available).
     */
    public boolean updateDonationDetails(Donation donation) {
        // Business rule: only available items can be updated.
        // The repository's SQL WHERE clause enforces this, but the service provides the interface.
        int rowsAffected = donationRepository.update(donation);
        return rowsAffected > 0;
    }

    /**
     * Handles deleting an available donation (Module 5: Delete).
     * @return true if deletion was successful (i.e., donation was available).
     */
    public boolean deleteDonation(int donationId) {
        int rowsAffected = donationRepository.delete(donationId);
        return rowsAffected > 0;
    }

    // --- Module 4: Claim Donation (UPDATE - Status) ---

    /**
     * Handles the logic for a Home claiming a donation.
     * @return true if claim was successful (if status was 'AVAILABLE' previously).
     */
    public boolean claimDonation(int donationId, int homeId) {
        // Business rule: The database update only proceeds if the status is 'AVAILABLE'.
        int rowsAffected = donationRepository.updateStatusToClaimed(donationId, homeId);
        
        if (rowsAffected == 0) {
            // This means the donation was already claimed or the ID was invalid.
            return false;
        }
        // Successfully claimed
        return true;
    }
    
 // Inside DonationService.java

    /**
     * Retrieves a single donation by its ID.
     */
    public Donation getDonationById(int donationId) {
        return donationRepository.findDonationById(donationId);
    }

    /**
     * [UPDATED] Handles updating an available donation.
     * We now pass the donorId for a security check.
     */
    public boolean updateDonationDetails(Donation donation, int donorId) {
        int rowsAffected = donationRepository.update(donation, donorId);
        return rowsAffected > 0;
    }
}
