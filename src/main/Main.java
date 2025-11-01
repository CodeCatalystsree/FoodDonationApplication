// //package com.hackathon.donation;
// //
// //import org.springframework.boot.SpringApplication;
// //import org.springframework.boot.autoconfigure.SpringBootApplication;
// //
// //@SpringBootApplication
// //public class FoodDonationApplication {
// //
// //	public static void main(String[] args) {
// //		SpringApplication.run(FoodDonationApplication.class, args);
// //	}
// //
// //}


// package com.hackathon.donation;

// import com.hackathon.donation.controller.AuthController;
// import com.hackathon.donation.controller.DonorController;
// import com.hackathon.donation.controller.HomeController;
// import com.hackathon.donation.repository.AuthRepository;
// import com.hackathon.donation.repository.DonationRepository;
// import com.hackathon.donation.service.AuthService;
// import com.hackathon.donation.service.DonationService;

// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// @SpringBootApplication
// @Configuration
// public class FoodDonationApplication {

//     public static void main(String[] args) {
//         SpringApplication.run(FoodDonationApplication.class, args);
//     }

//     // --- REPOSITORY BEANS (Data Access Layer) ---

//     // 1. Manually create the DonationRepository bean
//     @Bean
//     public DonationRepository donationRepository() {
//         return new DonationRepository();
//     }

//     // 2. Manually create the AuthRepository bean
//     @Bean
//     public AuthRepository authRepository() {
//         return new AuthRepository();
//     }

//     // --- SERVICE BEANS (Business Logic Layer) ---

//     // 3. Create the DonationService, injecting the DonationRepository
//     @Bean
//     public DonationService donationService(DonationRepository donationRepository) {
//         return new DonationService(donationRepository);
//     }

//     // 4. Create the AuthService, injecting the AuthRepository
//     @Bean
//     public AuthService authService(AuthRepository authRepository) {
//         return new AuthService(authRepository);
//     }

//     // --- CONTROLLER BEANS (Web Layer) ---

//     // 5. Create the AuthController, injecting the AuthService
//     @Bean
//     public AuthController authController(AuthService authService) {
//         return new AuthController(authService);
//     }

//     // 6. Create the DonorController, injecting the DonationService
//     @Bean
//     public DonorController donorController(DonationService donationService) {
//         return new DonorController(donationService);
//     }

//     // 7. Create the HomeController, injecting the DonationService
//     @Bean
//     public HomeController homeController(DonationService donationService) {
//         return new HomeController(donationService);
//     }
// }
package com.hackathon.donation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FoodDonationApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodDonationApplication.class, args);
	}

}
