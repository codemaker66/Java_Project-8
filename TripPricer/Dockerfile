FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD build/libs/TripPricer-1.0.0.jar trip-pricer.jar
ENTRYPOINT ["java", "-jar", "/trip-pricer.jar"]