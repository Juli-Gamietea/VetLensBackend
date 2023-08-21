FROM openjdk:17-jdk
COPY target/vetlens.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "vetlens.jar"]