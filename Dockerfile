FROM maven:3.9.9-amazoncorretto-17

MAINTAINER zephyrquest

COPY src /home/app/src
COPY pom.xml /home/app

RUN mvn -f /home/app/pom.xml clean package -DskipTests

ENTRYPOINT ["java", "-jar", "/home/app/target/TicketingApp.jar"]