# FoodDonationApplication
Food Donation Management System for Child Care and Old Age Homes
Code-a-thon Team-1
•	Priyanshu Kundu – 25MCA0167
•	Soumik Mandal - 25MCA0151
•	Arpita Bhaumik – 25MCA0170
•	Arnab Bhattacharya – 25MCA0141
•	Srilekha Hatui – 25MCA0179

What the project does :
The Food Donation Management System is a web-based application that connects people who have surplus food (Donors) with institutions that need it, like child care centres and old age homes (Homes).
Donors can post details about food they want to donate, and Homes can browse these available donations and claim them for pickup.

Why the project is useful :
 This system helps reduce food waste while supporting vulnerable communities. It creates a simple, organized digital connection between those who have surplus food  and those in need.
 
 Core Features :
•	Dual-Role Authentication: Separate login and registration for "Donors" and "Receiving Homes."
•	Donor Dashboard: A personal dashboard for donors to post new items and manage their existing donations.
•	Donation Marketplace: A central dashboard for homes to browse all "Available" donations from all donors.
•	One-Click Claim System: Homes can claim a donation, which atomically updates its status and removes it from the marketplace.
•	Donation Management: Donors can edit or delete their available donation posts.

 Technology Stack :
•	Backend: Spring Boot (Java 21)
•	Frontend: Thymeleaf (for server-side rendered HTML)
•	Database: PostgreSQL
•	Data Access: Spring Data JDBC (JdbcTemplate)
•	Build Tool: Maven

How users can get started :
1.	Clone the repository:
2.	git clone [your-repo-url]
3.	Set up the Database:
    o	Open PostgreSQL (e.g., pgAdmin).
    o	Create a new database named food_donation_db.
    o	Run the necessary SQL scripts to create the users and donations tables.
4.	Configure the Application:
   o	Open src/main/resources/application.properties.
   o	Update the spring.datasource.username and spring.datasource.password to match your local PostgreSQL credentials.
5.	Run the Application:
   o	Open the project in Spring Tool Suite (STS) or any Java IDE.
   o	Right-click on FoodDonationApplication.java and select "Run As" -> "Spring Boot App".
6.	Access the Application:
  o	Open your web browser and navigate to http://localhost:8080.

