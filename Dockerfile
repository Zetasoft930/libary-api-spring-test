
# Etapa de build usando Maven e Temurin JDK 17
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app
COPY . .
RUN mvn clean install

# Etapa final com uma imagem otimizada do JDK
FROM amazoncorretto:17-alpine AS runtime

WORKDIR /app
COPY --from=build /app/target/libary.jar app.jar

#VARIAVEL DE AMBIENTE
ENV GLOBAL_POSTGRES_USER=postgres
ENV GLOBAL_POSTGRES_PASSWORD=root
ENV GLOBAL_POSTGRES_DOCKER_PORT=5432

EXPOSE 8003

ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-Djdk.disableProcessContainerDetection=true", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseContainerSupport", "-jar", "app.jar"]

