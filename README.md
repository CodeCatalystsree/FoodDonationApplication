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

Project Workflow:
1. Project Introduction: The "Elevator Pitch"
"Our project is a full-stack Java web application called 'Food Connect'.
The problem we are solving is the critical gap between food surplus at restaurants and businesses, and the high demand for food at charitable institutions like children's care and old-age homes.
Our solution is a robust, two-sided marketplace that allows donors to easily post available food, and homes to browse and claim that food in real-time."
________________________________________
2. Technical Architecture
"We built this application using a classic, 3-tier (or MVC) architecture, which separates our code into distinct, maintainable layers:
•	1. Presentation Layer (View): We used Thymeleaf for server-side rendering. This allows us to build dynamic HTML pages that are directly integrated with our Java backend.
•	2. Web/Business Layer (Controller/Service): We used Spring Boot to handle all web requests, manage application logic, and implement user session management.
•	3. Data Access Layer (Repository): We used plain JDBC to write all our own SQL queries, giving us full control over database interactions with our PostgreSQL database.
For our hackathon's advanced goals, we also implemented two key production-ready features:
•	Performance: We refactored our code to use a Hikari Connection Pool. Instead of opening and closing a new database connection for every query, our application now re-uses connections from a managed pool, which is significantly faster and more efficient.
•	Monitoring: We integrated Log4j 2 for enterprise-grade logging. All application events—from logins to database errors—are logged to both the console and a separate app.log file for debugging and monitoring."
________________________________________
3. The Core Workflow: User Journeys
"Our application has two main user journeys that demonstrate all five of our core modules."
Journey 1: The Donor (Modules 1, 2, 5)
"This is the 'supply' side of our marketplace."
1.	Registration & Login: A new donor (like a restaurant) first registers. Our AuthService validates their data, and the AuthRepository saves them to the donors table.
2.	Post Donation (Module 2): Once logged in, the donor accesses the 'Post New Donation' form. When they submit details like 'Food Type' and 'Quantity', our DonationService creates a new record in the donations table with a status of 'AVAILABLE'.
3.	View Dashboard (Module 1): The donor is then redirected to their personal 'Donor Dashboard'. This page runs a SELECT query to show only that donor's posts and their current status ('AVAILABLE' or 'CLAIMED').
4.	Manage Donations (Module 5): If a donation is still 'AVAILABLE', the donor has full control. They can use the 'Donation Management' features on their dashboard to Edit the post details (running an UPDATE query) or Delete the post (running a DELETE query)."
Journey 2: The Home (Modules 3, 4)
"This is the 'demand' side of our marketplace."
1.	Registration & Login: A verified home (like an orphanage) registers and logs in. Our AuthController validates them and creates a secure HttpSession.
2.	Browse Marketplace (Module 3): The home is immediately directed to the 'Home Dashboard', which is the central marketplace. This page is our most important 'read' module: it runs a SELECT * FROM donations WHERE status = 'AVAILABLE' query. This shows a live, global view of all available food from all donors.
3.	Claim Donation (Module 4): When the home finds a donation they need, they click the 'Claim Donation' button. This is our most critical transaction:
o	It triggers an atomic UPDATE query in the DonationRepository.
o	The query is UPDATE donations SET status = 'CLAIMED', home_id = ? WHERE id = ? AND status = 'AVAILABLE'.
o	Using AND status = 'AVAILABLE' in the WHERE clause is a key design choice. It acts as a lock, ensuring that if two homes click 'Claim' at the exact same time, only the first one to hit the database can successfully claim the item. This prevents 'race conditions' and ensures data integrity.
o	The page then refreshes, and the claimed item is instantly removed from the marketplace for all other users."
________________________________________
4. Conclusion
"In summary, our project is a complete, end-to-end solution. It fulfills all five hackathon modules and demonstrates a scalable Java architecture, advanced performance tuning with connection pooling, and robust monitoring with Log4j 2."
 
Explaining the Hikari Connection Pool Implementation
"That's an excellent question. One of the key performance optimizations we implemented was moving from a basic JDBC connection-per-request model to a production-grade connection pool.
The Problem We Solved
"In our initial prototype, our repositories used DriverManager.getConnection() every time they needed to run a query. This is extremely inefficient.
1.	High Latency: It establishes a new, physical TCP connection to the PostgreSQL server for every single web request, which involves a slow network handshake.
2.	Resource Exhaustion: If 100 users visited our site at once, we would try to open 100 physical database connections, which would overwhelm and likely crash the database.
We solved this by integrating HikariCP, the high-performance connection pool that comes standard with Spring Boot."
The Solution: How the Pool Works
"A connection pool is like a 'valet service' for database connections. Instead of building a new connection from scratch every time, the pool pre-builds a set of 10 connections and keeps them 'idle' and ready.
When our code needs to run a query, it just asks the pool for a connection, which it gets almost instantly. When it's done, it 'closes' the connection, which just returns it to the pool, ready for the next request. This re-use is the key to high performance."
Our 3-Step Implementation Process
"We implemented this refactor in three main steps:"
Step 1: Dependency and Configuration (Telling Spring what to build)
•	"First, we ensured the spring-boot-starter-jdbc dependency was in our pom.xml. This automatically includes the HikariCP library.
•	Second, we moved all our hard-coded database credentials (URL, user, password) out of the Java code and into our application.properties file.
•	This is the most critical part: Spring Boot's auto-configuration sees these properties, automatically builds a fully-configured Hikari DataSource bean, and manages it for us. We don't have to write any manual pool-building code."
Step 2: Refactoring our Repositories (Telling Spring where to put it)
•	"This is where we used Dependency Injection. We refactored our DonationRepository and AuthRepository."
•	"Before, the code looked like this, managing its own connection:"
Java
// BEFORE: Inefficient and hard-coded
public class DonationRepository {
    private static final String DB_URL = "jdbc:postgresql://...";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, ...);
    }
}
•	"After, we refactored the code to ask for the pool to be given to it:"
Java
// AFTER: Efficient and managed by Spring
@Repository
public class DonationRepository {

