package com.hackathon.donation.controller;

import com.hackathon.donation.model.Donation;
import com.hackathon.donation.service.DonationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/donor") 
public class DonorController {

    private final DonationService donationService;
    
    public DonorController(DonationService donationService) {
        this.donationService = donationService;
    }

    // Helper method to enforce session authentication and get the Donor ID
    private int getCurrentDonorId(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null || !"DONOR".equals(session.getAttribute("userRole"))) {
            // If authentication fails, throw an exception to be caught by the calling method
            throw new IllegalStateException("User not logged in as Donor.");
        }
        return userId;
    }

    /**
     * Module 1: Donor Dashboard (Read)
     * Displays a list of all posts by the current donor.
     */
    @GetMapping("/dashboard")
    public String showDonorDashboard(Model model, HttpSession session) {
        try {
            int donorId = getCurrentDonorId(session);
            List<Donation> donorDonations = donationService.getDonationsByDonor(donorId);
            model.addAttribute("donations", donorDonations);
            return "donor/dashboard"; 
        } catch (IllegalStateException e) {
            return "redirect:/donor/login";
        }
    }

    // --- Module 2: Post New Donation (Create) ---

    @GetMapping("/post")
    public String showPostDonationForm(Model model, HttpSession session) {
        try {
            getCurrentDonorId(session); // Check auth
            model.addAttribute("donation", new Donation()); 
            return "donor/post-donation";
        } catch (IllegalStateException e) {
            return "redirect:/donor/login";
        }
    }

    @PostMapping("/post")
    public String handlePostDonationForm(@ModelAttribute Donation donation, HttpSession session) {
        try {
            int donorId = getCurrentDonorId(session);
            donation.setDonorId(donorId);
            
            donationService.createNewDonation(donation);
            
            return "redirect:/donor/dashboard?success"; 
        } catch (IllegalStateException e) {
            return "redirect:/donor/login";
        } catch (Exception e) {
            // Handle database or validation errors
            System.err.println("Error posting donation: " + e.getMessage());
            return "redirect:/donor/post?error"; 
        }
    }

    // --- Module 5: Donation Management (Delete) ---
    
    @PostMapping("/delete/{donationId}")
    public String deleteDonation(@PathVariable int donationId, HttpSession session) {
        try {
            getCurrentDonorId(session); // Check auth
            
            if (donationService.deleteDonation(donationId)) {
                return "redirect:/donor/dashboard?deleted";
            } else {
                // If deletion fails, it means the item was likely already claimed.
                return "redirect:/donor/dashboard?deleteError"; 
            }
        } catch (IllegalStateException e) {
            return "redirect:/donor/login";
        }
    }
    
 // Inside DonorController.java

    /**
     * GET /donor/edit/{id}
     * Shows the edit form, pre-filled with the donation's data.
     * Includes a security check to ensure the donor owns this post.
     */
    @GetMapping("/edit/{donationId}")
    public String showEditDonationForm(@PathVariable int donationId, Model model, HttpSession session) {
        // 1. Session check
        Integer currentDonorId = (Integer) session.getAttribute("userId");
        if (currentDonorId == null || !"DONOR".equals(session.getAttribute("userRole"))) {
            return "redirect:/donor/login";
        }
        
        // 2. Fetch the donation
        Donation donation = donationService.getDonationById(donationId);
        
        // 3. Security Check: Ensure donation exists AND belongs to the logged-in donor
        if (donation == null || donation.getDonorId() != currentDonorId) {
            return "redirect:/donor/dashboard?unauthorized";
        }
        
        // 4. Status Check: Ensure it's still available (optional, but good practice)
        if (!"AVAILABLE".equals(donation.getStatus())) {
             return "redirect:/donor/dashboard?error=claimed";
        }

        // 5. Add to model and show the form
        model.addAttribute("donation", donation);
        return "donor/edit-donation"; // Path to the new edit-donation.html
    }

    /**
     * POST /donor/update
     * Processes the submission from the edit form.
     */
    @PostMapping("/update")
    public String handleUpdateDonationForm(@ModelAttribute Donation donation, HttpSession session) {
        // 1. Session check
        Integer currentDonorId = (Integer) session.getAttribute("userId");
        if (currentDonorId == null || !"DONOR".equals(session.getAttribute("userRole"))) {
            return "redirect:/donor/login";
        }

        // 2. The service method's update call includes the donorId,
        // which prevents a malicious user from editing another donor's post.
        boolean success = donationService.updateDonationDetails(donation, currentDonorId);

        if (success) {
            return "redirect:/donor/dashboard?updated";
        } else {
            return "redirect:/donor/edit/" + donation.getId() + "?error";
        }
    }
}
