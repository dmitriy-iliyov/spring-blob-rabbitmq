FROM maven:latest AS stage1
WORKDIR /app
COPY ../producer_service/pom.xml /app
RUN mvn dependency:resolve
COPY ../producer_service /app
RUN mvn clean
RUN mvn package -DskipTests

FROM openjdk:17 as final
COPY --from=stage1 /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar", "$ARGS"]