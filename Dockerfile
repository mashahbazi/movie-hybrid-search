FROM eclipse-temurin:17.0.8.1_1-jdk-centos7 AS build
RUN mkdir -p /opt/app
COPY ./ /opt/app
WORKDIR /opt/app
RUN ./gradlew clean build -x test -x ktlintKotlinScriptCheck

FROM eclipse-temurin:17.0.8.1_1-jdk-centos7
RUN mkdir -p /opt/app
COPY --from=build /opt/app/build/libs/*.jar /opt/app/app.jar
ENTRYPOINT ["java", "-jar" , "/opt/app/app.jar"]