package com.hackathon.donation.controller;

import com.hackathon.donation.model.Donation;
import com.hackathon.donation.service.DonationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

// 1. Import Log4j 2 classes
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Controller
@RequestMapping("/home")
public class HomeController {

    // 2. Set up the logger instance for this class
    private static final Logger logger = LogManager.getLogger(HomeController.class);

    private final DonationService donationService;

    public HomeController(DonationService donationService) {
        this.donationService = donationService;
    }
    
    /**
     * Helper method to enforce session authentication and get the Home ID.
     * Throws IllegalStateException if the session is invalid.
     */
    private int getCurrentHomeId(HttpSession session) throws IllegalStateException {
        Integer userId = (Integer) session.getAttribute("userId");
        String userRole = (String) session.getAttribute("userRole");

        if (userId == null || !"HOME".equals(userRole)) {
            logger.warn("Session check failed: User is not logged in as HOME. userId: {}, userRole: {}", userId, userRole);
            throw new IllegalStateException("User not logged in as Home.");
        }
        return userId;
    }

    /**
     * Module 3: Home Dashboard (Browse Marketplace)
     * Displays all donations with status 'AVAILABLE'.
     */
    @GetMapping("/marketplace")
    public String showMarketplace(Model model, HttpSession session) {
        try {
            int homeId = getCurrentHomeId(session); // Check auth
            logger.info("Loading marketplace for HomeId: {}", homeId);
            
            List<Donation> availableDonations = donationService.getAllAvailableDonations();
            model.addAttribute("donations", availableDonations);
            
            logger.debug("Found {} available donations for marketplace.", availableDonations.size());
            return "home/marketplace"; // View for Module 3

        } catch (IllegalStateException e) {
            logger.warn("Redirecting to login: {}", e.getMessage());
            return "redirect:/home/login";
        }
    }

    /**
     * Module 4: Claim Donation
     * Handles the update query to change status to 'CLAIMED'.
     */
    @PostMapping("/claim/{donationId}")
    public String claimDonation(@PathVariable int donationId, HttpSession session) {
        int homeId;
        try {
            homeId = getCurrentHomeId(session);
            logger.info("Processing claim request: HomeId: {} for DonationId: {}", homeId, donationId);
            
            boolean success = donationService.claimDonation(donationId, homeId);
            
            if (success) {
                // Success: Donation claimed
                logger.info("Claim successful for DonationId: {}", donationId);
                return "redirect:/home/marketplace?claimed";
            } else {
                // Failure: Race condition or invalid ID (handled by service)
                logger.warn("Claim failed (likely race condition) for DonationId: {}", donationId);
                return "redirect:/home/marketplace?claimError";
            }
        } catch (IllegalStateException e) {
            // Session check failed
            logger.warn("Redirecting to login: {}", e.getMessage());
            return "redirect:/home/login";
        } catch (Exception e) {
            // 3. Replaced System.err.println with logger.error
            // This catches the 'RuntimeException' from the service/repository
            logger.error("CRITICAL: System error while claiming donationId: {}. Error: {}", donationId, e.getMessage(), e);
            return "redirect:/home/marketplace?systemError";
        }
    }
}
