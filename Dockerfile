# Create image based on lightwight JRE 
FROM openjdk:8-jre-alpine

# Set developer information
MAINTAINER Bassam Almahdy

# Assgin variable for build location
ARG JAR_FILE=/target/api.jar

# Copy this build to root directory of image 
COPY ${JAR_FILE} api.jar

# Set entery point of excution 
ENTRYPOINT ["/usr/bin/java"]

# Terminal command for running service 
CMD ["-jar", "api.jar"]

# Set export port to browse application 
EXPOSE 8080