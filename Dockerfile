FROM openjdk:21
WORKDIR /ambulanceService
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]