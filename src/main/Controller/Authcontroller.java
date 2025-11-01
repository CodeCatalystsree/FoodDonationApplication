package com.hackathon.donation.controller;

import com.hackathon.donation.model.Donor;
import com.hackathon.donation.model.Home;
import com.hackathon.donation.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {
    
    private final AuthService authService;

    // Dependency injection for AuthService
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Displays the main homepage (index.html)
    @GetMapping("/")
    public String homepage() {
        return "index";
    }
    
    // --- Shared Logout Endpoint ---
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Clear the entire session data
        return "redirect:/"; // Redirect back to the homepage
    }

    // --- Donor Login ---
    @GetMapping("/donor/login")
    public String showDonorLogin(Model model) {
        model.addAttribute("donor", new Donor());
        return "donor/login"; 
    }

    @PostMapping("/donor/login")
    public String processDonorLogin(@ModelAttribute Donor donor, HttpSession session) {
        // Authenticate donor using email and password
        Donor authenticatedDonor = authService.authenticateDonor(donor.getEmail(), donor.getPasswordHash());
        
        if (authenticatedDonor != null) {
            // Success: Set session attributes and redirect to Donor Dashboard (Module 1)
            session.setAttribute("userId", authenticatedDonor.getId());
            session.setAttribute("userRole", "DONOR");
            return "redirect:/donor/dashboard";
        } else {
            // Failure: Redirect back to login with error parameter
            return "redirect:/donor/login?error";
        }
    }

    // --- Home Login (Old Age Home/Orphanage) ---
    @GetMapping("/home/login")
    public String showHomeLogin(Model model) {
        model.addAttribute("home", new Home());
        return "home/login"; 
    }

    @PostMapping("/home/login")
    public String processHomeLogin(@ModelAttribute Home home, HttpSession session) {
        // Authenticate home user using email and password
        Home authenticatedHome = authService.authenticateHome(home.getContactEmail(), home.getPasswordHash());
        
        if (authenticatedHome != null) {
            // Success: Set session attributes and redirect to Marketplace (Module 3)
            session.setAttribute("userId", authenticatedHome.getId());
            session.setAttribute("userRole", "HOME");
            return "redirect:/home/marketplace";
        } else {
            // Failure
            return "redirect:/home/login?error";
        }
    }
    
 // Inside com.hackathon.donation.controller.AuthController.java

    // --- Donor Registration ---
    
    @GetMapping("/donor/register")
    public String showDonorRegisterForm(Model model) {
        model.addAttribute("donor", new Donor());
        return "donor/register"; // Path to the new register.html template
    }

    @PostMapping("/donor/register")
    public String processDonorRegistration(@ModelAttribute Donor donor, Model model) {
        if (authService.registerDonor(donor)) {
            return "redirect:/donor/login?registered"; // Success
        } else {
            // Failure (email exists)
            model.addAttribute("error", "An account with this email already exists.");
            return "donor/register";
        }
    }

    // --- Home Registration ---

    @GetMapping("/home/register")
    public String showHomeRegisterForm(Model model) {
        model.addAttribute("home", new Home());
        return "home/register"; // Path to the new register.html template
    }

    @PostMapping("/home/register")
    public String processHomeRegistration(@ModelAttribute Home home, Model model) {
        if (authService.registerHome(home)) {
            return "redirect:/home/login?registered"; // Success
        } else {
            // Failure (email exists)
            model.addAttribute("error", "An account with this email already exists.");
            return "home/register";
        }
    }
}
