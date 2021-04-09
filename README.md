# TourGuide

TourGuide is a Spring Boot application that allows users to see nearby tourist attractions and get discounts on hotel stays as well as tickets to various shows.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

- Java 1.8
- Maven 3.6.3
- Docker

### Installing

A step by step series of examples that tell you how to get a development env running:

1.Install Java:

https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html

2.Install Maven:

https://maven.apache.org/install.html

3.Install Docker:

https://docs.docker.com/desktop/

### Running The App

After installing everything, make sure to run Docker than import the code into a folder of your choice and do the folowing steps :

1. Go to the GpsUtil folder and execute the following commands :
    1. `./gradlew build`
    2. `docker build -t gps-util .`
2. Go to the RewardCentral folder and execute the following commands :
    1. `./gradlew build`
    2. `docker build -t reward-central .`
3. Go to the TourGuide folder and execute the following commands :
    1. `./gradlew build`
    2. `docker build -t tour-guide .`
4. Go to the TripPricer folder and execute the following commands :
    1. `./gradlew build`
    2. `docker build -t trip-pricer .`
5. Go back to the main folder that contain the project and execute the following command :
    1. `docker compose up`




### Testing

The app has unit tests written. To run them from gradle, go to the TourGuide folder and execute the below command.

`./gradlew test`