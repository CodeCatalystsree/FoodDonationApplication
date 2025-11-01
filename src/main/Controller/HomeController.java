package com.hackathon.donation.controller;

import com.hackathon.donation.model.Donation;
import com.hackathon.donation.service.DonationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final DonationService donationService;

    public HomeController(DonationService donationService) {
        this.donationService = donationService;
    }
    
    // Helper method to enforce session authentication and get the Home ID
    private int getCurrentHomeId(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null || !"HOME".equals(session.getAttribute("userRole"))) {
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
            getCurrentHomeId(session); // Check auth
            
            List<Donation> availableDonations = donationService.getAllAvailableDonations();
            model.addAttribute("donations", availableDonations);
            
            return "home/marketplace"; // View for Module 3
        } catch (IllegalStateException e) {
            return "redirect:/home/login";
        }
    }

    /**
     * Module 4: Claim Donation
     * Handles the update query to change status to 'CLAIMED'.
     */
    @PostMapping("/claim/{donationId}")
    public String claimDonation(@PathVariable int donationId, HttpSession session) {
        try {
            int homeId = getCurrentHomeId(session);
            
            boolean success = donationService.claimDonation(donationId, homeId);
            
            if (success) {
                // Success: Donation claimed
                return "redirect:/home/marketplace?claimed";
            } else {
                // Failure: Race condition or invalid ID
                return "redirect:/home/marketplace?claimError";
            }
        } catch (IllegalStateException e) {
            return "redirect:/home/login";
        } catch (Exception e) {
            System.err.println("Error claiming donation: " + e.getMessage());
            return "redirect:/home/marketplace?systemError";
        }
    }
}
