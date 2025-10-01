# Build stage
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn -q -DskipTests package

# Run stage (non-root)
FROM eclipse-temurin:17-jre
RUN useradd -m appuser
USER appuser
WORKDIR /home/appuser
COPY --from=build /app/target/mottu-1.0.0.jar app.jar
EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]
