FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD build/libs/RewardCentral-1.0.0.jar reward-central.jar
ENTRYPOINT ["java", "-jar", "/reward-central.jar"]