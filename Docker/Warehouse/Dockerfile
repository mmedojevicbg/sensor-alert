FROM openjdk:21-jdk-slim
WORKDIR /app
COPY /warehouse.jar ./warehouse.jar
EXPOSE 8080
CMD ["java", "-jar", "warehouse.jar"]