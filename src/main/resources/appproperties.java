spring.application.name=FoodDonation
# Server Port
server.port=8085

# --- SPRING DATASOURCE (HIKARI) CONFIGURATION ---
# This tells Spring Boot's auto-configured pool *what* to connect to.
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=igor4u
spring.datasource.driver-class-name=org.postgresql.Driver

# --- Optional: Fine-tune the pool ---
# Max number of connections in the pool
spring.datasource.hikari.maximum-pool-size=10
# How long a connection can live (prevents stale connections)
spring.datasource.hikari.max-lifetime=1800000 
# How long to wait for a connection before timing out
spring.datasource.hikari.connection-timeout=30000

#logging.level.org.springframework=DEBUG
