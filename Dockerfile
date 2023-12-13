FROM maven:3.6.3-openjdk-11 as BUILD-APPS

LABEL DEVELOPER="ryanserfanru@gmail.com"
LABEL APPS-NAME="core-wallet-service"
WORKDIR /build-apps

COPY . /build-apps

RUN mvn verify -DskipTests
RUN mvn clean install -DskipTests
RUN ls -ltr

FROM openjdk:11 as RUN-APPS
USER root
LABEL DEVELOPER="ryanserfanru@gmail.com"
LABEL APPS-NAME="core-wallet-service"
WORKDIR /apps
COPY --from=BUILD-APPS /build-apps/target/*.jar apps/apps.jar
RUN ls -ltr
ENTRYPOINT ["java","-jar","apps.jar"]