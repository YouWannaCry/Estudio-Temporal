FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY . .
RUN chmod +x mvnw && ./mvnw -q -DskipTests dependency:go-offline
EXPOSE 8080
CMD ["./mvnw", "spring-boot:run"]
