# Microservice Management Tool

This application will allow the user to manage a database of microservices (their names, categories, authors, dependencies) through an E-UI webapp. 

The E-UI frontend calls a REST API in a Spring Boot application. Data is persisted using MongoDB. 

## Running

Run `docker-compose-build` at the root of the project to build Docker images for the E-UI frontend and Spring Boot REST application. 

Run `docker-compose-up` and the frontend will be available at [localhost:80](localhost:80). 

The REST API should be available at [localhost:8080](localhost:8080) should you wish to call it yourself. 

The database is managed by a MongoDB container with a persistent volume. MongoDB will be accessible on port 27017 of the host.

Port mappings may be changed in the docker-compose file. 

**NOTE**: If an error is encountered during the Maven build, this is because the mvnw file in the Java project has DOS line endings. Convert to Unix line endings by running `dos2unix mvnw`.
