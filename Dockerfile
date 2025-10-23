FROM eclipse-temurin:17-jre
WORKDIR /app
COPY build/libs/fintech-kotlin-app-all.jar app.jar
EXPOSE 8080
CMD ["java","-jar","/app/app.jar"]