    private final DataSource dataSource; // The Hikari Pool

    // Spring injects the pool here
    public DonationRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection getConnection() throws SQLException {
        // This is now super-fast: it just grabs a connection from the pool
        return dataSource.getConnection(); 
    }
}
Step 3: Cleaning up the Main Application
•	"Finally, because Spring's @Repository and @Service annotations were now automatically handling the bean creation, we were able to delete all the manual @Bean methods from our main FoodDonationApplication.java file. This makes our main class cleaner and relies on Spring's powerful component scanning."
The Result
"The result is a much faster, more scalable, and more professional application. We kept all of our original, plain JDBC query logic, but we fundamentally changed how we get the connections, which solved our biggest performance bottleneck."
Explaining the Log4j 2 Implementation
"That's a key part of our application's maturity. In our initial build, we used System.out.println and System.err.println for debugging. We quickly realized this was inefficient and unsustainable.
The Problem We Solved
"The old System.err.println method had three major problems:
1.	No Control: We couldn't filter logs by severity. We saw everything (INFO, DEBUG, ERROR) all jumbled together.
2.	No Persistence: All our debug messages were lost forever as soon as the console buffer was full or the application restarted.
3.	Performance: System.out is a blocking, slow I/O operation.
We made a deliberate technical decision to integrate a professional-grade logging framework, Log4j 2, to solve all three problems."
Our 3-Step Implementation Process
"We implemented this in three phases: managing the dependencies, configuring the logger, and implementing it in our code."
Step 1: Maven Dependency Management (The "Logger Swap")
•	"This was the most complex part. Spring Boot's default logging framework is Logback, not Log4j 2.
•	"By default, starters like spring-boot-starter-web and spring-boot-starter-jdbc automatically pull in Logback.
•	"To fix this, we had to modify our pom.xml to first exclude the default spring-boot-starter-logging from all other Spring Boot starters.
•	"Then, we added a single new dependency: spring-boot-starter-log4j2. This one dependency tells Spring Boot to automatically find and adopt Log4j 2 as its primary logging implementation."
Step 2: The log4j2.xml Configuration (The "Control Center")
•	"Next, we created a log4j2.xml file in our src/main/resources folder. This file is the 'brain' that tells Log4j 2 what to log and where to send it.
•	"Inside this file, we configured two 'Appenders', or destinations:
1.	A Console Appender: This continues to print logs to our Eclipse console, but now with clean, uniform formatting.
2.	A RollingFile Appender: This is the most important part. It writes all logs to a file in a new logs/app.log directory. We configured this to 'roll over' and create an archive file (like app-2025-11-02-1.log.gz) every time the log reaches 10MB, so we have a persistent history of our application.
•	"Finally, we set up our 'Loggers'. This is where we control the log levels:
o	For our own code (com.hackathon.donation), we set the level to DEBUG to see all our detailed, custom messages.
o	For all Spring Framework code (org.springframework), we set the level to INFO to reduce noise but still see important startup events."
Step 3: Implementation in Java (The "Action")
•	"The last step was to update our Java code. In every Controller and Service, we replaced System.err.println with structured logging.
•	"First, we get a static logger instance for the class:
Java
// At the top of HomeController.java
private static final Logger logger = LogManager.getLogger(HomeController.class);
•	"Then, we use it to log with context and severity. For example, in our 'Claim Donation' controller, we replaced our old error handling:
Java
// BEFORE:
} catch (Exception e) {
    System.err.println("Error claiming donation: " + e.getMessage());
    return "redirect:/home/marketplace?systemError";
}

// AFTER:
} catch (Exception e) {
    // This writes the full stack trace to app.log for debugging
    logger.error("CRITICAL: System error while claiming donationId: {}. Error: {}", 
                 donationId, e.getMessage(), e);
    return "redirect:/home/marketplace?systemError";
}
```"

The Result (The Payoff)
"The result is a professional, maintainable application. When a bug occurs now, we don't have to guess.
•	We can distinguish between a logic error (a logger.warn, like a race condition) and a system crash (a logger.error, like a database failure).
•	All this information is saved in logs/app.log, so we can go back and debug any issue that happened, even hours later.
•	It's also faster, because Log4j 2 is highly optimized and writes logs asynchronously, so it doesn't block the main application thread."


