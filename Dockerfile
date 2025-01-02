FROM openjdk:18-jdk-alpine
COPY ./target/*.jar /app/app.jar
ENV DB_HOST=bank-db
CMD ["java","-jar","/app/app.jar"]
