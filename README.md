# Microservice: account-transaction-service
This is a sample microservice application to create account and make transactions

### Tech stack used:
1. Java 11
2. Spring Boot 2.5.6
3. Spring REST API (Spring Web)
4. Lombok 1.18.22
5. Spring Data JPA & H2 in-memory database 
6. springdoc-openapi-ui - Auto generated API documentation using OpenAPI 3.0 and exposed Swagger UI for the same 
7. Enabled /health,/info,/mappings,/metrics actuator endpoints
8. TDD approach using JUnit 5, Mockito, and Spring Boot Test
9. Spring boot starter validation for request validation  

---

### Steps to Run the application:

##### Step 1: Enter the project directory, 
    cd <project_directory>

##### Step 2: To clean and package the micro-service locally
    mvn clean package
 
##### Step 3: To start the spring boot application
    mvn spring-boot:run

---

### OpenAPI 3.0 Documentation URL: 
http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config

---

### Please find below sample curl commands to test the applications
##### Please create an account using below command
	curl --location --request POST 'http://localhost:8080/api/v1/accounts' \
	--header 'Content-Type: application/json' \
	--data-raw '{
	    "firstName": "Mani",
	    "lastName":"Reddy",
	    "balance": 10000
	}'

##### Account can be fetched using below command
	curl --location --request GET 'http://localhost:8080/api/v1/accounts/1000002'

##### Once accounts are created,  please use below command to transfer money from one account to other
	curl --location --request POST 'http://localhost:8080/api/v1/transactions' \
	--header 'Content-Type: application/json' \
	--data-raw '{
	    "fromAccountNumber": 10000021,
	    "toAccountNumber": 1000001,
	    "transactionAmount": 600,
	    "referenceNotes": "Test Amount"
	}'

##### Transactions can be retrieved based on source account or destination account
	curl --location --request GET 'http://localhost:8080/api/v1/transactions/1000002'