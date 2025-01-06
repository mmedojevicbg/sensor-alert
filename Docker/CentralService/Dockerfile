FROM openjdk:21-jdk-slim
WORKDIR /app
COPY /centralservice.jar ./centralservice.jar
EXPOSE 8080
CMD ["java", "-jar", "centralservice.jar"]