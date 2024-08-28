FROM openjdk:11 as build
COPY mvnw .
COPY .mvn .mvn 
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests

FROM openjdk:11
COPY --from=build target/microservicemanagementtool-0.0.1-SNAPSHOT.jar /msmt.jar
EXPOSE 8080
ENTRYPOINT java -jar ./msmt.jar