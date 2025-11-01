package com.hackathon.donation.service;

import com.hackathon.donation.model.Donation;
import com.hackathon.donation.repository.DonationRepository;
import org.springframework.stereotype.Service;

// 1. Import Log4j 2 classes
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Service layer for implementing business logic related to Donations.
 * Handles the logic for all five core modules.
 */
@Service
public class DonationService {

    // 2. Set up the logger instance for this class
    private static final Logger logger = LogManager.getLogger(DonationService.class);

    private final DonationRepository donationRepository;

    public DonationService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    // --- Module 2: Post New Donation (CREATE) ---

    public void createNewDonation(Donation donation) {
        try {
            if (donation.getDonorId() == 0 || donation.getQuantity() == null || donation.getPickupTime() == null) {
                logger.warn("Donation creation failed validation: Missing required details.");
                throw new IllegalArgumentException("Missing required donation details.");
            }
            donationRepository.save(donation);
            logger.info("New donation created successfully by donorId: {}", donation.getDonorId());
        } catch (Exception e) {
            logger.error("Error during donation creation for donorId {}: {}", donation.getDonorId(), e.getMessage(), e);
            throw e; // Re-throw exception to be handled by the controller
        }
    }
    
    // --- Module 3: Home Dashboard (READ - All Available) ---

    public List<Donation> getAllAvailableDonations() {
        logger.debug("Fetching all available donations for marketplace.");
        List<Donation> donations = donationRepository.findAllAvailable();
        logger.debug("Found {} available donations.", donations.size());
        return donations;
    }
    
    // --- Module 1 & 5: Donor Dashboard and Management ---

    public List<Donation> getDonationsByDonor(int donorId) {
        logger.debug("Fetching donations for donorId: {}", donorId);
        List<Donation> donations = donationRepository.findByDonorId(donorId);
        logger.debug("Found {} donations for donorId: {}.", donations.size(), donorId);
        return donations;
    }

    /**
     * Retrieves a single donation by its ID. Used for the edit form.
     */
    public Donation getDonationById(int donationId) {
        logger.debug("Fetching donation by id: {}", donationId);
        Donation donation = donationRepository.findDonationById(donationId);
        if (donation == null) {
            logger.warn("Could not find donation with id: {}", donationId);
        }
        return donation;
    }

    /**
     * [UPDATED] Handles updating an available donation with security check.
     * This is the correct method to call from the controller.
     */
    public boolean updateDonationDetails(Donation donation, int donorId) {
        logger.debug("Attempting to update donationId: {} for donorId: {}", donation.getId(), donorId);
        int rowsAffected = donationRepository.update(donation, donorId);
        if (rowsAffected > 0) {
            logger.info("Successfully updated donationId: {}", donation.getId());
            return true;
        } else {
            logger.warn("Failed to update donationId: {}. Either not found, already claimed, or donorId did not match.", donation.getId());
            return false;
        }
    }
    
    /**
     * [DEPRECATED-STYLE] Original update method without security check.
     * We keep the new one (above) as the primary.
     */
    public boolean updateDonationDetails(Donation donation) {
        logger.warn("Calling updateDonationDetails without donorId security check. This may be unsafe.");
        int rowsAffected = donationRepository.update(donation); // Assumes old repo method
        return rowsAffected > 0;
    }

    public boolean deleteDonation(int donationId) {
        logger.debug("Attempting to delete donationId: {}", donationId);
        int rowsAffected = donationRepository.delete(donationId);
        if (rowsAffected > 0) {
            logger.info("Successfully deleted donationId: {}", donationId);
            return true;
        } else {
            logger.warn("Failed to delete donationId: {}. Either not found or already claimed.", donationId);
            return false;
        }
    }

    // --- Module 4: Claim Donation (UPDATE - Status) ---

    public boolean claimDonation(int donationId, int homeId) {
        logger.debug("HomeId: {} is attempting to claim donationId: {}", homeId, donationId);
        try {
            int rowsAffected = donationRepository.updateStatusToClaimed(donationId, homeId);
            
            if (rowsAffected > 0) {
                logger.info("SUCCESS: HomeId: {} successfully claimed donationId: {}", homeId, donationId);
                return true;
            } else {
                // This is a "race condition" or invalid ID, not a system error.
                logger.warn("FAIL: HomeId: {} failed to claim donationId: {}. Donation was likely already claimed (rowsAffected=0).", homeId, donationId);
                return false;
            }
        } catch (Exception e) {
            // This is a true system/SQL error.
            logger.error("CRASH: Error during claim attempt for donationId: {} by homeId: {}. Message: {}", donationId, homeId, e.getMessage(), e);
            throw e; // Re-throw to be caught by the controller's systemError block
        }
    }
}
