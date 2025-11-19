FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY build/libs/app.jar app.jar

EXPOSE 8080

ENV SPRING_DATASOURCE_URL=jdbc:mariadb://host.docker.internal:3306/expoospace?useSSL=false&serverTimezone=UTC \
    SPRING_DATASOURCE_USERNAME=root \
    SPRING_DATASOURCE_PASSWORD=

ENTRYPOINT ["java", "-jar", "app.jar"]