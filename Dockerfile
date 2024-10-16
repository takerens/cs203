# Use OpenJDK 17 with Alpine Linux
FROM openjdk:17-jdk-alpine

# Argument to specify the JAR file
ARG JAR_FILE=target/*.jar

# Copy the JAR file to the container
COPY ${JAR_FILE} app.jar

# Command to run the JAR file
ENTRYPOINT ["java","-jar","/app.jar"]
