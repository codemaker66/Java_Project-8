FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD build/libs/TourGuide-1.0.0.jar tour-guide.jar
ENTRYPOINT ["java", "-jar", "/tour-guide.jar"]