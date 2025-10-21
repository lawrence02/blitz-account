# -------- Build Stage --------
FROM maven:3.8.4-openjdk-17 AS build

COPY . .
RUN mvn clean install -DskipTests -Pprod -DskipTests

# -------- Runtime Stage --------
FROM openjdk:17-jdk

# Copy only the built JAR from the previous stage
COPY --from=build target/blitz-account-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 5151

CMD ["java", "-jar", "app.jar"]
