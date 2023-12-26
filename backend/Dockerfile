FROM gradle:jdk17-alpine

WORKDIR /app

COPY build/libs/demo-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

