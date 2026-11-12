# Etapa 1: Build (opcional si quieres compilar dentro del contenedor)
# Usa Maven con JDK 21 para compilar
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Runtime (más ligero)
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app

# Copiamos el JAR desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto que usará Spring Boot
EXPOSE 8080

# Ejecutamos la aplicación
ENTRYPOINT ["java", "--enable-preview", "-jar", "app.jar"]
