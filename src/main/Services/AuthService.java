package com.hackathon.donation.service;

import com.hackathon.donation.model.Donor;
import com.hackathon.donation.model.Home;
import com.hackathon.donation.repository.AuthRepository;
import org.springframework.stereotype.Service;

@Service 
public class AuthService {
    
    private final AuthRepository authRepository;

    // Spring will automatically inject AuthRepository here
    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }


    public Donor authenticateDonor(String email, String password) {
        // 1. Fetch the user record by email from the database
        Donor donor = authRepository.findDonorByEmail(email);
        
        // 2. Perform the authentication check
        if (donor != null && donor.getPasswordHash() != null && donor.getPasswordHash().equals(password)) {
            // Password match successful
            return donor;
        }
        
        // Failed authentication
        return null;
    }

    public Home authenticateHome(String email, String password) {
        // 1. Fetch the user record by email from the database
        Home home = authRepository.findHomeByEmail(email);
        
        // 2. Perform the authentication check
        if (home != null && home.getPasswordHash() != null && home.getPasswordHash().equals(password)) {
            // Password match successful
            return home;
        }
        
        // Failed authentication
        return null;
    }
    
    // Inside com.hackathon.donation.service.AuthService.java

    /**
     * Registers a new Donor.
     * @return true if registration is successful, false if email already exists.
     */
    public boolean registerDonor(Donor donor) {
        // 1. Check if email already exists
        if (authRepository.findDonorByEmail(donor.getEmail()) != null) {
            return false; // Email already in use
        }
        
        // 2. TODO: HASH THE PASSWORD
        // In a real app, you would hash donor.getPasswordHash() here before saving.
        
        // 3. Save the new donor
        authRepository.saveDonor(donor);
        return true;
    }

    /**
     * Registers a new Home.
     * @return true if registration is successful, false if email already exists.
     */
    public boolean registerHome(Home home) {
        // 1. Check if email already exists
        if (authRepository.findHomeByEmail(home.getContactEmail()) != null) {
            return false; // Email already in use
        }
        
        // 2. TODO: HASH THE PASSWORD
        
        // 3. Save the new home
        authRepository.saveHome(home);
        return true;
    }
}
