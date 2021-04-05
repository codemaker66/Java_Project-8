FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD build/libs/GpsUtil-1.0.0.jar gps-util.jar
ENTRYPOINT ["java", "-jar", "/gps-util.jar"]