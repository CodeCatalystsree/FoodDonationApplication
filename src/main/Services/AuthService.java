package com.hackathon.donation.service;

import com.hackathon.donation.model.Donor;
import com.hackathon.donation.model.Home;
import com.hackathon.donation.repository.AuthRepository;
import org.springframework.stereotype.Service;

// 1. Import Log4j 2 classes
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class AuthService {

    // 2. Set up the logger instance for this class
    private static final Logger logger = LogManager.getLogger(AuthService.class);

    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    /**
     * Authenticates a Donor based on email and password.
     * @return The authenticated Donor object, or null if authentication fails.
     */
    public Donor authenticateDonor(String email, String password) {
        if (email == null || password == null) {
            logger.warn("Authentication failed: email or password was null.");
            return null;
        }
        
        Donor donor = authRepository.findDonorByEmail(email.toLowerCase());
        
        if (donor == null) {
            logger.warn("Authentication failed for Donor email: {}. User not found.", email);
            return null;
        }

        // TODO: Implement proper BCrypt password hashing
        // This is a temporary plain-text check for the hackathon
        if (password.equals(donor.getPasswordHash())) {
            logger.info("Authentication successful for Donor: {}", email);
            return donor;
        } else {
            logger.warn("Authentication failed for Donor email: {}. Invalid password.", email);
            return null;
        }
    }

    /**
     * Authenticates a Home based on email and password.
     * @return The authenticated Home object, or null if authentication fails.
     */
    public Home authenticateHome(String email, String password) {
         if (email == null || password == null) {
            logger.warn("Authentication failed: email or password was null.");
            return null;
        }

        Home home = authRepository.findHomeByEmail(email.toLowerCase());
        
        if (home == null) {
            logger.warn("Authentication failed for Home email: {}. User not found.", email);
            return null;
        }

        // TODO: Implement proper BCrypt password hashing
        if (password.equals(home.getPasswordHash())) {
            logger.info("Authentication successful for Home: {}", email);
            return home;
        } else {
            logger.warn("Authentication failed for Home email: {}. Invalid password.", email);
            return null;
        }
    }

    /**
     * Registers a new Donor.
     * @return true if registration is successful, false if email already exists.
     */
    public boolean registerDonor(Donor donor) {
        // 1. Check if email already exists
        if (authRepository.findDonorByEmail(donor.getEmail()) != null) {
            logger.warn("Donor registration failed: Email already exists {}", donor.getEmail());
            return false; // Email already in use
        }
        
        // 2. TODO: HASH THE PASSWORD
        // In a real app, you would hash donor.getPasswordHash() here before saving.
        
        // 3. Save the new donor
        try {
            authRepository.saveDonor(donor);
            logger.info("New Donor registered successfully: {}", donor.getEmail());
            return true;
        } catch (Exception e) {
            logger.error("Error during donor registration for email {}: {}", donor.getEmail(), e.getMessage());
            return false;
        }
    }

    /**
     * Registers a new Home.
     * @return true if registration is successful, false if email already exists.
     */
    public boolean registerHome(Home home) {
        // 1. Check if email already exists
        if (authRepository.findHomeByEmail(home.getContactEmail()) != null) {
            logger.warn("Home registration failed: Email already exists {}", home.getContactEmail());
            return false; // Email already in use
        }
        
        // 2. TODO: HASH THE PASSWORD
        
        // 3. Save the new home
        try {
            authRepository.saveHome(home);
            logger.info("New Home registered successfully: {}", home.getContactEmail());
            return true;
        } catch (Exception e) {
            logger.error("Error during home registration for email {}: {}", home.getContactEmail(), e.getMessage());
            return false;
        }
    }
}
